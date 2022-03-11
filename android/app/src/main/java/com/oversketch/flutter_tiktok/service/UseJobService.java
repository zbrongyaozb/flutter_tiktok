package com.oversketch.flutter_tiktok.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class UseJobService extends Service {

    private static final String CHANNEL_ID = "com.example.keep.alive";
    private static final String CHANNEL_NAME = "Keep Alive Service";
    public static final int NOTICE_ID = 100;
    private static final String TAG = UseJobService.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        useJobServiceForKeepAlive();
        startForeground();
    }
    private void startForeground() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager == null) {
                Log.e(TAG, "getSystemService for NotificationManager failed.");
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 使用JobScheduler进行保活
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void useJobServiceForKeepAlive() {
        Log.e(TAG, "使用JobScheduler进行保活");

        Intent bootIntent = new Intent(getApplicationContext(), PlayService.class);
//            bootIntent.addFlags()
//            context.startService(bootIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e(TAG, "使用JobScheduler进行保活 startForegroundService");
            getApplicationContext().startForegroundService(bootIntent);
        } else {
            getApplicationContext().startService(bootIntent);
        }
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler == null) {
            return;
        }
        jobScheduler.cancelAll();
        JobInfo.Builder builder =
                new JobInfo.Builder(1024, new ComponentName(getPackageName(),
                        JobSchedulerService.class.getName()));
        //周期设置为了2s
        builder.setPeriodic(5000 * 2);
        builder.setPersisted(true);
//        builder.setBackoffCriteria(10, JobInfo.BACKOFF_POLICY_LINEAR);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        int schedule = jobScheduler.schedule(builder.build());

        Log.e(TAG, "使用JobScheduler进行保活 schedule"+schedule);
        if (schedule <= 0) {
            Log.e(TAG, "schedule error！");
        }
    }
}