package com.financeapp.di

import com.financeapp.data.remote.GeminiApiService
import com.financeapp.data.service.GeminiReceiptParser
import com.financeapp.data.service.MlKitOcrService
import com.financeapp.domain.repository.OcrRepository
import com.financeapp.domain.repository.ReceiptParserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OcrModule {

    @Binds @Singleton
    abstract fun bindOcrRepository(impl: MlKitOcrService): OcrRepository

    @Binds @Singleton
    abstract fun bindReceiptParserRepository(impl: GeminiReceiptParser): ReceiptParserRepository

    companion object {
        @Provides @Singleton
        fun provideGeminiApiService(): GeminiApiService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl("https://generativelanguage.googleapis.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GeminiApiService::class.java)
        }
    }
}