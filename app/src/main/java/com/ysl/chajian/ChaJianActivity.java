package com.ysl.chajian;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ysl.helloworld.R;

import java.lang.reflect.Method;

public class ChaJianActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chajian);

        new RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {

                });

        //把插件apk加载进去
        LoadUtil.loadChaJian(this);
        HookUtil.hookStartAct();
        HookUtil.hookHander();

        //调用插件类
        findViewById(R.id.button2).setOnClickListener(v -> {
            printClassLoader();
            try {
                Class<?> aClass = Class.forName("com.ysl.chajianc.Test");
                Method print = aClass.getMethod("print");
                print.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //启动插件activity
        findViewById(R.id.buttonact).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.ysl.chajianc",
                    "com.ysl.chajianc.MainActivity"));
            startActivity(intent);
        });
    }



    private void printClassLoader() {
        ClassLoader classLoader = getClassLoader();
        System.out.println("-----自己的----->"+classLoader);
        while (classLoader.getParent() != null){
            classLoader = classLoader.getParent();
            System.out.println("-----父类的----->"+classLoader);
        }

        System.out.println("-----Activity.java的----->"+ Activity.class.getClassLoader());
    }
}
