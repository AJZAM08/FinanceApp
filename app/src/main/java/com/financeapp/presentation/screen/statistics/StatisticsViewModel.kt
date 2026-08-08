package com.financeapp.presentation.screen.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financeapp.domain.model.Transaction
import com.financeapp.domain.model.TransactionCategory
import com.financeapp.domain.model.TransactionType
import com.financeapp.domain.usecase.GetAllTransactionsUseCase
import com.financeapp.domain.usecase.CategoryStatistics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

enum class StatsPeriod {
    THIS_MONTH, LAST_MONTH, ALL_TIME
}

data class StatisticsUiState(
    val categoryStats: List<CategoryStatistics> = emptyList(),
    val monthlyTrends: Map<String, Long> = emptyMap(),
    val selectedPeriod: StatsPeriod = StatsPeriod.THIS_MONTH,
    val selectedType: TransactionType = TransactionType.EXPENSE,
    val totalAmount: Long = 0L,
    val comparisonText: String = "",
    val isLoading: Boolean = true
)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getAllTransactions: GetAllTransactionsUseCase
) : ViewModel() {

    private val _selectedPeriod = MutableStateFlow(StatsPeriod.THIS_MONTH)
    private val _selectedType = MutableStateFlow(TransactionType.EXPENSE)
    private val _isLoading = MutableStateFlow(true)

    val uiState: StateFlow<StatisticsUiState> = combine(
        getAllTransactions(),
        _selectedPeriod,
        _selectedType,
        _isLoading
    ) { transactions, period, type, loading ->
        val now = LocalDateTime.now()
        val startOfThisMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
        val startOfLastMonth = startOfThisMonth.minusMonths(1)
        val endOfLastMonth = startOfThisMonth.minusNanos(1)

        // 1. Filter transaksi berdasarkan periode dan tipe
        val filteredForStats = transactions.filter { t ->
            t.type == type && when (period) {
                StatsPeriod.THIS_MONTH -> t.date.isAfter(startOfThisMonth.minusNanos(1))
                StatsPeriod.LAST_MONTH -> t.date.isAfter(startOfLastMonth.minusNanos(1)) && t.date.isBefore(endOfLastMonth.plusNanos(1))
                StatsPeriod.ALL_TIME -> true
            }
        }

        // 2. Hitung statistik kategori
        val grandTotal = filteredForStats.sumOf { it.amount }
        val categoryStats = filteredForStats
            .groupBy { it.category }
            .map { (cat, list) ->
                val total = list.sumOf { it.amount }
                CategoryStatistics(
                    category = cat,
                    total = total,
                    percentage = if (grandTotal > 0) (total.toFloat() / grandTotal) * 100f else 0f
                )
            }.sortedByDescending { it.total }

        // 3. Hitung teks perbandingan (Bulan Ini vs Bulan Lalu)
        val thisMonthSum = transactions.filter { t -> t.type == type && t.date.isAfter(startOfThisMonth.minusNanos(1)) }.sumOf { it.amount }
        val lastMonthSum = transactions.filter { t -> t.type == type && t.date.isAfter(startOfLastMonth.minusNanos(1)) && t.date.isBefore(endOfLastMonth.plusNanos(1)) }.sumOf { it.amount }
        
        val comparisonText = when {
            lastMonthSum == 0L -> "Belum ada data pembanding untuk bulan lalu"
            thisMonthSum > lastMonthSum -> {
                val diff = thisMonthSum - lastMonthSum
                val pct = (diff.toFloat() / lastMonthSum) * 100f
                "Pengeluaran naik ${String.format("%.1f", pct)}% (Rp ${String.format("%,.0f", diff.toDouble())}) dibanding bulan lalu"
            }
            thisMonthSum < lastMonthSum -> {
                val diff = lastMonthSum - thisMonthSum
                val pct = (diff.toFloat() / lastMonthSum) * 100f
                "Kamu lebih hemat ${String.format("%.1f", pct)}% (Rp ${String.format("%,.0f", diff.toDouble())}) dibanding bulan lalu!"
            }
            else -> "Pengeluaran sama persis dengan bulan lalu"
        }

        // 4. Hitung tren bulanan (4 bulan terakhir)
        val formatter = DateTimeFormatter.ofPattern("MMM")
        val monthlyTrends = mutableMapOf<String, Long>()
        for (i in 3 downTo 0) {
            val monthDate = now.minusMonths(i.toLong())
            val start = monthDate.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
            val end = start.plusMonths(1).minusNanos(1)
            val monthLabel = monthDate.format(formatter)
            val monthSum = transactions.filter { t -> t.type == type && t.date.isAfter(start.minusNanos(1)) && t.date.isBefore(end.plusNanos(1)) }.sumOf { it.amount }
            monthlyTrends[monthLabel] = monthSum
        }

        StatisticsUiState(
            categoryStats = categoryStats,
            monthlyTrends = monthlyTrends,
            selectedPeriod = period,
            selectedType = type,
            totalAmount = grandTotal,
            comparisonText = comparisonText,
            isLoading = loading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StatisticsUiState()
    )

    init {
        viewModelScope.launch {
            kotlinx.coroutines.delay(800) // Efek loading shimmer
            _isLoading.value = false
        }
    }

    fun onPeriodChange(period: StatsPeriod) {
        _selectedPeriod.value = period
    }

    fun onTypeChange(type: TransactionType) {
        _selectedType.value = type
    }
}
