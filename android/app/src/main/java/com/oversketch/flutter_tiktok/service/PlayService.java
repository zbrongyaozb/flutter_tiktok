package com.oversketch.flutter_tiktok.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.oversketch.flutter_tiktok.LockActivity;
import com.oversketch.flutter_tiktok.LockscreenActivity;

public class PlayService extends Service {
    ScreenBroadcastReceiver screenBroadcastReceiver;
    private static final String CHANNEL_ID = "com.example.keep.alive";
    private static final String CHANNEL_NAME = "Keep Alive Service";
    private static final String TAG = "keep-alive";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        screenBroadcastReceiver = new ScreenBroadcastReceiver();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenBroadcastReceiver, filter);
        Log.e(TAG, "doWork PlayService onCreate");
        startForeground();

    }
    private void startForeground() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager == null) {
                Log.e(TAG, "doWork getSystemService for NotificationManager failed.");
                return;
            }

            manager.createNotificationChannel(notificationChannel);
            NotificationCompat.Builder builder =new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("服务运行于前台")
                    .setContentText("service被设为前台进程")
                    .setTicker("service正在后台运行...")
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(NotificationCompat.DEFAULT_ALL);
            Notification notification = builder.build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            startForeground(1,notification );

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopForeground(true);
                }
            }, 500);//3秒后执行Runnable中的run方法

        }

    }

    public class ScreenBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("");
            Log.e(TAG, "doWork ScreenBroadcastReceiver onReceive");
            handleCommandIntent(intent);
        }
    }

    private void handleCommandIntent(Intent intent) {
        final String action = intent.getAction();
        Log.e(TAG, "doWork handleCommandIntent onCreate");

        if (Intent.ACTION_SCREEN_OFF.equals(action) ){
            Intent lockScreen = new Intent(this, LockActivity.class);
            lockScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.e(TAG, "doWork LockscreenActivity onCreate");
            startActivity(lockScreen);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenBroadcastReceiver);
    }
}
