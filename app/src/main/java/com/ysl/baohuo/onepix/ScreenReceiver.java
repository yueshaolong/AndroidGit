package com.ysl.baohuo.onepix;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.equals(Intent.ACTION_SCREEN_OFF)){
            context.getApplicationContext().startActivity(new Intent(context.getApplicationContext(), OnePixActivity.class));
        }else if (intent.equals(Intent.ACTION_SCREEN_ON)){
            Activity activity = OnePixManager.getInstance().getActivity();
            if (activity != null)
                activity.finish();
        }
    }
}
