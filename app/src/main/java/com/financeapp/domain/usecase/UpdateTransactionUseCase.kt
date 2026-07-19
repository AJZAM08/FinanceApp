package com.financeapp.domain.usecase

import com.financeapp.domain.model.Transaction
import com.financeapp.domain.repository.TransactionRepository
import javax.inject.Inject

class UpdateTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Unit> {
        return try {
            require(transaction.title.isNotBlank()) {
                "Judul transaksi tidak boleh kosong"
            }
            require(transaction.amount > 0) {
                "Nominal transaksi harus lebih besar dari 0"
            }
            repository.updateTransaction(transaction)
            Result.success(Unit)
        } catch (e: IllegalStateException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}