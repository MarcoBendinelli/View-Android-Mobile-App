package com.mvrc.viewapp.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import com.mvrc.viewapp.core.ScreenUtils.Companion.rT
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansBold
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansMedium
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansRegular

/**
 * Typography configuration for text styles in the application.
 */
val Typography = Typography(

    // Title size (all in bold)

    displayLarge = TextStyle(
        fontSize = 30.rT(),
        fontFamily = openSansBold,
        color = ViewAlmostBlack,
    ),

    displayMedium = TextStyle(
        fontSize = 28.rT(),
        fontFamily = openSansBold,
        color = ViewAlmostBlack,
    ),

    displaySmall = TextStyle(
        fontSize = 24.rT(),
        fontFamily = openSansBold,
        color = ViewAlmostBlack,
    ),

    headlineLarge = TextStyle(
        fontSize = 18.rT(),
        fontFamily = openSansBold,
        color = ViewAlmostBlack,
    ),

    headlineMedium = TextStyle(
        fontSize = 22.rT(),
        fontFamily = openSansBold,
        color = ViewAlmostBlack,
    ),

    headlineSmall = TextStyle(
        fontSize = 16.rT(),
        fontFamily = openSansBold,
        color = ViewAlmostBlack,
    ),

    titleLarge = TextStyle(
        fontSize = 14.rT(),
        fontFamily = openSansBold,
        color = ViewAlmostBlack,
    ),

    // All the gray, body and other texts

    titleMedium = TextStyle(
        fontSize = 16.rT(),
        fontFamily = openSansRegular,
        color = ViewGray,
    ),

    titleSmall = TextStyle(
        fontSize = 14.rT(),
        fontFamily = openSansRegular,
        color = ViewGray,
    ),

    bodyLarge = TextStyle(
        fontSize = 12.rT(),
        fontFamily = openSansRegular,
        color = ViewGray,
    ),

    // Text button size

    labelLarge = TextStyle(
        fontSize = 16.rT(),
        fontFamily = openSansBold,
        color = ViewAlmostBlack,
    ),

    labelMedium = TextStyle(
        fontSize = 14.rT(),
        fontFamily = openSansBold,
        color = ViewAlmostBlack,
    ),

    // Error message

    labelSmall = TextStyle(
        fontSize = 14.rT(),
        fontFamily = openSansMedium,
        color = ViewAlmostBlack,
    )
)