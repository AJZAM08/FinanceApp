package com.financeapp.domain.usecase

import com.financeapp.domain.model.TransactionCategory
import com.financeapp.domain.model.TransactionType
import com.financeapp.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class CategoryStatistics(
    val category: TransactionCategory,
    val total: Long,
    val percentage: Float
)

class GetCategoryStatisticsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(type: TransactionType): Flow<List<CategoryStatistics>> {
        return repository.getTotalByCategory(type).map { categoryMap ->
            val grandTotal = categoryMap.values.sum()
            if (grandTotal == 0L) return@map emptyList()

            categoryMap.map { (category, total) -> CategoryStatistics(
                category = category,
                total = total,
                percentage = (total.toFloat() / grandTotal) * 100f)
            }.sortedByDescending { it.total }
        }
    }
}