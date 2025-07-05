package com.example.kienldmbtvn.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Brush

private val customColorScheme = CustomColorScheme(
    primaryTextColor = primaryTextColor,
    secondaryTextColor = secondaryTextColor,
    primaryBackgroundColor = primaryBackgroundColor,
    errorBackgroundColor = errorBackgroundColor,
    primaryBorderColor = primaryBorderColor,
    normalTextColor = normalTextColor,
    chosenTextColor = chosenTextColor,
    startGradientColor = startGradientColor,
    endGradientColor = endGradientColor,
    buttonBackground = buttonBackground,
    promptTextColor = promptTextColor
)

val LocalCustomColors = compositionLocalOf { customColorScheme }

@Composable
fun KienLDMBTVNTheme(
    content: @Composable () -> Unit
) {
    val materialColorScheme = lightColorScheme(
        primary = primaryTextColor,
    )

    CompositionLocalProvider(
        LocalCustomColors provides customColorScheme
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            content = content
        )
    }
}

data class CustomColorScheme(
    val primaryTextColor: androidx.compose.ui.graphics.Color,
    val secondaryTextColor: androidx.compose.ui.graphics.Color,
    val primaryBackgroundColor: androidx.compose.ui.graphics.Color,
    val errorBackgroundColor: androidx.compose.ui.graphics.Color,
    val primaryBorderColor: androidx.compose.ui.graphics.Color,
    val normalTextColor: androidx.compose.ui.graphics.Color,
    val chosenTextColor: androidx.compose.ui.graphics.Color,
    val startGradientColor: androidx.compose.ui.graphics.Color,
    val endGradientColor: androidx.compose.ui.graphics.Color,
    val promptTextColor: androidx.compose.ui.graphics.Color,
    val buttonBackground: Brush,
)