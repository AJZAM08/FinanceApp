package com.financeapp.presentation.screen.scan

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financeapp.domain.model.ReceiptData
import com.financeapp.domain.model.TransactionCategory
import com.financeapp.domain.usecase.ScanReceiptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class ScanReceiptUiState(
    val imageUri: Uri? = null,
    val isOcrLoading: Boolean = false,
    val isParsingLoading: Boolean = false,
    val receiptData: ReceiptData? = null,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,

    val editedAmount: String = "",
    val editedMerchant: String = "",
    val editedCategory: TransactionCategory = TransactionCategory.OTHER,
    val editedDate: LocalDateTime = LocalDateTime.now()
)

@HiltViewModel
class ScanReceiptViewModel @Inject constructor(
    private val scanReceiptUseCase: ScanReceiptUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScanReceiptUiState())
    val uiState: StateFlow<ScanReceiptUiState> = _uiState.asStateFlow()

    fun onImageSelected(uri: Uri) {
        _uiState.update { it.copy(imageUri = uri, errorMessage = null) }
        processImage(uri)
    }

    private fun processImage(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isOcrLoading = true) }

            val result = scanReceiptUseCase(uri)

            _uiState.update { it.copy(isOcrLoading = false) }

            result.fold(
                onSuccess = { receiptData ->
                    _uiState.update { state ->
                        state.copy(
                            receiptData = receiptData,
                            editedAmount = receiptData.totalAmount?.toLong()?.toString() ?: "",
                            editedMerchant = receiptData.merchantName ?: "",
                            editedCategory = receiptData.category,
                            editedDate = receiptData.date ?: LocalDateTime.now()
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(errorMessage = error.message)
                    }
                }
            )
        }
    }
    // Fungsi edit field — dipanggil dari review screen
    fun onAmountChange(value: String) =
        _uiState.update { it.copy(editedAmount = value) }
    fun onMerchantChange(value: String) =
        _uiState.update { it.copy(editedMerchant = value) }
    fun onCategoryChange(category: TransactionCategory) =
        _uiState.update { it.copy(editedCategory = category) }
    fun onDateChange(date: LocalDateTime) =
        _uiState.update { it.copy(editedDate = date) }
    fun clearError() =
        _uiState.update { it.copy(errorMessage = null) }
    fun reset() =
        _uiState.update { ScanReceiptUiState() }
}