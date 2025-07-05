package com.example.kienldmbtvn.ui.navigation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController

object AppNavigationHandler {
    const val IMAGE_URI_KEY = "imageUri"

    fun navigateToPhotoPicker(navController: NavController) {
        navController.navigate(AppNavRoutes.PhotoPicker.route)
    }

    fun navigateToStyle(navController: NavController) {
        navController.navigate(AppNavRoutes.Style.route)
    }

    fun navigateToResult(navController: NavController, imageUrl: String) {
        navController.navigate(AppNavRoutes.Result.createRoute(imageUrl))
    }

    fun goBack(navController: NavController) {
        navController.popBackStack()
    }

    fun setImageUriAndNavigateBack(navController: NavController, imageUri: Uri?) {
        if (imageUri != null) {
            navController.previousBackStackEntry?.savedStateHandle?.set(IMAGE_URI_KEY, imageUri.toString())
            navController.popBackStack()
        }
    }

    fun getImageUri(savedStateHandle: SavedStateHandle): Uri? {
        val uriString = savedStateHandle.get<String>(IMAGE_URI_KEY)
        return uriString?.let { Uri.parse(it) }
    }
}
