package com.example.kienldmbtvn.ui.photopicker

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.kienldmbtvn.R
import com.example.kienldmbtvn.base.BaseUIState
import com.example.kienldmbtvn.ui.navigation.AppNavigationHandler
import com.example.kienldmbtvn.ui.style.FloatingLoadingLottieAnimation
import com.example.kienldmbtvn.ui.theme.LocalCustomColors
import com.example.kienldmbtvn.ui.theme.LocalCustomTypography
import org.koin.androidx.compose.koinViewModel

@Composable
fun PhotoPickerContents(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: PhotoPickerViewModel = koinViewModel(),
) {

    val permission = if (android.os.Build.VERSION.SDK_INT >= 33) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    var hasPermission by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (isGranted) {
            viewModel.loadPhotos()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(permission)
    }

    when {
        !hasPermission -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.ask_for_permission),
                    style = LocalCustomTypography.current.Title.bold
                )
            }
        }

        else -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 20.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { AppNavigationHandler.goBack(navController) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close_photo_picker),
                            contentDescription = null,
                        )
                    }

                    Text(
                        text = stringResource(R.string.next),
                        style = LocalCustomTypography.current.ButtonTypoGraphy.bold,
                        modifier = Modifier.clickable {
                            val selectedPhotoUri = viewModel.getSelectedPhotoUri()
                            AppNavigationHandler.setImageUriAndNavigateBack(navController, selectedPhotoUri)
                        }
                    )
                }
                when (val state = uiState) {
                    is BaseUIState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            FloatingLoadingLottieAnimation(text = R.string.loading)
                        }
                    }

                    is BaseUIState.Success -> {
                        if (state.data.photos.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(stringResource(R.string.no_photos_found))
                            }
                        } else {
                            val context = LocalContext.current

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(state.data.photos.size) { index ->
                                    val photo = state.data.photos[index]
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .aspectRatio(1f)
                                            .clickable { viewModel.togglePhotoSelection(photo) }
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(context)
                                                .data(photo.uri)
                                                .size(200, 200)
                                                .memoryCacheKey("thumbnail_${photo.id}")
                                                .diskCacheKey("thumbnail_${photo.id}")
                                                .crossfade(500)
                                                .build(),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                        Checkbox(
                                            checked = state.data.selectedPhoto == photo,
                                            onCheckedChange = { viewModel.togglePhotoSelection(photo) },
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(4.dp)
                                                .size(24.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }

                    is BaseUIState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = state.message,
                                style = LocalCustomTypography.current.Title.bold,
                                color = LocalCustomColors.current.errorBackgroundColor
                            )
                        }
                    }

                    is BaseUIState.Idle -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stringResource(R.string.no_photos_found))
                        }
                    }
                }
            }
        }
    }
}