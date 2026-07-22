package com.financeapp.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.financeapp.domain.model.TransactionCategory

data class CategoryDisplay(
    val icon: ImageVector,
    val color: Color,
    val label: String
)

fun TransactionCategory.toDisplay(): CategoryDisplay {
    return when (this) {
        TransactionCategory.FOOD -> CategoryDisplay(
            icon = Icons.Default.Fastfood,
            color = Color(0xFFFF5722),
            label = "Makanan"
        )
        TransactionCategory.TRANSPORT -> CategoryDisplay(
            icon = Icons.Default.DirectionsCar,
            color = Color(0xFF2196F3),
            label = "Transportasi"
        )
        TransactionCategory.SHOPPING -> CategoryDisplay(
            icon = Icons.Default.ShoppingBag,
            color = Color(0xFFE91E63),
            label = "Belanja"
        )
        TransactionCategory.HEALTH -> CategoryDisplay(
            icon = Icons.Default.HealthAndSafety,
            color = Color(0xFF4CAF50),
            label = "Kesehatan"
        )
        TransactionCategory.ENTERTAINMENT -> CategoryDisplay(
            icon = Icons.Default.Movie,
            color = Color(0xFF9C27B0),
            label = "Hiburan"
        )
        TransactionCategory.EDUCATION -> CategoryDisplay(
            icon = Icons.Default.School,
            color = Color(0xFF3F51B5),
            label = "Pendidikan"
        )
        TransactionCategory.BILLS -> CategoryDisplay(
            icon = Icons.Default.Receipt,
            color = Color(0xFFFF9800),
            label = "Tagihan"
        )
        TransactionCategory.SALARY -> CategoryDisplay(
            icon = Icons.Default.Work,
            color = Color(0xFF009688),
            label = "Gaji"
        )
        TransactionCategory.FREELANCE -> CategoryDisplay(
            icon = Icons.Default.People,
            color = Color(0xFF00BCD4),
            label = "Freelance"
        )
        TransactionCategory.INVESTMENT -> CategoryDisplay(
            icon = Icons.Default.TrendingUp,
            color = Color(0xFF8BC34A),
            label = "Investasi"
        )
        TransactionCategory.GIFT -> CategoryDisplay(
            icon = Icons.Default.CardGiftcard,
            color = Color(0xFFFF4081),
            label = "Hadiah"
        )
        TransactionCategory.OTHER -> CategoryDisplay(
            icon = Icons.Default.Home,
            color = Color(0xFF607D8B),
            label = "Lainnya"
        )
    }
}