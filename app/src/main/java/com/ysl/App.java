package com.ysl;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        //把插件apk加载进去
//        LoadUtil.loadChaJian(this);
//
//        HookUtil.hookStartAct();
//        HookUtil.hookHander();
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
