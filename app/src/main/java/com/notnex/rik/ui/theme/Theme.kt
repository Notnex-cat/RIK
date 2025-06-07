package com.notnex.rik.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Светлая тема — соответствует макету
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFF3D00),
    onPrimary = Color.White,
    secondary = Color(0xFFFF9100),
    onSecondary = Color.White,
    background = Color(0xFFF9F9F9),     // Фон экрана
    onBackground = Color.Black,
    surface = Color.White,             // Карточки
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFEAEAEA),
    onSurfaceVariant = Color(0xFF505050),
    error = Color(0xFFB00020),
    onError = Color.White
)

// Тёмная тема — в современном контрастном стиле
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF7043),
    onPrimary = Color.Black,
    secondary = Color(0xFFFFAB40),
    onSecondary = Color.Black,
    background = Color(0xFF121212),    // Глубокий тёмный фон
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),        // Тёмные карточки
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFCCCCCC),
    error = Color(0xFFEF5350),
    onError = Color.Black
)

@Composable
fun RikTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = Shapes,
        content = content
    )
}
