package com.example.portraitattest.view.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.portraitattest.R;
import com.example.portraitattest.baiduyun.FaceSearch;
import com.example.portraitattest.base.BaseActivity;
import com.example.portraitattest.base.BaseApplication;
import com.example.portraitattest.callback.CameraDataCallback;
import com.example.portraitattest.camera.AutoTexturePreviewView;
import com.example.portraitattest.camera.CameraPreviewManager;
import com.example.portraitattest.model.SingleBaseConfig;
import com.example.portraitattest.utils.Base64Util;
import com.example.portraitattest.utils.HttpDESUtil;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private AutoTexturePreviewView autoTexturePreviewView;
    private TextureView textureViewDraw;
    private int qualityValue = 100;
    private ImageView ivFrame;
    private Button btnBack;
    private Button btnConfirm;
    private Dialog dialog;
    private Thread thread;
    private PopupWindow popupWindow;
    private Button btnStart;

    @Override
    protected int getActivity() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvent();
    }

    boolean dialogShow = false;

    private void initEvent() {
        btnBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        findViewById(R.id.btn_start).setOnClickListener(this);
    }
    String userName = "";
    String name = "";
    @Override
    protected void initView() {
        autoTexturePreviewView = findViewById(R.id.autoTexturePreviewView);
        textureViewDraw = findViewById(R.id.texture_view_draw);
        dialog = new Dialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.register_dialog, null);
        ivFrame = dialogView.findViewById(R.id.iv_frame);
        btnBack = dialogView.findViewById(R.id.btn_back);
        btnStart = findViewById(R.id.btn_start);
        btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        dialog.setContentView(dialogView);
        dialog.setCancelable(false);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showpopWind();
        startCamera();
    }

    private void showpopWind() {
        View inflate = LayoutInflater.from(BaseApplication.getContext()).inflate(R.layout.user_info_dialog, null);
        EditText etUserName = inflate.findViewById(R.id.et_userName);
        EditText etName = inflate.findViewById(R.id.et_name);
        inflate.findViewById(R.id.btn_ok).setOnClickListener(v -> {
            userName = etUserName.getText().toString();
            name = etName.getText().toString();
            popupWindow.dismiss();
        });
        popupWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        popupWindow.setFocusable(true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setContentView(inflate);
        btnStart.post(()->{
            popupWindow.showAtLocation(btnStart, Gravity.BOTTOM,0,0);
        });
    }

    long monitorTime = System.currentTimeMillis();
    private static final int mWidth = SingleBaseConfig.getBaseConfig().getRgbAndNirWidth();
    private static final int mHeight = SingleBaseConfig.getBaseConfig().getRgbAndNirHeight();
    CameraPreviewManager instance = CameraPreviewManager.getInstance();
    private static final String TAG = "RegisterActivity";
    private void startCamera() {
        //获取相机管理类的实例

        //设置开启哪个相机
        instance.setCameraFacing(CameraPreviewManager.CAMERA_FACING_FRONT);
        //开启相机
        instance.startPreview(this, autoTexturePreviewView, mWidth, mHeight, (data, camera, width, height) -> {
            YuvImage image = new YuvImage(data, ImageFormat.NV21, width, height, null);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compressToJpeg(new Rect(0, 0, width, height), qualityValue, stream);
            byte[] freshData = stream.toByteArray();
            long intervalTime = System.currentTimeMillis();
            if (intervalTime - monitorTime > 2000 && !dialogShow&&!dialog.isShowing()&&!popupWindow.isShowing()) {
                monitorTime= intervalTime;
                Log.e(TAG, "startCamera: "+dialogShow );
                if (thread==null||!thread.isAlive()) {
                    thread = new Thread(() -> {
                        try {
                            JSONObject object = new JSONObject();
                            String img = Base64Util.encode(freshData);
                            object.put("img", img);
                            String request = FaceSearch.faceDetect(freshData);
                            Log.e("TAG", "faceDetect: " + request);
                            JSONObject jsonObject = new JSONObject(request);
                            if (jsonObject.getString("error_msg").equals("SUCCESS")) {
                                Log.d("TAG", "onGetCameraData: 监测到人脸");
                                Bitmap bitmap = BitmapFactory.decodeByteArray(freshData, 0, freshData.length);
                                Matrix m = new Matrix();
                                m.setRotate(-90, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
                                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                                Bitmap finalBitmap = bitmap;
                                runOnUiThread(() -> {
                                    Toast.makeText(this, "监测到人脸", Toast.LENGTH_SHORT).show();
                                    ivFrame.setTag(img);
                                    ivFrame.setImageBitmap(finalBitmap);
                                    dialog.show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                dialog.dismiss();
                break;
            case R.id.btn_confirm:
                dialogShow = true;
                Log.e(TAG, "onClick: 开始上传注册" );
                registerImg();
                break;
            case R.id.btn_start:
                startCamera();
                break;


        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        instance.stopPreview();
        finish();
    }

    boolean isPost = false;

    private void registerImg() {
        new Thread(() -> {
            try {
                JSONObject object = new JSONObject();
                object.put("img", ivFrame.getTag());
                object.put("group", "family");
                object.put("userName", userName);
                object.put("name", name);
                String sendRequest = HttpDESUtil.sendRequest("portrait/register", object.toString());
                JSONObject jsonObject = new JSONObject(sendRequest);
                boolean registerUser = jsonObject.getBoolean("registerUser");
                boolean addUserMysql = jsonObject.getBoolean("addUserMysql");
                runOnUiThread(() -> {
                    isPost = true;
                    Toast.makeText(this, "注册人脸库" + registerUser + "添加到数据库" + addUserMysql, Toast.LENGTH_SHORT).show();
                    instance.stopPreview();
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
                });
            } finally {
                runOnUiThread(() -> {

                    dialog.dismiss();
                });
            }
        }).start();
    }
}