package com.oversketch.flutter_tiktok;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.oversketch.flutter_tiktok.service.PlayService;
import com.oversketch.flutter_tiktok.service.UseJobService;
import com.oversketch.flutter_tiktok.work.CleanUpWorker;
import com.oversketch.flutter_tiktok.work.DelayWorker;
import com.oversketch.flutter_tiktok.work.DelayWorker2;
import com.oversketch.flutter_tiktok.work.DelayWorker3;
import com.oversketch.flutter_tiktok.work.DelayWorker4;

import java.util.concurrent.TimeUnit;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {
    private static final String BATTERY_CHANNEL = "samples.flutter.io/battery";
    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor(), BATTERY_CHANNEL).setMethodCallHandler(
                new MethodChannel.MethodCallHandler() {
                    @Override
                    public void onMethodCall(MethodCall call, MethodChannel.Result result) {
//                        lock();
//                        useJobService();
                        worker();
                        result.success(1);
                    }
                }
        );
    }

    private void lock() {
        System.out.println("lock");
        Intent intent = new Intent(this,  PlayService.class);
        startService(intent);

    }
    private void worker(){
        WorkRequest delayWorker1 =
                        new OneTimeWorkRequest.Builder(DelayWorker.class)
                         .setInitialDelay(2, TimeUnit.MINUTES)
                         .build();
        WorkRequest delayWorker2 =
                new OneTimeWorkRequest.Builder(DelayWorker2.class)
                        .setInitialDelay(4, TimeUnit.MINUTES)
                        .build();
        WorkRequest delayWorker3 =
                new OneTimeWorkRequest.Builder(DelayWorker3.class)
                        .setInitialDelay(6, TimeUnit.MINUTES)
                        .build();
        WorkRequest delayWorker4 =
                new OneTimeWorkRequest.Builder(DelayWorker4.class)
                        .setInitialDelay(8, TimeUnit.MINUTES)
                        .build();
        WorkManager
                .getInstance(this)
                .enqueue(delayWorker1);
        WorkManager
                .getInstance(this)
                .enqueue(delayWorker2);
        WorkManager
                .getInstance(this)
                .enqueue(delayWorker3);
        WorkManager
                .getInstance(this)
                .enqueue(delayWorker4);

        PeriodicWorkRequest saveRequest =
                new PeriodicWorkRequest.Builder(CleanUpWorker.class, 15, TimeUnit.SECONDS) //工作的运行时间间隔定为一小时
                        // Constraints
                        .build();
        WorkManager
                .getInstance(this)
                .enqueue(saveRequest);

    }
    private void useJobService() {
        System.out.println("useJobService");
        Intent intentJob = new Intent(this, UseJobService.class);
        startService(intentJob);
    }
}
