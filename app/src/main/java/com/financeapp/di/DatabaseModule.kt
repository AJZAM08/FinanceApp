package com.financeapp.di

import android.content.Context
import androidx.room.Room
import com.financeapp.core.security.DatabasePassphrase
import com.financeapp.data.local.dao.TransactionDao
import com.financeapp.data.local.database.FinanceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideFinanceDatabase(
        @ApplicationContext context: Context,
        databasePassphrase: DatabasePassphrase,
    ): FinanceDatabase {
        System.loadLibrary("sqlcipher")
        val passphrase = databasePassphrase.getOrCreatePassphrase()
        val factory = SupportOpenHelperFactory(passphrase)

        return Room.databaseBuilder(
            context,
            FinanceDatabase::class.java,
            FinanceDatabase.DATABASE_NAME,
        )
            .openHelperFactory(factory)
            .build()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(
        database: FinanceDatabase,
    ): TransactionDao {
        return database.transactionDao()
    }
}