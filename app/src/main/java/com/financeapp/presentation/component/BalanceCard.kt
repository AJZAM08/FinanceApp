package com.financeapp.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.financeapp.domain.usecase.BalanceInfo
import com.financeapp.presentation.theme.ExpenseRedLight
import com.financeapp.presentation.theme.IncomeGreenLight
import com.financeapp.presentation.theme.Lavender50
import com.financeapp.presentation.theme.TextOnDark
import com.financeapp.presentation.theme.TextPrimary
import com.financeapp.presentation.theme.Violet500

@Composable
fun BalanceCard(
    balanceInfo: BalanceInfo,
    modifier: Modifier = Modifier,
    isBalanceHidden: Boolean = false,
    onToggleVisibility: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Saldo Utama
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Total Saldo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextOnDark.copy(alpha = 0.75f)
                )
                IconButton(
                    onClick = onToggleVisibility,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (isBalanceHidden) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (isBalanceHidden) "Tampilkan Saldo" else "Sembunyikan Saldo",
                        tint = TextOnDark.copy(alpha = 0.75f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            if (isBalanceHidden) {
                Text(
                    text = "Rp ••••••",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 32.sp),
                    fontWeight = FontWeight.Bold,
                    color = TextOnDark
                )
            } else {
                AnimatedCurrencyText(
                    amount = balanceInfo.balance.toDouble(),
                    prefix = "Rp ",
                    textStyle = MaterialTheme.typography.headlineMedium.copy(fontSize = 32.sp),
                    fontWeight = FontWeight.Bold,
                    color = TextOnDark
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth().height(1.dp).background(TextOnDark.copy(alpha = 0.2f))
        )

        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Violet500.copy(alpha = 0.5f)),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ── Box Pemasukan ────────────────────────────────────────
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 14.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Icon di kiri
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Lavender50.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = null,
                        tint = IncomeGreenLight,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Label + Angka di kanan icon
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)  // ← Jarak label & angka tipis
                ) {
                    Text(
                        text = "Pemasukan",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextOnDark.copy(alpha = 0.7f)
                    )
                    if (isBalanceHidden) {
                        Text(
                            text = "Rp ••••••",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = IncomeGreenLight
                        )
                    } else {
                        AnimatedCurrencyText(
                            amount = balanceInfo.totalIncome.toDouble(),
                            prefix = "Rp ",
                            textStyle = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = IncomeGreenLight
                        )
                    }
                }
            }

            Box(
                Modifier
                    .width(1.dp)
                    .height(30.dp)
                    .background(TextPrimary.copy(alpha = 0.2f))
            )

            // ── Box Pengeluaran ────────────────────────────────────────
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 14.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Icon di kiri
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Lavender50.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = null,
                        tint = ExpenseRedLight,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "Pengeluaran",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextOnDark.copy(alpha = 0.7f)
                    )
                    if (isBalanceHidden) {
                        Text(
                            text = "Rp ••••••",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = ExpenseRedLight
                        )
                    } else {
                        AnimatedCurrencyText(
                            amount = balanceInfo.totalExpense.toDouble(),
                            prefix = "Rp ",
                            textStyle = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = ExpenseRedLight
                        )
                    }
                }
            }
        }
    }
}