package com.financeapp.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val IS_ONBOARDING_COMPLETED =
            booleanPreferencesKey("is_onboarding_completed")
        val IS_BALANCE_HIDDEN =
            booleanPreferencesKey("is_balance_hidden")
        val IS_REMINDER_ENABLED = booleanPreferencesKey("is_reminder_enabled")
        val REMINDER_HOUR = intPreferencesKey("reminder_hour")
        val REMINDER_MINUTE = intPreferencesKey("reminder_minute")
    }

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[Keys.IS_ONBOARDING_COMPLETED] ?: false
    }

    val isBalanceHidden: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[Keys.IS_BALANCE_HIDDEN] ?: false
    }

    suspend fun setOnBoardingCompleted() {
        context.dataStore.edit { preferences ->
            preferences[Keys.IS_ONBOARDING_COMPLETED] = true
        }
    }

    suspend fun setBalanceHidden(hidden: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.IS_BALANCE_HIDDEN] = hidden
        }
    }

    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    val isReminderEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[Keys.IS_REMINDER_ENABLED] ?: false
    }

    val reminderHour: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[Keys.REMINDER_HOUR] ?: 20
    }

    val reminderMinute: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[Keys.REMINDER_MINUTE] ?: 0
    }

    suspend fun setReminderEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.IS_REMINDER_ENABLED] = enabled
        }
    }

    suspend fun setReminderTime(hour: Int, minute: Int) {
        context.dataStore.edit { preferences ->
            preferences[Keys.REMINDER_HOUR] = hour
            preferences[Keys.REMINDER_MINUTE] = minute
        }
    }
}