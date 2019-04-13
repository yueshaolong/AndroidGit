package com.ysl.baohuo;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.ysl.helloworld.R;

public class MyActivity extends Activity {

    private BaoHuoReceiver baoHuoReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_activity);

        baoHuoReceiver = new BaoHuoReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(baoHuoReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (baoHuoReceiver != null)
            unregisterReceiver(baoHuoReceiver);
    }
}
