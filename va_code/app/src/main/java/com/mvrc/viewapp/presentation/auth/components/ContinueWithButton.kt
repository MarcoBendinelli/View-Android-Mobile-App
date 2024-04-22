package com.mvrc.viewapp.presentation.auth.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.r
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.core.SignInOption
import com.mvrc.viewapp.core.SignInOption.Email
import com.mvrc.viewapp.core.SignInOption.Google
import com.mvrc.viewapp.presentation.theme.UiConstants.MAX_OPACITY
import com.mvrc.viewapp.presentation.theme.UiConstants.MIN_OPACITY
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansSemiBold

/**
 * [Composable] for rendering a "Continue with" button used in the Auth phase
 * to allow the users different type of authentication.
 *
 * @param modifier The Modifier to style the Composable.
 * @param onClick The callback function to be invoked when the button is clicked.
 * @param namePlatform The option to authenticate the user_profile associated with the button.
 */
@Composable
fun ContinueWithButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    namePlatform: SignInOption
) {
    // Mutable interaction source to handle button press interactions
    // and to collect the pressed state
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Calculate background alpha based on the pressed state
    val backgroundAlpha = if (isPressed) MIN_OPACITY else MAX_OPACITY

    // Surface composable to create a clickable and stylized button
    Surface(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = backgroundAlpha),
                shape = RoundedCornerShape(size = 16.r())
            )
            .height(55.rh())
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = interactionSource
            ) {
                onClick() // Invoke the onClick callback when the button is clicked
            },
        color = MaterialTheme.colorScheme.tertiary.copy(alpha = backgroundAlpha)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 28.rw())
                .fillMaxWidth()
        ) {
            // Image composable to display the icon
            Image(
                painter = painterResource(id = getIconId(namePlatform)),
                contentDescription = "${namePlatform.name} Icon",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .height(22.rh())
                    .align(Alignment.CenterStart)
                    .alpha(backgroundAlpha),
                colorFilter = if (namePlatform == Email) ColorFilter.tint(
                    color = MaterialTheme.colorScheme.secondary
                ) else null
            )

            // Text composable to display the button text
            Text(
                text = "${stringResource(id = R.string.continue_with)} ${namePlatform.name}",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontFamily = openSansSemiBold,
                ),
                modifier = Modifier
                    .align(Alignment.Center)
                    .alpha(backgroundAlpha),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

/**
 * Helper function to get the corresponding icon resource ID based on the SignInOption.
 */
private fun getIconId(platform: SignInOption): Int {
    return when (platform) {
        Google -> R.drawable.icon_google
        Email -> R.drawable.icon_email
    }
}