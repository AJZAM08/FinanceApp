package com.financeapp.presentation.screen.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financeapp.core.datastore.UserPreferences
import com.financeapp.core.notification.DailyReminderScheduler
import com.financeapp.core.notification.NotificationHelper
import com.financeapp.domain.model.TransactionType
import com.financeapp.domain.usecase.GetAllTransactionsUseCase
import com.financeapp.domain.usecase.GetBalanceUseCase
import com.financeapp.domain.usecase.GetCategoryStatisticsUseCase
import com.financeapp.domain.usecase.DeleteTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.financeapp.domain.model.Transaction
import com.financeapp.domain.usecase.BalanceInfo
import com.financeapp.domain.usecase.CategoryStatistics

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAllTransactions: GetAllTransactionsUseCase,
    private val getBalance: GetBalanceUseCase,
    private val getCategoryStatistics: GetCategoryStatisticsUseCase,
    private val deleteTransaction: DeleteTransactionUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    init {
        loadDashboardData()
    }
    private fun loadDashboardData() {
        viewModelScope.launch {
            combine(
                getAllTransactions(),
                getBalance(),
                getCategoryStatistics(TransactionType.EXPENSE),
                userPreferences.isBalanceHidden,
                userPreferences.isReminderEnabled,
                userPreferences.reminderHour,
                userPreferences.reminderMinute
            ) { args: Array<Any> ->
                val transactions = args[0] as List<Transaction>
                val balance = args[1] as BalanceInfo
                val statistics = args[2] as List<CategoryStatistics>
                val isBalanceHidden = args[3] as Boolean
                val isReminderEnabled = args[4] as Boolean
                val reminderHour = args[5] as Int
                val reminderMinute = args[6] as Int
                DashboardUiState(
                    isLoading = false,
                    balanceInfo = balance,
                    recentTransactions = transactions.take(5),
                    categoryStatistics = statistics,
                    isBalanceHidden = isBalanceHidden,
                    isReminderEnabled = isReminderEnabled,
                    reminderHour = reminderHour,
                    reminderMinute = reminderMinute,
                    errorMessage = null
                )
            }.catch { error ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
            }.collect { newState ->
                _uiState.update { newState }
            }
        }
    }

    fun toggleBalanceVisibility() {
        viewModelScope.launch {
            userPreferences.setBalanceHidden(!_uiState.value.isBalanceHidden)
        }
    }
    fun setReminderEnabled(enabled: Boolean, context: Context) {
        viewModelScope.launch {
            userPreferences.setReminderEnabled(enabled)
            if (enabled) {
                val state = _uiState.value
                DailyReminderScheduler.scheduleDailyReminder(
                    context,
                    state.reminderHour,
                    state.reminderMinute
                )
            } else {
                DailyReminderScheduler.cancelDailyReminder(context)
            }
        }
    }
    fun setReminderTime(hour: Int, minute: Int, context: Context) {
        viewModelScope.launch {
            userPreferences.setReminderTime(
                hour,
                minute
            )
            if (_uiState.value.isReminderEnabled) {
                DailyReminderScheduler.scheduleDailyReminder(context, hour, minute)
            }
        }
    }
    fun testNotification(context: Context) {
        NotificationHelper.showDailyReminder(context)
    }
    fun deleteCurrentTransaction(id: Long) {
        viewModelScope.launch {
            val result = deleteTransaction(id)
            result.onFailure { error ->
                _uiState.update { currentState ->
                    currentState.copy(
                        errorMessage = error.message
                    )
                }
            }
        }
    }
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}