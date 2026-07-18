package com.financeapp.data.repository

import com.financeapp.data.local.dao.TransactionDao
import com.financeapp.data.local.entity.CategoryTotal
import com.financeapp.data.local.entity.toDomain
import com.financeapp.data.local.entity.toEntity
import com.financeapp.domain.model.Transaction
import com.financeapp.domain.model.TransactionCategory
import com.financeapp.domain.model.TransactionType
import com.financeapp.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionRepository {

    // CREATE
    override suspend fun insertTransaction(transaction: Transaction): Long {
        return dao.insertTransaction(transaction.toEntity())
    }

    // READ
    override fun getAllTransactions(): Flow<List<Transaction>> {
        return dao.getAllTransactions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllTransactionsByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<Transaction>> {
        return dao.getAllTransactionsByDateRange(startDate, endDate).map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getTransactionById(id: Long): Transaction? {
        return dao.getTransactionById(id)?.toDomain()
    }

    override fun getTotalIncome(): Flow<Long> {
        return dao.getTotalIncome()
    }

    override fun getTotalExpense(): Flow<Long> {
        return dao.getTotalExpense()
    }

    override fun getTransactionsByCategory(category: TransactionCategory): Flow<List<Transaction>> {
        return dao.getTransactionsByCategory(category.name).map { entities -> entities.map { it.toDomain() } }
    }

    override fun getTotalByCategory(type: TransactionType): Flow<Map<TransactionCategory, Long>> {
        return dao.getTotalByCategory(type.name).map { list -> list.toTransactionCategoryMap() }
    }

    // UPDATE
    override suspend fun updateTransaction(transaction: Transaction) {
        dao.updateTransaction(transaction.toEntity())
    }

    // DELETE
    override suspend fun deleteTransaction(transaction: Transaction) {
        dao.deleteTransaction(transaction.toEntity())
    }

    override suspend fun deleteTransactionById(id: Long) {
        dao.deleteTransactionById(id)
    }
}

private fun List<CategoryTotal>.toTransactionCategoryMap(): Map<TransactionCategory, Long> {
    return associate { categoryTotal -> TransactionCategory.valueOf(categoryTotal.category) to categoryTotal.total }
}