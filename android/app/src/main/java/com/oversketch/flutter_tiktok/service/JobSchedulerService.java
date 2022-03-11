package com.oversketch.flutter_tiktok.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {
    private static final String TAG = JobSchedulerService.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "使用JobScheduler进行保活 Service created");
    }
    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e(TAG, "使用JobScheduler进行保活 onStopJob ");
        return false;
    }

}
