package com.oversketch.flutter_tiktok.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

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
            Intent bootIntent = new Intent(context, UseJobService.class);
//            Intent bootIntent = new Intent(context, PlayService.class);
//            bootIntent.addFlags()
//            context.startService(bootIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(bootIntent);
            } else {
                context.startService(bootIntent);
            }
        }
    }
}