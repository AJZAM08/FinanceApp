package com.financeapp.core.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class DailyReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        NotificationHelper.showDailyReminder(context)
        return Result.success()
    }
}