package com.financeapp.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.financeapp.domain.model.PaymentMethod

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PaymentMethodPicker(
    selectedMethod: PaymentMethod,
    onMethodSelected: (PaymentMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    val methods = listOf(
        PaymentMethod.Cash to "Tunai",
        PaymentMethod.Debit to "Debit/Transfer",
        PaymentMethod.Credit("", java.time.LocalDateTime.now()) to "Kredit",
        PaymentMethod.EWallet("") to "E-Wallet"
    )
    Column(modifier = modifier) {
        Text(
            text = "Metode Pembayaran",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            methods.forEach { (method, label) ->
                val isSelected = selectedMethod::class == method::class
                FilterChip(
                    selected = isSelected,
                    onClick = { onMethodSelected(method) },
                    label = { Text(label) }
                )
            }
        }
    }
}