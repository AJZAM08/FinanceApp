package com.financeapp.di

import android.content.Context
import androidx.room.Room
import com.financeapp.data.local.dao.TransactionDao
import com.financeapp.data.local.database.FinanceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideFinanceDatabase(
        @ApplicationContext context: Context
    ): FinanceDatabase{
        return Room.databaseBuilder<FinanceDatabase>(
            context = context,
            name = FinanceDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(
        database: FinanceDatabase
    ): TransactionDao {
        return database.transactionDao()
    }
}