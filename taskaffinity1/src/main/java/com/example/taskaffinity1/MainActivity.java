package com.example.taskaffinity1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        System.out.println("invoke onNewIntent");

        setIntent(intent);//must store the new intent unless getIntent() will return the old one

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        System.out.println("invoke onPostResume,执行在onResume之后");
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("invoke onRestart");
    }
}
