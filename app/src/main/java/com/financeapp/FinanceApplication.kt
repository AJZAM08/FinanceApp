package com.financeapp

import android.app.Application
import com.financeapp.core.notification.NotificationHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FinanceApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this)
    }
}
