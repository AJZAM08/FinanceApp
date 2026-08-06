package com.financeapp.core.security

import android.content.Context
import com.scottyab.rootbeer.RootBeer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RootDetection @Inject constructor(
    private val context: Context
) {
    fun isDeviceRooted(): Boolean {
        val rootBeer = RootBeer(context)
        return rootBeer.isRooted
    }
}