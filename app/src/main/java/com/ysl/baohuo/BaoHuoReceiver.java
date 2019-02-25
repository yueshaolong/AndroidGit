package com.ysl.baohuo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BaoHuoReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.equals(Intent.ACTION_SCREEN_OFF)){
            context.getApplicationContext().startActivity(new Intent(context.getApplicationContext(), BaoHuoActivity.class));
        }else if (intent.equals(Intent.ACTION_SCREEN_ON)){
            Activity activity = BaoHuoManager.getInstance().getActivity();
            if (activity != null)
                activity.finish();
        }
    }
}
