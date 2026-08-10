package com.financeapp.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Indigo500,
    onPrimary = TextOnDark,
    primaryContainer = Lavender100,
    onPrimaryContainer = Indigo900,

    secondary = Violet500,
    onSecondary = TextOnDark,
    secondaryContainer = Lavender100,
    onSecondaryContainer = Indigo700,

    background = Lavender50,
    onBackground = TextPrimary,

    surface = White,
    onSurface = TextPrimary,
    surfaceVariant = Lavender100,
    onSurfaceVariant = TextSecondary,

    error = ExpenseRed,
    onError = White
)

private val DarkColorScheme = darkColorScheme(
    primary = Violet500,
    onPrimary = TextOnDark,
    primaryContainer = Indigo700,
    onPrimaryContainer = Lavender50,

    secondary = Indigo500,
    onSecondary = TextOnDark,

    background = IndigoDark,
    onBackground = TextOnDark,

    surface = SurfaceDark,
    onSurface = TextOnDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = Lavender100,

    error = ExpenseRed,
    onError = White
)

@Composable
fun FinanceAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}