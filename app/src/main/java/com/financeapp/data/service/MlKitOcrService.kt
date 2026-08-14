package com.financeapp.data.service

import android.content.Context
import android.net.Uri
import com.financeapp.domain.repository.OcrRepository
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class MlKitOcrService @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : OcrRepository {
    override suspend fun extractTextFromImage(imageUri: Uri): Result<String> {
        return try {
            val image = InputImage.fromFilePath(context, imageUri)
            val recognizer = TextRecognition.getClient(
                TextRecognizerOptions.DEFAULT_OPTIONS
            )
            val rawText = suspendCancellableCoroutine<String?> { continuation ->
                recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        continuation.resume(visionText.text)
                    }
                    .addOnFailureListener { _ ->
                        continuation.resume(null)
                    }
            }
            if (rawText != null) {
                Result.success(rawText)
            } else {
                Result.failure(Exception("ML Kit gagal memproses gambar"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Gagal membaca gambar: ${e.message}"))
        }
    }
}