package com.ysl.chajianc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("chajianc----->", "onCreate: 我是插件activity");
//        setContentView(R.layout.activity_main);
    }
}
