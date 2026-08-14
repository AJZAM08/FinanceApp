package com.financeapp.domain.model

import java.time.LocalDateTime

data class ReceiptData(
    val rawText: String,
    val totalAmount: Double?,
    val date: LocalDateTime?,
    val merchantName: String?,
    val category: TransactionCategory = TransactionCategory.OTHER
)