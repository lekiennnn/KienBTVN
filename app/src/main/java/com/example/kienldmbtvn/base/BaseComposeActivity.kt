package com.example.kienldmbtvn.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import com.example.kienldmbtvn.ext.setStatusBarVisibility
import com.example.kienldmbtvn.ui.theme.KienLDMBTVNTheme

abstract class BaseComposeActivity : AppCompatActivity(){
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarVisibility(
            hideStatusBar = false,
            hideNavigationBar = true,
            isLightStatusBar = false
        )

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            KienLDMBTVNTheme {
                ScreenContent()
            }
        }

        super.onCreate(savedInstanceState)
    }

    @Composable
    protected abstract fun ScreenContent()
}