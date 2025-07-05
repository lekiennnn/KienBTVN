package com.example.kienldmbtvn.di

import com.example.kienldmbtvn.data.AiArtRepository
import com.example.kienldmbtvn.data.impl.AiArtRepositoryImpl
import com.example.kienldmbtvn.data.network.ApiClient
import com.example.kienldmbtvn.data.network.service.AiArtService
import com.example.kienldmbtvn.data.network.service.TimeStampService
import com.example.kienldmbtvn.data.style.StyleApiService
import com.example.kienldmbtvn.data.style.StyleRepository
import com.example.kienldmbtvn.data.style.StyleRepositoryImpl
import com.example.kienldmbtvn.ui.photopicker.PhotoPickerViewModel
import com.example.kienldmbtvn.ui.photopicker.PhotoRepository
import com.example.kienldmbtvn.ui.result.ResultViewModel
import com.example.kienldmbtvn.ui.style.StyleViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val serviceModule = module {
    single<AiArtService> { ApiClient.getAiArtService() }
    single<TimeStampService> { ApiClient.getTimeStampService() }
    single<StyleApiService> { ApiClient.getStyleService() }
}

internal val repositoryModule = module {
    single<AiArtRepository> { AiArtRepositoryImpl(androidContext(), get(), get()) }
    single<StyleRepository> { StyleRepositoryImpl(get()) }
    single { PhotoRepository(androidContext()) }
}

internal val viewModelModule = module {
    viewModel { StyleViewModel(get(), get()) }
    viewModel { PhotoPickerViewModel(get()) }
    viewModel { ResultViewModel(androidContext()) }
}

val appModule = listOf(
    serviceModule,
    repositoryModule,
    viewModelModule
)