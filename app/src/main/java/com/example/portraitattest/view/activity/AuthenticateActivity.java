package com.example.portraitattest.view.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.portraitattest.R;
import com.example.portraitattest.baiduyun.FaceSearch;
import com.example.portraitattest.base.BaseActivity;
import com.example.portraitattest.callback.CameraDataCallback;
import com.example.portraitattest.camera.AutoTexturePreviewView;
import com.example.portraitattest.camera.CameraPreviewManager;
import com.example.portraitattest.model.SingleBaseConfig;
import com.example.portraitattest.presenter.impl.AuthenticateAPresenterImpl;
import com.example.portraitattest.presenter.inter.IAuthenticateAPresenter;
import com.example.portraitattest.utils.Base64Util;
import com.example.portraitattest.utils.HttpDESUtil;
import com.example.portraitattest.view.inter.IAuthenticateAView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Random;

public class AuthenticateActivity extends BaseActivity implements IAuthenticateAView {
    private AutoTexturePreviewView previewView;
    private TextureView mDrawDetectFaceView;
    private static final int mWidth = SingleBaseConfig.getBaseConfig().getRgbAndNirWidth();
    private static final int mHeight = SingleBaseConfig.getBaseConfig().getRgbAndNirHeight();
    private ImageView ivFrame;
    private int qualityValue = 100;
    private TextView tvData;
    private IAuthenticateAPresenter mIAuthenticateAPresenter;
    private Paint paint;

