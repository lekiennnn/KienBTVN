package com.example.kienldmbtvn.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.kienldmbtvn.R

private val CustomFontFamily = FontFamily(
    Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold),
    Font(R.font.plus_jakarta_sans_semibold, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans_medium, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal)
)
fun Float.pxToSp(fontScale: Float) = this * fontScale

data class TitleTypography(
    val bold: TextStyle,
    val semiBold: TextStyle,
    val medium: TextStyle,
    val regular: TextStyle
)

val LocalCustomTypography = compositionLocalOf { CustomTypography.Default }

abstract class CustomTypography {
    abstract val Title: TitleTypography
    abstract val CategoryTypography: TitleTypography
    abstract val StyleTypoGraphy: TitleTypography
    abstract val ButtonTypoGraphy: TitleTypography
    abstract val ErrorTypoGraphy: TitleTypography
    abstract val PromptTypoGraphy: TitleTypography

    companion object {
        val Default = createTypography { fontSize, lineHeight ->
            TitleTypography(
                createTextStyle(FontWeight.Bold, fontSize, lineHeight),
                createTextStyle(FontWeight.SemiBold, fontSize, lineHeight),
                createTextStyle(FontWeight.Medium, fontSize, lineHeight),
                createTextStyle(FontWeight.Normal, fontSize, lineHeight)
            )
        }

        private fun createTextStyle(
            weight: FontWeight,
            fontSize: Float,
            lineHeight: Float
        ) = TextStyle(
            fontFamily = CustomFontFamily,
            fontWeight = weight,
            fontSize = fontSize.sp,
            lineHeight = lineHeight.sp
        )
    }
}

private fun createTypography(
    createTitleTypography: (fontSize: Float, lineHeight: Float) -> TitleTypography
): CustomTypography = object : CustomTypography() {
    override val Title = createTitleTypography(20f, 24f)
    override val CategoryTypography = createTitleTypography(12f, 18f)
    override val StyleTypoGraphy = createTitleTypography(12f, 18f)
    override val ButtonTypoGraphy = createTitleTypography(16f, 24f)
    override val ErrorTypoGraphy = createTitleTypography(14f, 21f)
    override val PromptTypoGraphy = createTitleTypography(14f, 21f)
}

@Composable
fun rememberCustomTypography(screenScale: Float): CustomTypography {
    val context = LocalContext.current

    return remember(context) {
        createTypography { fontSize, lineHeight ->
            TitleTypography(
                createScaledTextStyle(screenScale, FontWeight.Bold, fontSize, lineHeight),
                createScaledTextStyle(screenScale, FontWeight.SemiBold, fontSize, lineHeight),
                createScaledTextStyle(screenScale, FontWeight.Medium, fontSize, lineHeight),
                createScaledTextStyle(screenScale, FontWeight.Normal, fontSize, lineHeight)
            )
        }
    }
}

private fun createScaledTextStyle(
    fontScale: Float,
    weight: FontWeight,
    fontSize: Float,
    lineHeight: Float
) = TextStyle(
    fontFamily = CustomFontFamily,
    fontWeight = weight,
    fontSize = fontSize.pxToSp(fontScale).sp,
    lineHeight = lineHeight.pxToSp(fontScale).sp
)