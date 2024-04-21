package com.mvrc.viewapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.SubcomposeAsyncImage
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.r
import com.mvrc.viewapp.presentation.theme.UiConstants
import com.mvrc.viewapp.presentation.theme.ViewBlue

/**
 * [Composable] that displays a circle box with a possible empty photo.
 *
 * The [ViewAvatar] composable is designed to show a circular box with an
 * optional photo retrieved from the [photoUrl] by using Coil [SubcomposeAsyncImage].
 * If no photo URL is provided, it displays a default placeholder icon.
 *
 * @param photoUrl The URL of the photo to display in the avatar.
 * @param size The size of the avatar, specified as the radius in logical pixels.
 * @param iconSize The size of the profile icon shown when the user_profile has not a profile image.
 * @param isEnabled Boolean flag indicating whether the clickable behaviour is enabled.
 * @param onUserClick The function for navigating to the specific User Screen.
 */
@Composable
fun ViewAvatar(
    photoUrl: String,
    size: Int,
    iconSize: Int,
    isEnabled: Boolean,
    onUserClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundAlpha = if (isPressed) UiConstants.MIN_OPACITY else UiConstants.MAX_OPACITY

    Box(
        modifier = Modifier
            .size((size * 2).r())
            .clip(CircleShape)
            .alpha(backgroundAlpha)
            .background(if (photoUrl.isNotEmpty()) Color.Transparent else ViewBlue)
            .clickable(
                indication = null,  // Disable the default ripple effect
                interactionSource = interactionSource,
                enabled = isEnabled
            ) { onUserClick() },
        contentAlignment = Alignment.Center
    ) {
        if (photoUrl.isNotEmpty()) {
            SubcomposeAsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = photoUrl,
                contentScale = ContentScale.Crop,
                contentDescription = "User Image",
                loading = { ViewSmallCircularProgress() },
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.icon_profile),
                contentDescription = "Profile Icon",
                modifier = Modifier.size(iconSize.r()),
            )
        }
    }
}