    @Override
    protected int getActivity() {
        return R.layout.activity_authenticate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIAuthenticateAPresenter = AuthenticateAPresenterImpl.getInstance();
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2f);
        paint.setAntiAlias(true);


    }

    @Override
    protected void initView() {
        previewView = findViewById(R.id.autoTexturePreviewView);
        mDrawDetectFaceView = findViewById(R.id.texture_view_draw);
        tvData = findViewById(R.id.tv_data);
        ivFrame = findViewById(R.id.iv_frame);
        mDrawDetectFaceView.setOpaque(false);
        mDrawDetectFaceView.setKeepScreenOn(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        startCamera();
    }

    private static final String TAG = "MainActivity";
    Random random = new Random();
    long postTime = System.currentTimeMillis();


//    private void startCamera() {
//        //设置开启摄像头那个摄像头
//        CameraPreviewManager instance = CameraPreviewManager.getInstance();
//        instance.setCameraFacing(CameraPreviewManager.CAMERA_FACING_FRONT);
//        instance.startPreview(this, previewView, mWidth, mHeight, new CameraDataCallback() {
//
//            private Thread locationThread;
//            private Thread thread;
//
//            @Override
//            public void onGetCameraData(byte[] data, Camera camera, int width, int height) {
//                YuvImage image = new YuvImage(data, ImageFormat.NV21, width, height, null);
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                image.compressToJpeg(new Rect(0, 0, width, height), qualityValue, stream);
//                byte[] freshData = stream.toByteArray();
//                Bitmap bitmap = BitmapFactory.decodeByteArray(freshData, 0, freshData.length);
//                Matrix m = new Matrix();
//                m.setRotate(-90, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
//                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
//                if (this.thread == null || !this.thread.isAlive()) {
//                    long timeMillis = System.currentTimeMillis();
//                    long time = timeMillis - startTime;
//                    if (time > 1000 * 2) {
//                        startTime = timeMillis;
//                        locationThread = new Thread(() -> {
//                            synchronized (this) {
//                                JSONObject jsonObject = new JSONObject();
//                                try {
//                                    jsonObject.put("img", Base64Util.encode(freshData));
////                        String request = HttpDESUtil.sendRequest("portrait/imgLocation", jsonObject.toString());
//                                    String request = FaceSearch.faceDetect(freshData);
//                                    Log.e(TAG, "faceDetect: " + request);
//                                    JSONObject object = new JSONObject(request);
//                                    String errorMsg = object.getString("error_msg");
//                                    if (errorMsg.equals("SUCCESS")) {
//                                        JSONObject result = object.getJSONObject("result");
//                                        JSONObject location = result.getJSONArray("face_list").getJSONObject(0).getJSONObject("location");
//                                        Log.e(TAG, "onGetCameraData: " + location);
//                                        double left = location.getDouble("left");
//                                        double top = location.getDouble("top");
//                                        double avatarWidth = location.getDouble("width");
//                                        double avatarHeight = location.getDouble("height");
//                                        runOnUiThread(() -> {
//                                            showFrame(0, 0, mDrawDetectFaceView.getMeasuredWidth(), mDrawDetectFaceView.getMeasuredHeight());
//                                            Toast.makeText(AuthenticateActivity.this, "检测到人脸", Toast.LENGTH_SHORT).show();
//                                        });
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                        locationThread.start();
//                    }
//                }
//                long timeMillis = System.currentTimeMillis();
//                long time = timeMillis - postTime;
//                if (time > 1000 * 2) {
//                    postTime = timeMillis;
//                    if (this.thread == null || !this.thread.isAlive()) {
//                        this.thread = new Thread(() -> {
//                            synchronized (this) {
//                                JSONObject jsonObject = new JSONObject();
//                                try {
//                                    jsonObject.put("img", Base64Util.encode(freshData));
////                                    String request = HttpDESUtil.sendRequest("portrait/faceSearch", jsonObject.toString());
//                                    String request = FaceSearch.faceSearch(freshData);
//                                    Log.e(TAG, "faceSearch: " + request);
//                                    JSONObject object = new JSONObject(request);
//                                    String errorMsg = object.getString("error_msg");
//                                    Log.e(TAG, "faceSearch  errorMsg : " + errorMsg);
//                                    if (errorMsg.equals("SUCCESS")) {
//                                        JSONObject userList = object.getJSONObject("result").getJSONArray("user_list").getJSONObject(0);
//                                        Log.e(TAG, "onGetCameraData: " + userList);
//                                        String score = userList.getString("score");
//                                        String name = userList.getString("user_id");
//                                        runOnUiThread(() -> {
//                                            Log.e(TAG, "onGetCameraData: 获取成功" + "姓名:" + name + "\n准确率：" + score);
//                                            tvData.setText("姓名:" + name + "\n准确率：" + score);
//                                        });
//                                    } else {
//                                        runOnUiThread(() -> {
//                                            tvData.setText(errorMsg);
//                                        });
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                        this.thread.start();
//                    }
//                }
//                if (bitmap != null) {
//                    ivFrame.setImageBitmap(bitmap);
//                }
//
//            }
//        });
//    }
    private void startCamera() {
        //设置开启摄像头那个摄像头
        CameraPreviewManager instance = CameraPreviewManager.getInstance();
        instance.setCameraFacing(CameraPreviewManager.CAMERA_FACING_FRONT);
        instance.startPreview(this, previewView, mWidth, mHeight, new CameraDataCallback() {

            private Thread locationThread;
            private Thread thread;

            @Override
            public void onGetCameraData(byte[] data, Camera camera, int width, int height) {
                YuvImage image = new YuvImage(data, ImageFormat.NV21, width, height, null);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compressToJpeg(new Rect(0, 0, width, height), qualityValue, stream);
                byte[] freshData = stream.toByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(freshData, 0, freshData.length);
                Matrix m = new Matrix();
                m.setRotate(-90, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (this.thread == null || !this.thread.isAlive()) {
                    long timeMillis = System.currentTimeMillis();
                    long time = timeMillis - startTime;
                    if (time > 1000 * 2) {
                        startTime = timeMillis;
                        locationThread = new Thread(() -> {
                            synchronized (this) {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("img", Base64Util.encode(freshData));
//                        String request = HttpDESUtil.sendRequest("portrait/imgLocation", jsonObject.toString());
                                    String request = FaceSearch.faceDetect(freshData);
                                    Log.e(TAG, "faceDetect: " + request);
                                    JSONObject object = new JSONObject(request);
                                    String errorMsg = object.getString("error_msg");
                                    if (errorMsg.equals("SUCCESS")) {
                                        JSONObject result = object.getJSONObject("result");
                                        JSONObject location = result.getJSONArray("face_list").getJSONObject(0).getJSONObject("location");
                                        Log.e(TAG, "onGetCameraData: " + location);
                                        double left = location.getDouble("left");
                                        double top = location.getDouble("top");
                                        double avatarWidth = location.getDouble("width");
                                        double avatarHeight = location.getDouble("height");
                                        runOnUiThread(() -> {
                                            showFrame(0, 0, mDrawDetectFaceView.getMeasuredWidth(), mDrawDetectFaceView.getMeasuredHeight());
                                            if (!isToast){
                                                Toast.makeText(AuthenticateActivity.this, "检测到人脸", Toast.LENGTH_SHORT).show();
                                                isToast = true;
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        locationThread.start();
                    }
                }
                long timeMillis = System.currentTimeMillis();
                long time = timeMillis - postTime;
                if (time > 1000 * 2) {
                    postTime = timeMillis;
                    if (this.thread == null || !this.thread.isAlive()) {
                        this.thread = new Thread(() -> {
                            synchronized (this) {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("img", Base64Util.encode(freshData));
                                    String request = HttpDESUtil.sendRequest("portrait/faceSearch", jsonObject.toString());
//                                    String request = FaceSearch.faceSearch(freshData);
                                    Log.e(TAG, "faceSearch: " + request);
                                    JSONObject object = new JSONObject(request);
                                    String errorMsg = object.getString("error_msg");
                                    Log.e(TAG, "faceSearch  errorMsg : " + errorMsg);
                                    if (errorMsg.equals("SUCCESS")) {
                                        String score = object.getString("score");
                                        String name = object.getString("name");
                                        runOnUiThread(() -> {
                                            Log.e(TAG, "onGetCameraData: 获取成功" + "姓名:" + name + "\n准确率：" + score);
                                            tvData.setText("姓名:" + name + "\n准确率：" + score);
                                        });
                                    } else {
                                        runOnUiThread(() -> {
                                            tvData.setText(errorMsg);
                                        });
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        this.thread.start();
                    }
                }
                if (bitmap != null) {
                    ivFrame.setImageBitmap(bitmap);
                }

            }
        });
    }
    boolean isToast = false;
    long startTime = System.currentTimeMillis();

    private void showFrame(double left, double top, double avatarWidth, double avatarHeight) {
        Log.e(TAG, "onGetCameraData: 开始画圆");
            Canvas canvas = mDrawDetectFaceView.lockCanvas();
            if (canvas == null) {
                Log.e(TAG, "showFrame: 结束画圆");
                mDrawDetectFaceView.unlockCanvasAndPost(canvas);
                return;
            }
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            float cx = (float) (left + avatarWidth / 2);
            float cy = (float) (top + avatarHeight / 2);
            float radius;
            if (avatarWidth < avatarHeight) {
                radius = (float) (avatarWidth / 2);
            } else {
                radius = (float) (avatarHeight / 2);
            }
            canvas.drawCircle(cx, cy, radius, paint);

            mDrawDetectFaceView.unlockCanvasAndPost(canvas);
    }


    @Override
    protected void onStop() {
        super.onStop();
        CameraPreviewManager.getInstance().stopPreview();
    }

}