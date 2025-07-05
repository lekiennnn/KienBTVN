package com.example.kienldmbtvn

import android.app.Application
import com.example.kienldmbtvn.data.AiArtServiceEntry
import com.example.kienldmbtvn.data.AiArtServiceEntry.API_KEY
import com.example.kienldmbtvn.data.AiArtServiceEntry.APP_NAME
import com.example.kienldmbtvn.data.AiArtServiceEntry.BUNDLE_ID
import com.example.kienldmbtvn.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@App)
            modules(appModule)
        }

        AiArtServiceEntry.init(
            apiKey = API_KEY,
            appName = APP_NAME,
            bundleId = BUNDLE_ID,
        )
    }
}