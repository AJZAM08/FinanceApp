package com.financeapp.presentation.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAllTransactions: GetAllTransactionsUseCase,
    private val getBalance: GetBalanceUseCase,
    private val getCategoryStatistics: GetCategoryStatisticsUseCase,
    private val deleteTransaction: DeleteTransactionUseCase
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
                getCategoryStatistics(TransactionType.EXPENSE)
            ) { transactions, balance, statistics ->
                DashboardUiState(
                    isLoading = false,
                    balanceInfo = balance,
                    recentTransactions = transactions.take(5),
                    categoryStatistics = statistics,
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