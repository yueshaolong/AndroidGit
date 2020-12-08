package com.ysl.ditu;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ysl.helloworld.R;

/**
 * 帧动画
 */

public class FrameAnimationActivity extends AppCompatActivity {

    private ImageView imageView;
    private AnimationDrawable animationDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frameanimation);

        imageView = findViewById(R.id.img_show);
        animationDrawable = (AnimationDrawable) imageView.getBackground();

        Glide.with(this).load(R.drawable.a).into((ImageView) findViewById(R.id.gif));

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null != animationDrawable && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != animationDrawable && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }
}
