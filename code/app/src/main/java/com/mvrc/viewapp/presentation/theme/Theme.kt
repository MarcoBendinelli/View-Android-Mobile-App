package com.mvrc.viewapp.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.mvrc.viewapp.core.ScreenUtils

/**
 * Dark color scheme for the ViewApp theme.
 */
private val darkColorScheme = darkColorScheme(
    primary = ViewBlue,
    secondary = Color.White,
    tertiary = ViewAlmostBlack,
    outline = ViewGrayBorder,
    outlineVariant = ViewLightGrayBorder,
    background = Color.Black,
    inverseSurface = Color.White,
    secondaryContainer = ViewLightGrayVariantBorder,
    error = ViewRed
)

/**
 * Light color scheme for the ViewApp theme.
 */
private val lightColorScheme = lightColorScheme(
    primary = ViewBlue,
    secondary = ViewAlmostBlack,
    tertiary = Color.White,
    outline = ViewGrayBorder,
    outlineVariant = ViewLightGrayBorder,
    background = Color.White,
    inverseSurface = Color.Black, // Text Field cursor
    secondaryContainer = ViewLightGrayVariantBorder,
    error = ViewRed
)

/**
 * [Composable] for the ViewApp theme, providing dynamic color support on Android 12+.
 *
 * @param darkTheme Whether the theme is in dark mode.
 * @param dynamicColor Whether to use dynamic colors available on Android 12+.
 * @param content The content to be displayed within the theme.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Initialize app dimensions
    val configuration = LocalConfiguration.current
    ScreenUtils.setDimensions(configuration)

    // Choose color scheme based on dark mode and dynamic color preferences
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    // Apply the color scheme and typography to the MaterialTheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
    ) {
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null,
            content = content
        )
    }
}