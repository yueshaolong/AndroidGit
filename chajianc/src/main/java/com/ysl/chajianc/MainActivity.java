package com.ysl.chajianc;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("chajianc----->", "onCreate: 我是插件activity");
//        setContentView(R.layout.activity_main);

        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_main,
                null);
        setContentView(view);
    }
}
