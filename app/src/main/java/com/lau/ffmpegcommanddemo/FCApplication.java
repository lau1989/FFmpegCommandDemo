package com.lau.ffmpegcommanddemo;

import android.app.Application;

import com.lau.ffmpegcommanddemo.fresco.FrescoManager;
import com.lau.ffmpegcommanddemo.util.AppCacheFileUtil;
import com.lau.ffmpegcommanddemo.util.RuntimeContext;

public class FCApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RuntimeContext.init(this);
        AppCacheFileUtil.init(this);
        FrescoManager.init(this);
    }
}
