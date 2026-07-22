package com.financeapp.presentation.screen.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.financeapp.domain.model.PaymentMethod
import com.financeapp.domain.model.TransactionType
import com.financeapp.presentation.component.CategoryPickerBottomSheet
import com.financeapp.presentation.component.PaymentMethodPicker
import com.financeapp.presentation.component.toDisplay
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onNavigateBack: () -> Unit,
    transactionId: Long? = null,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showCategoryPicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(transactionId) {
        transactionId?.let { viewModel.loadTransaction(it) }
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
            onCategorySelected = viewModel::onCategoryChange,
            onDismiss = { showCategoryPicker = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (transactionId != null)
                            "Edit Transaksi"
                        else
                            "Tambah Transaksi",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Tipe Transaksi ──────────────────────────
            Column {
                Text(
                    text = "Tipe Transaksi",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TransactionType.entries.forEach { type ->
                        FilterChip(
                            selected = uiState.type == type,
                            onClick = { viewModel.onTypeChange(type) },
                            label = {
                                Text(
                                    if (type == TransactionType.INCOME)
                                        "Pemasukan"
                                    else "Pengeluaran"
                                )
                            }
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
                    .padding(vertical = 8.dp),
                enabled = !uiState.isLoading
            ) {
                Text(
                    text = if (transactionId != null)
                        "Update Transaksi"
                    else
                        "Simpan Transaksi"
                )
            }
        }
    }
}