package com.mvrc.viewapp.presentation.components.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.SubcomposeAsyncImage
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.r
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.core.Utils.calculateTimeAgoString
import com.mvrc.viewapp.data.model.ViewPost
import com.mvrc.viewapp.presentation.components.ViewSmallCircularProgress
import com.mvrc.viewapp.presentation.theme.UiConstants
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansSemiBold
import com.mvrc.viewapp.presentation.theme.UiConstants.robotoLight
import com.mvrc.viewapp.presentation.theme.ViewBlue

/**
 * [Composable] representing a small post preview with an image and text content.
 *
 * The [ViewSmallPostPreview] composable displays a row with an image on
 * the left and a column on the right containing text post information.
 *
 * @param post The [ViewPost] to display.
 * @param onPostClick The function for navigating to the specific Post Screen.
 */
@Composable
fun ViewSmallPostPreview(post: ViewPost, onPostClick: () -> Unit) {
    val context = LocalContext.current

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundAlpha = if (isPressed) UiConstants.MIN_OPACITY else UiConstants.MAX_OPACITY

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(backgroundAlpha)
            .testTag("smallPostButton")
            .clickable(
                indication = null,  // Disable the default ripple effect
                interactionSource = interactionSource
            ) { onPostClick() },
    ) {
        // Post Image
        Surface(
            modifier = Modifier
                .size(90.r()),
            shape = RoundedCornerShape(16.r()),
            color = if (post.imageUrl.isNotEmpty()) Color.Black else ViewBlue
        ) {
            if (post.imageUrl.isNotEmpty()) {
                SubcomposeAsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = post.imageUrl,
                    contentDescription = "Post Image",
                    contentScale = ContentScale.Crop,
                    loading = { ViewSmallCircularProgress() },
                )
            } else {
                // Display placeholder icon if no image URL
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier.width(40.rw()),
                    ) {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(R.drawable.icon_document),
                            contentDescription = "Document Icon",
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(20.rw()))

        // Text Content
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = post.authorName,
                style = MaterialTheme.typography.titleSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(3.rh()))
            Text(
                text = post.title,
                style = MaterialTheme.typography.headlineLarge
                    .copy(
                        fontFamily = openSansSemiBold,
                        color = MaterialTheme.colorScheme.secondary
                    ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(3.rh()))
            Text(
                text = post.body,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(3.rh()))
            Text(
                text = calculateTimeAgoString(
                    dateTime = post.createdAt.toDate(),
                    context = context
                ),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontFamily = robotoLight,
                )
            )
        }
    }
}