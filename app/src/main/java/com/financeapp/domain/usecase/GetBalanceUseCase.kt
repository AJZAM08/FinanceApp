package com.financeapp.domain.usecase

import com.financeapp.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class BalanceInfo(
    val totalIncome: Long,
    val totalExpense: Long,
    val balance: Long
)

class GetBalanceUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<BalanceInfo> {
        return combine(
            repository.getTotalIncome(),
            repository.getTotalExpense()
        ) { income: Long, expense: Long ->
            BalanceInfo(
                totalIncome = income,
                totalExpense = expense,
                balance = income - expense
            )
        }
    }
}