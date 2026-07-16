package com.financeapp.data.local.entity

import com.financeapp.domain.model.Transaction
import com.financeapp.domain.model.PaymentMethod
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        title = title,
        amount = amount,
        type = type,
        category = category,
        paymentMethod = when (paymentMethod) {
            "CREDIT" -> PaymentMethod.Credit(
                bankName = bankName ?: "",
                dueDate = dueDate?.toLocalDateTime() ?: LocalDateTime.now()
            )
            "E_WALLET" -> PaymentMethod.EWallet(
                walletName = walletName ?: ""
            )
            "DEBIT" -> PaymentMethod.Debit
            else -> PaymentMethod.Cash
        },
        note = note,
        date = date.toLocalDateTime(),
        createdAt = createdAt.toLocalDateTime()
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        title = title,
        amount = amount,
        type = type,
        category = category,
        paymentMethod = when (paymentMethod) {
            is PaymentMethod.Cash -> "CASH"
            is PaymentMethod.Debit -> "DEBIT"
            is PaymentMethod.Credit -> "CREDIT"
            is PaymentMethod.EWallet -> "E_WALLET"
        },
        bankName = (paymentMethod as? PaymentMethod.Credit)?.bankName,
        dueDate = (paymentMethod as? PaymentMethod.Credit)
            ?.dueDate?.toTimestamp(),
        walletName = (paymentMethod as? PaymentMethod.EWallet)?.walletName,
        note = note,
        date = date.toTimestamp(),
        createdAt = createdAt.toTimestamp()
    )
}

private fun Long.toLocalDateTime(): LocalDateTime = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
private fun LocalDateTime.toTimestamp(): Long = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()