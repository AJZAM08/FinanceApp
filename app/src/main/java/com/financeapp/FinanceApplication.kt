package com.financeapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp // generate kode dependency injection
class FinanceApplication : Application() { // ini custom application class
    override fun onCreate() {
        super.onCreate()
    }
}