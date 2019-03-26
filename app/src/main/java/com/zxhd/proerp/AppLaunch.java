package com.zxhd.proerp;

import android.app.Application;

import com.zxhd.proerp.utils.CrashUtils;
import com.zxhd.proerp.utils.Utils;

public class AppLaunch extends Application {

    private static AppLaunch sInstance;

    public static AppLaunch getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Utils.init(sInstance);
        CrashUtils.init();//崩溃相关


    }
}
