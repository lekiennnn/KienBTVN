package com.example.kienldmbtvn.ui.style

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.kienldmbtvn.ui.navigation.AppNavigationHandler
import org.koin.androidx.compose.koinViewModel

@Composable
fun StyleScreen(
    navController: NavHostController,
    viewModel: StyleViewModel = koinViewModel()
) {
    val uriString = navController.currentBackStackEntry?.savedStateHandle?.get<String>(
        AppNavigationHandler.IMAGE_URI_KEY)
    val imageUri = remember(uriString) {
        uriString?.let { Uri.parse(it) } ?: Uri.EMPTY
    }
    val isImageSelected = imageUri != Uri.EMPTY && imageUri.toString().isNotEmpty()

    StyleContents(
        imageUri = imageUri,
        imageUrl = "",
        navController = navController,
        viewModel = viewModel,
        isImageSelected = isImageSelected,
    )
}

//@Preview
//@Composable
//private fun StyleScreenPreview() {
//    StyleScreen()
//}