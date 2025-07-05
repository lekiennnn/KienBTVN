package com.example.kienldmbtvn.ui.main

import androidx.compose.runtime.Composable
import com.example.kienldmbtvn.base.BaseComposeActivity
import com.example.kienldmbtvn.ui.navigation.AppNavHost

class MainActivity : BaseComposeActivity() {
    @Composable
    override fun ScreenContent() {
        AppNavHost()
    }
}