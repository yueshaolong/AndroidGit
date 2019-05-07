package com.ysl.baohuo;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.ysl.baohuo.accountservice.AccountHelper;
import com.ysl.baohuo.foregroundservice.MyService;
import com.ysl.baohuo.jobscheduler.MyJobSchedulerService;
import com.ysl.baohuo.onepix.ScreenReceiver;
import com.ysl.baohuo.systemservice.OrdinaryService;
import com.ysl.helloworld.R;

public class MyActivity extends Activity {

    private ScreenReceiver screenReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_activity);

        //一像素保活
        /*screenReceiver = new ScreenReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(screenReceiver, intentFilter);*/

        //前台服务保活
//        startService(new Intent(getApplicationContext(), MyService.class));

        //系统服务粘性特点拉活
//        startService(new Intent(getApplicationContext(), OrdinaryService.class));

        //账号同步拉活
//        AccountHelper.addAccount(this);
//        AccountHelper.autoSync();

        //JobScheduler拉活
        MyJobSchedulerService.startJob(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (screenReceiver != null)
            unregisterReceiver(screenReceiver);
    }
}
