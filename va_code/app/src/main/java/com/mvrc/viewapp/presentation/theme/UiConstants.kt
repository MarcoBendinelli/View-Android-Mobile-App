package com.mvrc.viewapp.presentation.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.mvrc.viewapp.R

/**
 * Object containing constants related to UI elements, such as paddings and font families.
 */
object UiConstants {
    // Paddings
    const val AUTH_CONTENT_H_PADDING = 30
    const val AUTH_CONTENT_V_PADDING = 20
    const val AUTH_CONTENT_TOP_PADDING = 30
    const val HOME_CONTENT_PADDING = 20
    const val HOME_CONTENT_TOP_PADDING = 30

    // Fonts
    val openSansLight = FontFamily(Font(R.font.opensans_light))
    val openSansRegular = FontFamily(Font(R.font.opensans_regular))
    val openSansMedium = FontFamily(Font(R.font.opensans_medium))
    val openSansSemiBold = FontFamily(Font(R.font.opensans_semibold))
    val openSansBold = FontFamily(Font(R.font.opensans_bold))
    val sourceSans3 = FontFamily(Font(R.font.sourcesans3_regular))
    val robotoLight = FontFamily(Font(R.font.roboto_light))

    // Opacity
    const val MAX_OPACITY = 1.0f
    const val MIN_OPACITY = 0.6f
}