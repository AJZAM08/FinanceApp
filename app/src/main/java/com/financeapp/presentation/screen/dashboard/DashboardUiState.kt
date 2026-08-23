package com.financeapp.presentation.screen.dashboard

import com.financeapp.domain.model.Transaction
import com.financeapp.domain.usecase.BalanceInfo
import com.financeapp.domain.usecase.CategoryStatistics

data class DashboardUiState(
    val isLoading: Boolean = true,
    val balanceInfo: BalanceInfo = BalanceInfo(
        totalIncome = 0L,
        totalExpense = 0L,
        balance = 0L
    ),
    val categoryStatistics: List<CategoryStatistics> = emptyList(),
    val recentTransactions: List<Transaction> = emptyList(),
    val isBalanceHidden: Boolean = false,
    val isReminderEnabled: Boolean = false,
    val reminderHour: Int = 20,
    val reminderMinute: Int = 0,
    val errorMessage: String? = null
)