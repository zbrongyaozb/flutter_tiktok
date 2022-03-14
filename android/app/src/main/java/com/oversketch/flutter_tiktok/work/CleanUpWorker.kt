package com.oversketch.flutter_tiktok.work

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.oversketch.flutter_tiktok.service.PlayService

class CleanUpWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.e("CleanUpWorker", "doWork: " )
        val bootIntent = Intent(applicationContext, PlayService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e("doWork", "使用JobScheduler进行保活 startForegroundService")
            applicationContext.startForegroundService(bootIntent)
        } else {
            applicationContext.startService(bootIntent)
        }
        return Result.success()
    }
}

