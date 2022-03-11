package com.oversketch.flutter_tiktok;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;

import androidx.annotation.NonNull;

import com.oversketch.flutter_tiktok.service.PlayService;
import com.oversketch.flutter_tiktok.service.UseJobService;

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
                        useJobService();
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
    private void useJobService() {
        System.out.println("useJobService");
        Intent intentJob = new Intent(this, UseJobService.class);
        startService(intentJob);
    }
}
