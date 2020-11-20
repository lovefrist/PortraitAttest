package com.example.portraitattest.view.activity;

import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.TextureView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.portraitattest.R;
import com.example.portraitattest.adapter.FeatureAdapter;
import com.example.portraitattest.base.BaseActivity;
import com.example.portraitattest.callback.CameraDataCallback;
import com.example.portraitattest.camera.AutoTexturePreviewView;
import com.example.portraitattest.camera.CameraPreviewManager;
import com.example.portraitattest.model.FeatureInfo;
import com.example.portraitattest.model.SingleBaseConfig;
import com.example.portraitattest.presenter.impl.FeatureAPresenterImpl;
import com.example.portraitattest.presenter.inter.IFeatureAPresenter;
import com.example.portraitattest.utils.Base64Util;
import com.example.portraitattest.utils.HttpDESUtil;
import com.example.portraitattest.view.inter.IFeatureAView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class FeatureActivity extends BaseActivity implements IFeatureAView {

    private IFeatureAPresenter mIFeatureAPresenter;
    private AutoTexturePreviewView autoTexturePreviewView;
    private TextureView textureViewDraw;
    private RecyclerView rvInfo;
    private CameraPreviewManager previewManager;
    private int mWidth;
    private int mHeight;
    private Thread thread;
    private FeatureAdapter adapter;

    @Override
    protected int getActivity() {
        return R.layout.activity_feature;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIFeatureAPresenter = FeatureAPresenterImpl.getInstance();
    }

    @Override
    protected void initView() {
        autoTexturePreviewView = findViewById(R.id.autoTexturePreviewView);
        textureViewDraw = findViewById(R.id.texture_view_draw);
        rvInfo = findViewById(R.id.rv_info);
        rvInfo.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FeatureAdapter();
        rvInfo.setAdapter(adapter);
        previewManager = CameraPreviewManager.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCamera();
    }
    private void startCamera() {
        mHeight = SingleBaseConfig.getBaseConfig().getRgbAndNirHeight();
        mWidth = SingleBaseConfig.getBaseConfig().getRgbAndNirWidth();

        previewManager.startPreview(this, autoTexturePreviewView, mWidth, mHeight, (data, camera, width, height) -> {
            YuvImage image = new YuvImage(data, ImageFormat.NV21, width, height, null);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compressToJpeg(new Rect(0, 0, width, height), 100, stream);
            byte[] freshData = stream.toByteArray();
            long endTime = System.currentTimeMillis();
            if (endTime-startTime>2000) {
                if (thread==null||!thread.isAlive()) {
                    thread = new Thread(()->{
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("img", Base64Util.encode(freshData));
                            String request = HttpDESUtil.sendRequest("portrait/faceFeature", jsonObject.toString());
                            JSONObject object = new JSONObject(request);
                            if (object.getString("error_msg").equals("SUCCESS")) {
                                FeatureInfo featureInfo = new FeatureInfo();
                                featureInfo.setAge(object.getInt("age")+"");
                                featureInfo.setBeauty(object.getString("beauty"));
                                featureInfo.setExpression(object.getString("expression"));
                                featureInfo.setEmotion(object.getString("emotion"));
                                featureInfo.setFaceNum(object.getInt("face_num")+"");
                                featureInfo.setGender(object.getString("gender"));
                                featureInfo.setFaceShape(object.getString("faceShape"));
                                runOnUiThread(()->{
                                    adapter.setData(featureInfo);
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    thread.start();
                }
            }
        });
    }
    long startTime = System.currentTimeMillis();

    @Override
    protected void onStop() {
        super.onStop();
        previewManager.stopPreview();
    }
}