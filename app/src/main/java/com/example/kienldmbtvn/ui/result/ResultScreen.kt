package com.example.kienldmbtvn.ui.result

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.kienldmbtvn.R
import com.example.kienldmbtvn.base.BaseUIState
import com.example.kienldmbtvn.ui.navigation.AppNavigationHandler
import com.example.kienldmbtvn.ui.style.CommonButton
import com.example.kienldmbtvn.ui.style.FloatingLoadingLottieAnimation
import com.example.kienldmbtvn.ui.theme.LocalCustomColors
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    imageUrl: String,
    viewModel: ResultViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle download state changes
    LaunchedEffect(uiState) {
        val currentState = uiState
        if (currentState is BaseUIState.Success) {
            when (val downloadState = currentState.data.downloadState) {
                is BaseUIState.Success -> {
                    snackbarHostState.showSnackbar("Photo downloaded successfully!")
                    viewModel.clearDownloadState()
                }

                is BaseUIState.Error -> {
                    snackbarHostState.showSnackbar(downloadState.message)
                    viewModel.clearDownloadState()
                }

                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { AppNavigationHandler.goBack(navController) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back"
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(380.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        2.dp,
                        LocalCustomColors.current.primaryBorderColor,
                        shape = RoundedCornerShape(16.dp)
                    ),
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Generated AI Art",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize(),
                    error = painterResource(id = R.drawable.ic_add_photo),
                    placeholder = painterResource(id = R.drawable.ic_add_photo),
                )
            }

            // Download button with loading state
            val currentUiState = uiState
            val isDownloading = currentUiState is BaseUIState.Success &&
                    currentUiState.data.downloadState is BaseUIState.Loading

            CommonButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp, start = 30.dp, end = 30.dp),
                textContent = if (isDownloading) R.string.result_downloading_popup else R.string.result_download_photo,
            ) {
                if (!isDownloading) {
                    viewModel.downloadPhoto(imageUrl)
                }
            }
            if (isDownloading) {
                FloatingLoadingLottieAnimation(
                    text = R.string.result_downloading_popup
                )
            }
        }
    }
}
