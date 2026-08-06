package com.financeapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.financeapp.data.local.dao.TransactionDao
import com.financeapp.data.local.entity.TransactionEntity

@Database(
    entities = [TransactionEntity::class],
    version = 1,
    exportSchema = true,
)

@TypeConverters(DateConverter::class)
abstract class FinanceDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    companion object {
        const val DATABASE_NAME = "finance_database"
    }
}