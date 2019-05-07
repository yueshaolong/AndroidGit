package com.ysl.baohuo.onepix;

import android.app.Activity;

import java.lang.ref.WeakReference;

public class OnePixManager {
    private WeakReference<Activity> activityWeakReference;

    private OnePixManager(){}
    private static class OnePixManagerHelper{
        private static OnePixManager onePixManager = new OnePixManager();
    }
    public static OnePixManager getInstance(){
        return OnePixManagerHelper.onePixManager;
    }

    public void setOnePixActivity(Activity activity){
        activityWeakReference = new WeakReference<>(activity);
    }

    public Activity getActivity() {
        return activityWeakReference.get();
    }
}
