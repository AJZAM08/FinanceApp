package com.financeapp.presentation.screen.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.financeapp.domain.model.TransactionCategory
import com.financeapp.domain.model.TransactionType
import com.financeapp.presentation.component.TransactionCard
import com.financeapp.presentation.component.TransactionItemSkeleton
import com.financeapp.presentation.component.toDisplay
import com.financeapp.presentation.theme.Indigo500
import com.financeapp.presentation.theme.Indigo700
import com.financeapp.presentation.theme.Lavender50
import com.financeapp.presentation.theme.TextOnDark
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEditTransaction: (Long) -> Unit,
    viewModel: TransactionListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDateRangePicker by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    if (showDateRangePicker) {
        DateRangePickerModal(
            onDateRangeSelected = { range ->
                viewModel.onDateRangeSelect(range.first, range.second)
            },
            onDismiss = { showDateRangePicker = false }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // ── Header Indigo ────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.28f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Indigo700, Indigo500)
                    )
                )
        )

        Column(modifier = Modifier.fillMaxSize()) {

            // ── Header bar ───────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .padding(top = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = TextOnDark
                        )
                    }
                    Text(
                        text = "Riwayat Keuangan",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextOnDark,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Tombol Reset filter di kanan header
                val hasFilters = uiState.searchQuery.isNotEmpty() ||
                        uiState.selectedCategory != null ||
                        uiState.selectedType != null ||
                        uiState.startDate != null
                if (hasFilters) {
                    TextButton(onClick = { viewModel.resetFilters() }) {
                        Text(
                            text = "Reset",
                            color = TextOnDark,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            // ── Panel Putih Melengkung ────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = viewModel::onSearchQueryChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 20.dp, bottom = 8.dp),
                        placeholder = { Text("Cari transaksi...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Cari",
                                tint = Indigo500
                            )
                        },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Bersihkan"
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp)
                    )

                    // 🎛️ Filter Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Tipe Filter Chip
                        TransactionType.entries.forEach { type ->
                            val isSelected = uiState.selectedType == type
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    viewModel.onTypeSelect(if (isSelected) null else type)
                                },
                                label = {
                                    Text(if (type == TransactionType.INCOME) "Pemasukan" else "Pengeluaran")
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Indigo500,
                                    selectedLabelColor = TextOnDark,
                                    selectedLeadingIconColor = TextOnDark
                                )
                            )
                        }

                        // Date Picker Chip
                        val dateLabel = if (uiState.startDate != null && uiState.endDate != null) {
                            val start = Instant.ofEpochMilli(uiState.startDate!!)
                                .atZone(ZoneId.systemDefault()).toLocalDate()
                            val end = Instant.ofEpochMilli(uiState.endDate!!)
                                .atZone(ZoneId.systemDefault()).toLocalDate()
                            val formatter = DateTimeFormatter.ofPattern("dd MMM")
                            "${start.format(formatter)} - ${end.format(formatter)}"
                        } else {
                            "Pilih Tanggal"
                        }

                        FilterChip(
                            selected = uiState.startDate != null,
                            onClick = { showDateRangePicker = true },
                            label = { Text(dateLabel) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = "Tanggal",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor     = Indigo500,
                                selectedLabelColor         = TextOnDark,
                                selectedLeadingIconColor   = TextOnDark,
                            )
                        )
                    }

                    // 📂 Category Filter Horizontal List
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TransactionCategory.entries.forEach { category ->
                            val isSelected = uiState.selectedCategory == category
                            val display = category.toDisplay()
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    viewModel.onCategorySelect(if (isSelected) null else category)
                                },
                                label = { Text(display.label) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = display.icon,
                                        contentDescription = display.label,
                                        modifier = Modifier.size(FilterChipDefaults.IconSize),
                                        tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else display.color
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor     = Indigo500,
                                    selectedLabelColor         = TextOnDark,
                                    selectedLeadingIconColor   = TextOnDark,
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 📦 List Area
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.isLoading) {
                            // Shimmer loading
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(5) {
                                    TransactionItemSkeleton()
                                }
                            }
                        } else if (uiState.transactions.isEmpty()) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FilterList,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.outline
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Tidak Ada Transaksi",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Coba ubah kata kunci pencarian atau bersihkan filter kamu.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            // Actual Transaction List
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(
                                    items = uiState.transactions,
                                    key = { it.id }
                                ) { transaction ->
                                    TransactionCard(
                                        transaction = transaction,
                                        onClick = { onNavigateToEditTransaction(transaction.id) },
                                        modifier = Modifier.fillMaxWidth().animateItem()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
    onDismiss: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateRangeSelected(
                        Pair(
                            dateRangePickerState.selectedStartDateMillis,
                            dateRangePickerState.selectedEndDateMillis
                        )
                    )
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(
                    text = "Pilih Rentang Tanggal",
                    modifier = Modifier.padding(start = 24.dp, top = 24.dp),
                    fontWeight = FontWeight.Bold
                )
            },
            showModeToggle = false,
            modifier = Modifier.weight(1f)
        )
    }
}