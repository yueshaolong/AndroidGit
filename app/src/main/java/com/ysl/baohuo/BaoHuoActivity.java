package com.ysl.baohuo;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class BaoHuoActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        LayoutParams attributes = window.getAttributes();
        attributes.width = 1;
        attributes.height = 1;
        attributes.x = 0;
        attributes.y = 0;
        window.setAttributes(attributes);
        BaoHuoManager.getInstance().setBaoHuoActivity(this);
    }
}
