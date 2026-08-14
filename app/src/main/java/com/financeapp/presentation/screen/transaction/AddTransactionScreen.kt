package com.financeapp.presentation.screen.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.financeapp.domain.model.PaymentMethod
import com.financeapp.domain.model.TransactionCategory
import com.financeapp.domain.model.TransactionType
import com.financeapp.presentation.component.CategoryPickerBottomSheet
import com.financeapp.presentation.component.PaymentMethodPicker
import com.financeapp.presentation.component.toDisplay
import com.financeapp.presentation.theme.Indigo500
import com.financeapp.presentation.theme.Indigo700
import com.financeapp.presentation.theme.Lavender50
import com.financeapp.presentation.theme.TextOnDark
import com.financeapp.presentation.theme.TextSecondary
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onNavigateBack: () -> Unit,
    transactionId: Long? = null,
    prefillAmount: String = "",
    prefillMerchant: String = "",
    prefillCategory: TransactionCategory? = null,
    viewModel: TransactionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showCategoryPicker by remember { mutableStateOf(value = false) }
    var showDatePicker by remember { mutableStateOf(value = false) }

    LaunchedEffect(transactionId) {
        transactionId?.let { viewModel.loadTransaction(it) }
    }

    // Terapkan data pre-fill dari hasil scan struk
    LaunchedEffect(prefillAmount, prefillMerchant, prefillCategory) {
        if (prefillAmount.isNotBlank()) viewModel.onAmountChange(prefillAmount)
        if (prefillMerchant.isNotBlank()) viewModel.onTitleChange(prefillMerchant)
        prefillCategory?.let { viewModel.onCategoryChange(it) }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateBack()
            viewModel.clearError()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.date
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val newDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime()
                        viewModel.onDateChange(newDate)
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Category Bottom Sheet
    if (showCategoryPicker) {
        CategoryPickerBottomSheet(
            selectedCategory = uiState.category,
            transactionType = uiState.type,
            onCategorySelected = viewModel::onCategoryChange
        ) {
            showCategoryPicker = false
        }
    }
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Indigo700, Indigo500)
                )
            )
    )

    Column(
        Modifier
            .fillMaxSize()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .padding(top = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = TextOnDark
                )
            }
            Text(
                text = if (transactionId != null) "Edit Transaksi" else "Tambah Transaksi",
                style = MaterialTheme.typography.titleLarge,
                color = TextOnDark,
                fontWeight =  FontWeight.Bold
            )
        }

        Card(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(Modifier.height(8.dp))

                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    TransactionType.entries.forEach { type ->
                        val isSelected = uiState.type == type
                        val label = if (type == TransactionType.INCOME) "Pemasukan" else "Pengeluaran"

                        Box(
                            Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) Indigo500 else Color.Transparent
                                )
                                .clickable {viewModel.onTypeChange(type)}
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                label,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) TextOnDark else TextSecondary
                            )
                        }
                    }
                }

                // ── Judul ───────────────────────────────────
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = viewModel::onTitleChange,
                    label = { Text("Judul") },
                    placeholder = { Text("Contoh: Makan siang") },
                    isError = uiState.titleError != null,
                    supportingText = uiState.titleError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // ── Nominal ─────────────────────────────────
                OutlinedTextField(
                    value = uiState.amount,
                    onValueChange = viewModel::onAmountChange,
                    label = { Text("Nominal (Rp)") },
                    placeholder = { Text("Contoh: 50000") },
                    isError = uiState.amountError != null,
                    supportingText = uiState.amountError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    prefix = { Text("Rp ") }
                )

                // ── Kategori ─────────────────────────────────
                val categoryDisplay = uiState.category.toDisplay()
                OutlinedTextField(
                    value = categoryDisplay.label,
                    onValueChange = {},
                    label = { Text("Kategori") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showCategoryPicker = true }) {
                            Icon(
                                Icons.Default.KeyboardArrowDown,
                                contentDescription = "Pilih Kategori"
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = categoryDisplay.icon,
                            contentDescription = null,
                            tint = categoryDisplay.color
                        )
                    }
                )

                // ── Metode Pembayaran ────────────────────────
                PaymentMethodPicker(
                    selectedMethod = uiState.paymentMethod,
                    onMethodSelected = viewModel::onPaymentMethodChange,
                    modifier = Modifier.fillMaxWidth()
                )

                // ── Field tambahan untuk Credit ──────────────
                if (uiState.paymentMethod is PaymentMethod.Credit) {
                    OutlinedTextField(
                        value = uiState.bankName,
                        onValueChange = viewModel::onBankNameChange,
                        label = { Text("Nama Bank") },
                        placeholder = { Text("Contoh: BCA, Mandiri") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                // ── Field tambahan untuk E-Wallet ────────────
                if (uiState.paymentMethod is PaymentMethod.EWallet) {
                    OutlinedTextField(
                        value = uiState.walletName,
                        onValueChange = viewModel::onWalletNameChange,
                        label = { Text("Nama E-Wallet") },
                        placeholder = { Text("Contoh: GoPay, OVO, Dana") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                // ── Tanggal ──────────────────────────────────
                OutlinedTextField(
                    value = uiState.date.format(
                        DateTimeFormatter.ofPattern("dd MMMM yyyy")
                    ),
                    onValueChange = {},
                    label = { Text("Tanggal") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                Icons.Default.CalendarMonth,
                                contentDescription = "Pilih Tanggal"
                            )
                        }
                    }
                )

                // ── Catatan ──────────────────────────────────
                OutlinedTextField(
                    value = uiState.note,
                    onValueChange = viewModel::onNoteChange,
                    label = { Text("Catatan (opsional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )

                // ── Tombol Simpan ────────────────────────────
                Button(
                    onClick = { viewModel.saveTransaction(transactionId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Indigo500,
                        contentColor = TextOnDark
                    ),
                    enabled = !uiState.isLoading
                ) {
                    Text(
                        text = if (transactionId != null)
                            "Update Transaksi"
                        else
                            "Simpan Transaksi"
                    )
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}