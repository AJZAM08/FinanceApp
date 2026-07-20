package com.financeapp.presentation.screen.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financeapp.domain.model.PaymentMethod
import com.financeapp.domain.model.Transaction
import com.financeapp.domain.model.TransactionCategory
import com.financeapp.domain.model.TransactionType
import com.financeapp.domain.usecase.AddTransactionUseCase
import com.financeapp.domain.usecase.GetTransactionByIdUseCase
import com.financeapp.domain.usecase.UpdateTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val addTransaction: AddTransactionUseCase,
    private val getTransactionById: GetTransactionByIdUseCase,
    private val updateTransaction: UpdateTransactionUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    fun loadTransaction(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val transaction = getTransactionById(id)
            transaction?.let { t ->
                _uiState.update {
                    it.copy(
                        title = t.title,
                        amount = t.amount.toString(),
                        type = t.type,
                        category = t.category,
                        paymentMethod = t.paymentMethod,
                        note = t.note,
                        date = t.date
                    )
                }
            }
        }
    }

    fun onTitleChange(value: String) {
        _uiState.update { it.copy(title = value, titleError = null) }
    }

    fun onAmountChange(value: String) {
        _uiState.update { it.copy(amount = value, amountError = null) }
    }

    fun onTypeChange(value: TransactionType) {
        _uiState.update { it.copy(type = value) }
    }

    fun onCategoryChange(value: TransactionCategory) {
        _uiState.update { it.copy(category = value) }
    }

    fun onPaymentMethodChange(value: PaymentMethod) {
        _uiState.update { it.copy(paymentMethod = value) }
    }

    fun onNoteChange(value: String) {
        _uiState.update { it.copy(note = value) }
    }

    fun onDateChange(value: LocalDateTime) {
        _uiState.update { it.copy(date = value) }
    }

    fun onBankNameChange(value: String) {
        _uiState.update { it.copy(bankName = value) }
    }

    fun onWalletNameChange(value: String) {
        _uiState.update { it.copy(walletName = value) }
    }

    fun onDueDateChange(value: LocalDateTime) {
        _uiState.update { it.copy(dueDate = value) }
    }

    fun saveTransaction(existingId: Long? = null) {
        if (!validateForm()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val state = _uiState.value
            val paymentMethod = when (state.paymentMethod) {
                is PaymentMethod.Credit -> PaymentMethod.Credit(
                    bankName = state.bankName,
                    dueDate = state.dueDate
                )
                is PaymentMethod.EWallet -> PaymentMethod.EWallet(
                    walletName = state.walletName
                )
                else -> state.paymentMethod
            }
            val transaction = Transaction(
                id = existingId ?: 0,
                title = state.title,
                amount = state.amount.toLong(),
                type = state.type,
                category = state.category,
                paymentMethod = paymentMethod,
                note = state.note,
                date = state.date
            )
            val result = if (existingId != null) {
                updateTransaction(transaction)
            } else {
                addTransaction(transaction)
            }
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    private fun validateForm(): Boolean {
        val state = _uiState.value
        var isValid = true
        if (state.title.isBlank()) {
            _uiState.update { it.copy(titleError = "Judul tidak boleh kosong") }
            isValid = false
        }
        if (state.amount.isBlank()) {
            _uiState.update { it.copy(amountError = "Nominal tidak valid") }
            isValid = false
        }
        else if (state.amount.toLong() <= 0) {
            _uiState.update { it.copy(amountError = "Nominal harus lebih dari 0") }
            isValid = false
        }
        return isValid
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null, isSuccess = false) }
    }
}