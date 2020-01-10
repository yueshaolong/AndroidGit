package com.ysl.chajian;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ysl.helloworld.R;

public class ProxyActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("------>", "onCreate: 我是代理Activity");
        setContentView(R.layout.proxyact);
    }
}
