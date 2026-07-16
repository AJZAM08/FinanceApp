package com.financeapp.domain.model

import java.time.LocalDateTime

data class Transaction(
    val id: Long = 0,
    val title: String,
    val amount: Long,
    val type: TransactionType,
    val category: TransactionCategory,
    val paymentMethod: PaymentMethod,
    val note: String = "",
    val date: LocalDateTime,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class TransactionType {
    INCOME,
    EXPENSE
}

enum class TransactionCategory {
    FOOD,
    TRANSPORT,
    SHOPPING,
    HEALTH,
    ENTERTAINMENT,
    EDUCATION,
    BILLS,
    SALARY,
    FREELANCE,
    INVESTMENT,
    GIFT,
    OTHER
}

sealed class PaymentMethod {
    object Cash : PaymentMethod()
    object Debit : PaymentMethod()
    data class Credit(
        val bankName: String,
        val dueDate: LocalDateTime
    ) : PaymentMethod()
    data class EWallet(
        val walletName: String
    ) : PaymentMethod()
}
