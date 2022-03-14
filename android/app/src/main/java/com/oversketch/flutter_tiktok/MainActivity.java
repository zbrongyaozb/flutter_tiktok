package com.oversketch.flutter_tiktok;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.oversketch.flutter_tiktok.service.PlayService;
import com.oversketch.flutter_tiktok.service.UseJobService;
import com.oversketch.flutter_tiktok.work.CleanUpWorker;
import com.oversketch.flutter_tiktok.work.DelayWorker;

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
        PeriodicWorkRequest saveRequest =
                new PeriodicWorkRequest.Builder(CleanUpWorker.class, 15, TimeUnit.SECONDS) //工作的运行时间间隔定为一小时
                        // Constraints
                        .build();
        WorkRequest uploadWorkRequest = OneTimeWorkRequest.from(CleanUpWorker.class);

        WorkRequest myWorkRequest =
                        new OneTimeWorkRequest.Builder(DelayWorker.class)
                         .setInitialDelay(30, TimeUnit.SECONDS)
                         .build();

        WorkManager
                .getInstance(this)
                .enqueue(myWorkRequest);

    }
    private void useJobService() {
        System.out.println("useJobService");
        Intent intentJob = new Intent(this, UseJobService.class);
        startService(intentJob);
    }
}
