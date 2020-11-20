package com.example.portraitattest.base;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

public class BaseApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(this,"327e9d7b7b",false);
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
