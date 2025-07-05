package com.example.kienldmbtvn.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val primaryTextColor = Color (0xFFE400D9)
val secondaryTextColor = Color(0xFFFFFFFF)
val primaryBackgroundColor = Color (0xFFFFFFFF)
val errorBackgroundColor = Color(0xFFC10000)
val primaryBorderColor = Color (0xFFE400D9)
val normalTextColor = Color (0xFF000000)
val chosenTextColor = Color (0xFF1D00F5)
val startGradientColor = Color (0xFFE400D9)
val endGradientColor = Color (0xFF1D00F5)
val promptTextColor = Color(0x66000000)
val buttonBackground = Brush.linearGradient(
    colors = listOf(
        startGradientColor,
        endGradientColor
    )
)