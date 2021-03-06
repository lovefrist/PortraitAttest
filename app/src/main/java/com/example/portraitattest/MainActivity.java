package com.example.portraitattest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.portraitattest.base.BaseActivity;
import com.example.portraitattest.view.activity.AuthenticateActivity;
import com.example.portraitattest.view.activity.FeatureActivity;
import com.example.portraitattest.view.activity.RegisterActivity;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.Random;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout homePersonRl;
    private RelativeLayout homeFeatureRl;
    private RelativeLayout homeRegisterRl;

    @Override
    protected int getActivity() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvent();
    }

    private void initEvent() {
        homePersonRl.setOnClickListener(this);
        homeFeatureRl.setOnClickListener(this);
        homeRegisterRl.setOnClickListener(this);

    }


    protected void initView() {
        homePersonRl = findViewById(R.id.home_personRl);
        homeFeatureRl = findViewById(R.id.home_featureRl);
        homeRegisterRl = findViewById(R.id.home_registerRl);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //跳转人脸特征页面
            case R.id.home_featureRl:
                startActivity(new Intent(this, FeatureActivity.class));
                break;
            //跳转人脸认证页面
            case R.id.home_personRl:
                startActivity(new Intent(this, AuthenticateActivity.class));
                break;
            case R.id.home_registerRl:
                startActivity(new Intent(this, RegisterActivity.class));
                break;

        }
    }

    public void testBug(View view) {

        Random random = new Random();
        int i = random.nextInt(3);
        Log.d("TAG", "testBug: 开始测试代码"+i);
//        switch (i){
//            case 1:
//                CrashReport.testJavaCrash();
//                break;
//            case 2:
//                CrashReport.testANRCrash();
//                break;
//            case 3:
//                CrashReport.testNativeCrash();
//                break;
//            default:
//                CrashReport.testNativeCrash();
//                break;
//
//        }
        CrashReport.testANRCrash();



    }
}