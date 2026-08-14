package com.financeapp.domain.repository

import com.financeapp.domain.model.ReceiptData

interface ReceiptParserRepository {
    suspend fun parseReceiptText(rawText: String): Result<ReceiptData>
}