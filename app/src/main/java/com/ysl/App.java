package com.ysl;

import android.app.Application;

import com.ysl.chajian.HookUtil;
import com.ysl.chajian.LoadUtil;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //把插件apk加载进去
        LoadUtil.loadChaJian(this);

        HookUtil.hookStartAct();
        HookUtil.hookHander();
    }

}
