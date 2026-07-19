package com.financeapp.domain.usecase

import com.financeapp.domain.model.Transaction
import com.financeapp.domain.model.TransactionType
import com.financeapp.domain.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Long> {
        return try {
            require(transaction.title.isNotBlank()) {
                "Judul transaksi tidak boleh kosong"
            }
            require(transaction.amount > 0) {
                "Nominal transaksi harus lebih besar dari 0"
            }
            val id = repository.insertTransaction(transaction)
            Result.success(id)
        } catch (e: IllegalStateException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}