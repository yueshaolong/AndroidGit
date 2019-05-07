package com.ysl.baohuo.foregroundservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    private static final String TAG = "MyService";
    public static final int NOTICE_ID = 100;


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyService---->onCreate被调用，启动前台service");
        //如果API大于18，需要弹出一个可见通知
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2){
            startForeground(NOTICE_ID, new Notification());
        }else if (Build.VERSION.SDK_INT < VERSION_CODES.O) {
            startForeground(NOTICE_ID, new Notification());
            startService(new Intent(getApplicationContext(), InnerService.class));
        } else {
            //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            //构建通知渠道
            NotificationChannel channel = new NotificationChannel("channel", "前台服务", NotificationManager.IMPORTANCE_NONE);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
                startForeground(NOTICE_ID, new NotificationCompat.Builder(this, "channel").build());
            }
        }
    }

    public static class InnerService extends Service{
        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
        @Override
        public void onCreate() {
            super.onCreate();
            startForeground(NOTICE_ID, new Notification());
            stopSelf();
        }
    }
}
