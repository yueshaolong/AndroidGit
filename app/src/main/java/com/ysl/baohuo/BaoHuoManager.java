package com.ysl.baohuo;

import android.app.Activity;

public class BaoHuoManager {
    private Activity activity;

    private BaoHuoManager(){}
    private static BaoHuoManager baoHuoManager;
    public static BaoHuoManager getInstance(){
        if (baoHuoManager == null)
            baoHuoManager = new BaoHuoManager();
        return baoHuoManager;
    }

    public void setBaoHuoActivity(Activity activity){
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }
}
