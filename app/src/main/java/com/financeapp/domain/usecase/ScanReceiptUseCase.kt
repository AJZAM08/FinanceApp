package com.financeapp.domain.usecase

import android.net.Uri
import com.financeapp.domain.model.ReceiptData
import com.financeapp.domain.repository.OcrRepository
import com.financeapp.domain.repository.ReceiptParserRepository
import javax.inject.Inject

class ScanReceiptUseCase @Inject constructor(
    private val ocrRepository: OcrRepository,
    private val receiptParserRepository: ReceiptParserRepository
) {
    suspend operator fun invoke(imageUri: Uri): Result<ReceiptData> {
        // Read Text From Image
        val ocrResult = ocrRepository.extractTextFromImage(imageUri)
        if (ocrResult.isFailure) {
            return Result.failure(
                ocrResult.exceptionOrNull()?: Exception("Gagal membaca teks dari gambar")
            )
        }
        val rawText = ocrResult.getOrThrow()
        if (rawText.isBlank()) {
            return Result.failure(
                Exception("Tidak ada teks terdeteksi, Pastikan foto struk jelas dan cukup cahaya")
            )
        }
        return receiptParserRepository.parseReceiptText(rawText)
    }
}