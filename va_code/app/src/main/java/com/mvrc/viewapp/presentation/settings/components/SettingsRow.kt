package com.mvrc.viewapp.presentation.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.presentation.theme.UiConstants
import com.mvrc.viewapp.presentation.theme.ViewGrayVariation

/**
 * [Composable] representing a row in the settings screen, displaying a setting name
 * with a clickable arrow icon on the right side.
 *
 * @param settingName The name of the setting to be displayed.
 * @param onClick Callback function executed on row click.
 */
@Composable
fun SettingsRow(settingName: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundAlpha = if (isPressed) UiConstants.MIN_OPACITY else UiConstants.MAX_OPACITY

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(backgroundAlpha)
            .clickable(
                indication = null,  // Disable the default ripple effect
                interactionSource = interactionSource
            ) { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = settingName,
            style = MaterialTheme.typography.labelMedium.copy(
                fontFamily = UiConstants.openSansSemiBold,
                color = MaterialTheme.colorScheme.secondary
            )
        )
        Image(
            modifier = Modifier.height(16.rh()),
            painter = painterResource(R.drawable.icon_arrow_right),
            contentDescription = "Back Icon",
            contentScale = ContentScale.FillHeight,
            colorFilter = ColorFilter.tint(
                color = ViewGrayVariation
            )
        )
    }
}