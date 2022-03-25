package com.oversketch.flutter_tiktok.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.oversketch.flutter_tiktok.work.CleanUpWorker;
import com.oversketch.flutter_tiktok.work.DelayWorker;
import com.oversketch.flutter_tiktok.work.DelayWorker2;
import com.oversketch.flutter_tiktok.work.DelayWorker3;
import com.oversketch.flutter_tiktok.work.DelayWorker4;

import java.util.concurrent.TimeUnit;

public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "开机启动成功", Toast.LENGTH_LONG).show();
        Log.i("❤驭霖·骏泊☆Myth.Mayor❤", "BootBroadcastReceiver --- onReceive: " + intent.getAction());
        if (intent.getAction().equals(ACTION)) {
            Toast.makeText(context, "开机启动成功 PlayService", Toast.LENGTH_LONG).show();
            //要启动的Activity
//            Intent bootIntent = new Intent(context, MainActivity.class);
//            bootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(bootIntent);

//            Intent bootIntent = new Intent(context, UseJobService.class);
////            Intent bootIntent = new Intent(context, PlayService.class);
////            bootIntent.addFlags()
////            context.startService(bootIntent);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                context.startForegroundService(bootIntent);
//            } else {
//                context.startService(bootIntent);
//            }
            worker(context);
        }
    }
    private void worker(Context context){
        WorkRequest delayWorker1 =
                new OneTimeWorkRequest.Builder(DelayWorker.class)
                        .setInitialDelay(3, TimeUnit.MINUTES)
                        .build();
        WorkRequest delayWorker2 =
                new OneTimeWorkRequest.Builder(DelayWorker2.class)
                        .setInitialDelay(6, TimeUnit.MINUTES)
                        .build();
        WorkRequest delayWorker3 =
                new OneTimeWorkRequest.Builder(DelayWorker3.class)
                        .setInitialDelay(9, TimeUnit.MINUTES)
                        .build();
        WorkRequest delayWorker4 =
                new OneTimeWorkRequest.Builder(DelayWorker4.class)
                        .setInitialDelay(12, TimeUnit.MINUTES)
                        .build();
        WorkManager
                .getInstance(context)
                .enqueue(delayWorker1);
        WorkManager
                .getInstance(context)
                .enqueue(delayWorker2);
        WorkManager
                .getInstance(context)
                .enqueue(delayWorker3);
        WorkManager
                .getInstance(context)
                .enqueue(delayWorker4);

        PeriodicWorkRequest saveRequest =
                new PeriodicWorkRequest.Builder(CleanUpWorker.class, 15, TimeUnit.SECONDS) //工作的运行时间间隔定为一小时
                        // Constraints
                        .build();
        WorkManager
                .getInstance(context)
                .enqueue(saveRequest);

    }
}