package com.ysl.baohuo.jobscheduler;

import android.annotation.SuppressLint;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobInfo.Builder;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.Log;

import androidx.annotation.RequiresApi;

@SuppressLint("NewApi")
public class MyJobSchedulerService extends JobService {

    public static void startJob(Context context){
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(context.getPackageName(), MyJobSchedulerService.class.getName());
        Builder builder = new Builder(8, componentName)
                .setPersisted(true);
        if (VERSION.SDK_INT < VERSION_CODES.N){
            builder.setPeriodic(1000);
        }else {
            builder.setMinimumLatency(1000);
        }
        jobScheduler.schedule(builder.build());
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("","onStartJob");
        if (VERSION.SDK_INT >= VERSION_CODES.N){
            startJob(this);
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
