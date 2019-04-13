package com.example.mylibrary;

import android.os.Bundle;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyActivity extends AppCompatActivity {

    @BindView(R2.id.tv) TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myactivity);
        ButterKnife.bind(this);

        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "lib......", Toast.LENGTH_LONG).show();
                System.out.println("点击");
            }
        });
    }
}
