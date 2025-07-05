package com.example.kienldmbtvn.ext

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
import com.example.kienldmbtvn.enumclass.TypeInsets

internal fun Activity.setStatusBarVisibility(
    hideStatusBar: Boolean = true,
    hideNavigationBar: Boolean = true,
    isLightStatusBar: Boolean = true,
) {
    val windowInsetsController =
        WindowCompat.getInsetsController(window, window.decorView)
    windowInsetsController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    if (hideStatusBar) {
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
    }
    if (hideNavigationBar) {
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
    }
    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = isLightStatusBar
}

fun Activity.setStatusBarColor(colorRes: Int) {
    window.statusBarColor = ContextCompat.getColor(this, colorRes)
}

internal fun Activity.displayCutout() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    }
}

internal fun View.insetPaddingSystemBar(type: TypeInsets = TypeInsets.SystemBar) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val systemBars = when (type) {
            TypeInsets.StatusBar -> insets.getInsets(WindowInsetsCompat.Type.statusBars())
            TypeInsets.NavigationBar -> insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            TypeInsets.SystemBar -> insets.getInsets(WindowInsetsCompat.Type.systemBars())
        }
        v.updatePadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        insets
    }
}

