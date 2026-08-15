package com.financeapp.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.google.mlkit.vision.text.Text
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.material3.Text

@Composable
fun AnimatedCurrencyText(
    amount: Double,
    modifier: Modifier = Modifier,
    prefix: String = "Rp ",
    textStyle: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null
) {
    val formatter = remember {
        NumberFormat.getNumberInstance(Locale("id", "ID")).apply {
            maximumFractionDigits = 0
        }
    }
    val formattedAmount = remember(amount) {
        formatter.format(amount)
    }
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (prefix.isNotEmpty()) {
            Text(
                prefix,
                style = textStyle,
                color = color,
                fontWeight = fontWeight
            )
        }
        formattedAmount.forEach { char ->
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    slideInVertically { height -> height } togetherWith slideOutVertically { height -> height }
                },
                label = "animatedDigit"
            ) { targetChar ->
                Text(
                    targetChar.toString(),
                    style = textStyle,
                    color = color,
                    fontWeight = fontWeight
                )
            }
        }
    }
}