package com.mvrc.viewapp.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.presentation.components.ViewAvatar
import com.mvrc.viewapp.presentation.components.ViewSmallCircularProgress
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansSemiBold
import com.mvrc.viewapp.presentation.theme.UiConstants.sourceSans3

/**
 * [Composable] representing the top bar of the home screen.
 *
 * The [HomeTopBar] composable displays the user_profile's username and profile image at the top
 * of the home screen. It also provides a welcome message and a brief description of
 * the content to be displayed on the home screen.
 *
 * @param username The username of the current user_profile.
 * @param photoUrl The URL of the user_profile's profile photo.
 * @param onUserAvatarClick The function for navigating to the specific User Screen.
 */
@Composable
fun HomeTopBar(username: String, photoUrl: String, onUserAvatarClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // Home title
            if (username.isEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ViewSmallCircularProgress()
                }
            } else {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "${stringResource(id = R.string.hello)}, $username!",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontFamily = openSansSemiBold,
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(10.rw()))

            // Profile Image
            ViewAvatar(photoUrl = photoUrl, size = 21, iconSize = 16, isEnabled = true) {
                onUserAvatarClick()
            }
        }

        // Home body
        Text(
            text = stringResource(id = R.string.hello_body),
            style = MaterialTheme.typography.titleMedium.copy(fontFamily = sourceSans3)
        )
    }
}
