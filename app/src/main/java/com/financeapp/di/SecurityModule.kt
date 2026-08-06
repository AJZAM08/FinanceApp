package com.financeapp.di

import android.content.Context
import com.financeapp.core.security.DatabasePassphrase
import com.financeapp.core.security.RootDetection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {
    @Provides
    @Singleton
    fun provideRootDetection(
        @ApplicationContext context: Context
    ): RootDetection {
        return RootDetection(context)
    }

    @Provides
    @Singleton
    fun provideDatabasePassphrase(
        @ApplicationContext context: Context
    ): DatabasePassphrase {
        return DatabasePassphrase(context)
    }
}