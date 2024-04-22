package com.mvrc.viewapp.presentation.components.bottom_nav_bar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.navigation.Screen
import com.mvrc.viewapp.presentation.theme.ViewGray

/**
 * Bottom navigation bar item for the custom [ViewBottomNavBar].
 *
 * @param modifier Modifier for customizing the layout and appearance of the item.
 * @param screen The screen associated with the navigation item.
 * @param isClicked Whether the item is currently clicked or not.
 * @param onClick Callback invoked when the item is clicked.
 */
@Composable
fun ViewBottomNavBarItem(
    modifier: Modifier = Modifier,
    screen: Screen,
    isClicked: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                indication = null,  // Disable the default ripple effect
                interactionSource = interactionSource
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(
                id = if (isClicked) if (isSystemInDarkTheme()) screen.iconFilledDark!!
                else screen.iconFilled!! else screen.icon!!
            ),
            tint = if (isClicked) Color.Unspecified else ViewGray,
            contentDescription = null,
            modifier = Modifier
                .height(20.rh())
        )
    }
}