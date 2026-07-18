package com.financeapp.domain.repository

import com.financeapp.domain.model.Transaction
import com.financeapp.domain.model.TransactionCategory
import com.financeapp.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    // CREATE
    suspend fun insertTransaction(transaction: Transaction): Long

    // READ
    fun getAllTransactions(): Flow<List<Transaction>>

    suspend fun getTransactionById(id: Long): Transaction?

    fun getAllTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>>

    fun getTotalIncome(): Flow<Long>

    fun getTotalExpense(): Flow<Long>

    fun getTransactionsByCategory(category: TransactionCategory): Flow<List<Transaction>>

    fun getTotalByCategory(type: TransactionType): Flow<Map<TransactionCategory, Long>>

    // UPDATE
    suspend fun updateTransaction(transaction: Transaction)

    //DELETE
    suspend fun deleteTransaction(transaction: Transaction)

    suspend fun deleteTransactionById(id: Long)
}