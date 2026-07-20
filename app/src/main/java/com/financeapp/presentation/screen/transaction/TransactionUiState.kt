package com.financeapp.presentation.screen.transaction

import com.financeapp.domain.model.PaymentMethod
import com.financeapp.domain.model.TransactionCategory
import com.financeapp.domain.model.TransactionType
import java.time.LocalDateTime

data class TransactionUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,

    val title: String = "",
    val amount: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val category: TransactionCategory = TransactionCategory.OTHER,
    val paymentMethod: PaymentMethod = PaymentMethod.Cash,
    val note: String = "",
    val date: LocalDateTime = LocalDateTime.now(),

    val bankName: String = "",
    val dueDate: LocalDateTime = LocalDateTime.now().plusMonths(1),
    val walletName: String = "",

    val titleError: String? = null,
    val amountError: String? = null,
    val errorMessage: String? = null
)