package com.mvrc.viewapp.presentation.components.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.data.model.ViewPost
import com.mvrc.viewapp.presentation.components.ViewContributor
import com.mvrc.viewapp.presentation.theme.UiConstants
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansSemiBold

/**
 * [Composable] representing a big post preview with contributor information and text content.
 *
 * The [ViewBigPostPreview] composable displays a column containing contributor
 * information, post title, post content, and additional details.
 *
 * @param post The [ViewPost] to display.
 * @param currentUserId The id of the user_profile currently logged in.
 * @param onBookmarkClick The callback to be invoked on bookmark click.
 * @param onPostClick The function for navigating to the specific Post Screen.
 * @param navigateToUserProfileScreen Function to navigate to the User Profile Screen.
 */
@Composable
fun ViewBigPostPreview(
    post: ViewPost,
    currentUserId: String,
    onBookmarkClick: () -> Unit,
    onPostClick: () -> Unit,
    navigateToUserProfileScreen: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundAlpha = if (isPressed) UiConstants.MIN_OPACITY else UiConstants.MAX_OPACITY

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(backgroundAlpha)
            .testTag("bigPostButton")
            .clickable(
                indication = null,  // Disable the default ripple effect
                interactionSource = interactionSource
            ) { onPostClick() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Contributor Information with Post Title
            ViewContributor(
                name = post.authorName,
                nameTextStyle = MaterialTheme.typography.titleLarge
                    .copy(color = MaterialTheme.colorScheme.secondary),
                bodyContributor = post.title,
                professionTextStyle = MaterialTheme.typography.bodyLarge
                    .copy(fontFamily = openSansSemiBold),
                photoUrl = post.authorPhotoUrl,
                avatarSize = 15,
                spaceBetweenAvatarAndText = 8,
                profileIconSize = 14,
                isEnabled = true
            ) {
                navigateToUserProfileScreen()
            }

            Spacer(modifier = Modifier.height(20.rh()))

            // Post Subtitle
            Text(
                text = post.subtitle,
                style = MaterialTheme.typography.headlineMedium
                    .copy(
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = openSansSemiBold
                    ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.rh()))

            // Post Content
            Text(
                text = post.body,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(20.rh()))

        // Read Time and Bookmark button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${stringResource(R.string.read_time)} ${post.readTime}",
                style = MaterialTheme.typography.bodyLarge
                    .copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = UiConstants.openSansMedium
                    )
            )
            Icon(
                modifier = Modifier
                    .height(20.rh())
                    .clickable(
                        indication = null,  // Disable the default ripple effect
                        interactionSource = interactionSource
                    ) { onBookmarkClick() },
                painter = if (post.bookmarkedBy.contains(currentUserId)) painterResource(R.drawable.icon_bookmark_blue)
                else painterResource(R.drawable.icon_bookmark),
                contentDescription = "Bookmark Icon",
                tint = Color.Unspecified
            )
        }
    }
}