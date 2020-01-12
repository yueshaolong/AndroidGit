package com.ysl.chajianc;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("chajianc----->", "onCreate: 我是插件activity");
//        setContentView(R.layout.activity_main);
    }
}
