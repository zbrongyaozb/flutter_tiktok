package com.oversketch.flutter_tiktok.work

import android.content.Context
import android.util.Log
import androidx.work.*
import java.util.concurrent.TimeUnit

class DelayWorker3(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.e("开启延时worker DelayWorker", "doWork: " )
        worker();
        return Result.success()
    }
    private fun worker() {
        val saveRequest = PeriodicWorkRequest.Builder(
            CleanUpWorker::class.java,
            15,
            TimeUnit.MINUTES
        ) //工作的运行时间间隔定为一小时
            .build()
        WorkManager
            .getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                "worker3",
                ExistingPeriodicWorkPolicy.REPLACE,
                saveRequest);
    }
}