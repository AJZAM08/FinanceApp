package com.financeapp.domain.repository

import android.net.Uri

interface OcrRepository {
    suspend fun extractTextFromImage(imageUri: Uri): Result<String>
}