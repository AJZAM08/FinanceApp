package com.financeapp.core.notification

import android.content.Context
import java.util.Calendar
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import java.util.concurrent.TimeUnit
import androidx.work.WorkManager

object DailyReminderScheduler {
    private const val WORK_NAME = "daily_reminder-produk"
    fun scheduleDailyReminder(context: Context, hour: Int = 20, minute: Int = 0) {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(now)) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        val initialDelay = target.timeInMillis - now.timeInMillis
        val workRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(
            24, TimeUnit.HOURS,
            15, TimeUnit.MINUTES
        ).setInitialDelay(initialDelay, TimeUnit.MILLISECONDS).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    fun cancelDailyReminder(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
}