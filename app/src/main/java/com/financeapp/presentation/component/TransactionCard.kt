package com.financeapp.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.financeapp.domain.model.TransactionType
import com.financeapp.domain.model.Transaction
import com.financeapp.presentation.theme.ExpenseRed
import com.financeapp.presentation.theme.IncomeGreen
import com.financeapp.presentation.theme.TextPrimary
import com.financeapp.presentation.theme.TextSecondary
import java.time.format.DateTimeFormatter

@Composable
fun TransactionCard(
    transaction: Transaction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryDisplay = transaction.category.toDisplay()
    val isIncome = transaction.type == TransactionType.INCOME

    Card(
        modifier = modifier.fillMaxWidth().bounceClick(scaleDown = 0.97f) {
            onClick()
        },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(categoryDisplay.color.copy(alpha = 0.15f))
                ) {
                    Icon(
                        imageVector = categoryDisplay.icon,
                        contentDescription = categoryDisplay.label,
                        tint = categoryDisplay.color,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = transaction.title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        maxLines = 1
                    )
                    Text(
                        text = "${categoryDisplay.label} • ${transaction.date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary,
                        maxLines = 1
                    )
                }
            }
            Text(
                text = "${if (isIncome) "+" else "-"} Rp ${
                    String.format("%,.0f", transaction.amount.toDouble())
                }",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (isIncome) IncomeGreen else ExpenseRed
            )
        }
    }
}