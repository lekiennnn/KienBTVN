package com.example.kienldmbtvn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kienldmbtvn.ui.photopicker.PhotoPickerScreen
import com.example.kienldmbtvn.ui.result.ResultScreen
import com.example.kienldmbtvn.ui.style.StyleScreen
import java.net.URLDecoder

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppNavRoutes.Style.route,
    ) {
        composable(AppNavRoutes.Style.route) {
            StyleScreen(navController = navController)
        }

        composable(AppNavRoutes.PhotoPicker.route) {
            PhotoPickerScreen(navController = navController)
        }

        composable(
            route = AppNavRoutes.Result.route,
            arguments = listOf(
                navArgument("imageUrl") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val encodedImageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
            val imageUrl = URLDecoder.decode(encodedImageUrl, "UTF-8")
            ResultScreen(
                navController = navController,
                imageUrl = imageUrl
            )
        }
    }
}