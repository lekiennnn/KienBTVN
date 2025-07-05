package com.example.kienldmbtvn.ui.style

import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.kienldmbtvn.R
import com.example.kienldmbtvn.data.network.response.CategoryItem
import com.example.kienldmbtvn.data.network.response.StyleItem
import com.example.kienldmbtvn.ui.theme.LocalCustomColors
import com.example.kienldmbtvn.ui.theme.LocalCustomTypography

@Composable
fun CommonButton(
    modifier: Modifier = Modifier,
    textContent: Int,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    onGenerate: () -> Unit,
) {
    val backgroundModifier = if (isEnabled && !isLoading) {
        Modifier.background(
            brush = LocalCustomColors.current.buttonBackground,
            shape = RoundedCornerShape(8.dp)
        )
    } else {
        Modifier.background(
//            brush = LocalCustomColors.current.buttonBackground.copy(alpha = 0.2f),
            color = Color.White.copy(alpha = 0.3f),
            shape = RoundedCornerShape(8.dp)
        )
    }

    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .then(backgroundModifier)
            .clip(RoundedCornerShape(8.dp)),
        onClick = onGenerate,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White,
        ),
        enabled = isEnabled && !isLoading
    ) {
        Text(stringResource(textContent))
    }
}

@Composable
fun CategoryLazyRow(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    categoryError: String? = null,
    categories: List<CategoryItem> = emptyList(),
    selectedCategoryId: String? = null,
    onCategorySelected: (CategoryItem) -> Unit = {}
) {
    val context = LocalContext.current

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        if (isLoading) {
            items (5){
                ShimmerStyleCard(isCategory = true)
            }
        } else if (categoryError != null) {
            Toast.makeText(context, categoryError, Toast.LENGTH_SHORT).show()
        } else {
            items(categories) { category ->
                val isCategorySelected = selectedCategoryId == category.id
                Text(
                    text = category.name,
                    color = if (isCategorySelected) {
                        LocalCustomColors.current.primaryTextColor
                    } else {
                        LocalCustomColors.current.normalTextColor
                    },
                    style = LocalCustomTypography.current.CategoryTypography.bold,
                    modifier = Modifier
                        .clickable { onCategorySelected(category) },
                )
            }
        }
    }
}

@Composable
fun StyleLazyRow(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    styleError: String? = null,
    styles: List<StyleItem> = emptyList(),
    selectedStyle: StyleItem? = null,
    onStyleSelected: (StyleItem) -> Unit = {},
) {
    val context = LocalContext.current

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        if (isLoading) {
            items(5) {
                ShimmerStyleCard(isCategory = false)
            }
        } else if (styleError != null) {
            Toast.makeText(context, styleError, Toast.LENGTH_SHORT).show()
        } else {
            items(styles) { style ->
                StyleCard(
                    imageUrl = style.key,
                    styleName = style.name,
                    isSelected = selectedStyle?.id == style.id,
                    onClick = { onStyleSelected(style) }
                )
            }
        }
    }
}

@Composable
fun ShimmerStyleCard(
    isCategory: Boolean = false
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, translateAnim),
        end = Offset(translateAnim + 100f, translateAnim + 100f),
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (!isCategory) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(brush)
            )
        }

        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .height(16.dp)
                .width(60.dp)
                .background(brush)
        )
    }
}

@Composable
fun StyleCard(
    modifier: Modifier = Modifier,
    imageUrl: String,
    styleName: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) LocalCustomColors.current.chosenTextColor else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onClick)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = styleName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(LocalCustomColors.current.chosenTextColor.copy(alpha = 0.3f))
                )
            }
        }

        Text(
            text = styleName,
            color = if (isSelected) LocalCustomColors.current.chosenTextColor else LocalCustomColors.current.normalTextColor,
            style = LocalCustomTypography.current.StyleTypoGraphy.semiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun FloatingLoadingLottieAnimation(
    modifier: Modifier = Modifier,
    text: Int,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_loading))

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )

    Box(
        modifier = Modifier
            .size(height = 140.dp, width = 160.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(30.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            LottieAnimation(
                modifier = Modifier.size(85.dp),
                composition = composition,
                progress = { progress },
            )

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(text),
                style = LocalCustomTypography.current.ButtonTypoGraphy.bold,
                color = Color.Black
            )
        }
    }
}