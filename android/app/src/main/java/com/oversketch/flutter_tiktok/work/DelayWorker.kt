package com.oversketch.flutter_tiktok.work

import android.content.Context
import android.util.Log
import androidx.work.*
import java.util.concurrent.TimeUnit

class DelayWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.e("开启延时worker DelayWorker", "doWork: " )
        worker();
        return Result.success()
    }
    private fun worker() {
        val saveRequest = PeriodicWorkRequest.Builder(
            CleanUpWorker::class.java,
            1,
            TimeUnit.SECONDS
        ) //工作的运行时间间隔定为一小时
            // Constraints
            .build()
        val uploadWorkRequest: WorkRequest = OneTimeWorkRequest.from(CleanUpWorker::class.java)
        val myWorkRequest: WorkRequest = OneTimeWorkRequest.Builder(CleanUpWorker::class.java)
            .setInitialDelay(30, TimeUnit.SECONDS)
            .build()
        WorkManager
            .getInstance(applicationContext)
            .enqueue(myWorkRequest)
    }
}