package com.financeapp.presentation.screen.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financeapp.domain.model.Transaction
import com.financeapp.domain.model.TransactionCategory
import com.financeapp.domain.model.TransactionType
import com.financeapp.domain.usecase.GetAllTransactionsUseCase
import com.financeapp.domain.usecase.DeleteTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZoneId
import javax.inject.Inject

data class TransactionListUiState(
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val selectedCategory: TransactionCategory? = null,
    val selectedType: TransactionType? = null,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val getAllTransactions: GetAllTransactionsUseCase,
    private val deleteTransaction: DeleteTransactionUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow<TransactionCategory?>(null)
    private val _selectedType = MutableStateFlow<TransactionType?>(null)
    private val _dateRange = MutableStateFlow<Pair<Long?, Long?>>(Pair(null, null))
    private val _isLoading = MutableStateFlow(true)
    private val _errorMessage = MutableStateFlow<String?>(null)

    @Suppress("UNCHECKED_CAST")
    val uiState: StateFlow<TransactionListUiState> = combine(
        getAllTransactions(),
        _searchQuery,
        _selectedCategory,
        _selectedType,
        _dateRange,
        _isLoading,
        _errorMessage
    ) { args: Array<Any?> ->
        val transactions = args[0] as List<Transaction>
        val query = args[1] as String
        val category = args[2] as TransactionCategory?
        val type = args[3] as TransactionType?
        val dateRange = args[4] as Pair<Long?, Long?>
        val loading = args[5] as Boolean
        val error = args[6] as String?

        val filteredList = transactions.filter { transaction ->
            val matchesQuery = query.isBlank() || transaction.title.contains(query, ignoreCase = true)
            val matchesCategory = category == null || transaction.category == category
            val matchesType = type == null || transaction.type == type
            
            val transactionMillis = transaction.date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val matchesStartDate = dateRange.first == null || transactionMillis >= dateRange.first!!
            val matchesEndDate = dateRange.second == null || transactionMillis <= (dateRange.second!! + 86400000L - 1L)

            matchesQuery && matchesCategory && matchesType && matchesStartDate && matchesEndDate
        }
        
        TransactionListUiState(
            transactions = filteredList,
            isLoading = loading,
            searchQuery = query,
            selectedCategory = category,
            selectedType = type,
            startDate = dateRange.first,
            endDate = dateRange.second,
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TransactionListUiState()
    )

    init {
        viewModelScope.launch {
            kotlinx.coroutines.delay(800) // Simulasikan loading agar efek shimmer terlihat
            _isLoading.value = false
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelect(category: TransactionCategory?) {
        _selectedCategory.value = category
    }

    fun onTypeSelect(type: TransactionType?) {
        _selectedType.value = type
    }

    fun onDateRangeSelect(start: Long?, end: Long?) {
        _dateRange.value = Pair(start, end)
    }

    fun deleteCurrentTransaction(id: Long) {
        viewModelScope.launch {
            val result = deleteTransaction(id)
            result.onFailure { error ->
                _errorMessage.value = error.message
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun resetFilters() {
        _searchQuery.value = ""
        _selectedCategory.value = null
        _selectedType.value = null
        _dateRange.value = Pair(null, null)
    }
}
