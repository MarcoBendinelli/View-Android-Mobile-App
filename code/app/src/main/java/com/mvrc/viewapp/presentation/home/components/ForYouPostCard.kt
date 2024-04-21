package com.mvrc.viewapp.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansLight
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansSemiBold
import com.mvrc.viewapp.presentation.theme.ViewBlue

/**
 * [Composable] representing a post card for the "For You" section.
 *
 * The [ForYouPostCard] composable displays the image of the post with rounded corners,
 * the post title, and read time.
 *
 * @param post The [ViewPost] instance representing the post to display.
 * @param onPostClick The function for navigating to the specific Post Screen.
 */
@Composable
fun ForYouPostCard(post: ViewPost, onPostClick: () -> Unit) {
    val context = LocalContext.current

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundAlpha = if (isPressed) UiConstants.MIN_OPACITY else UiConstants.MAX_OPACITY

    Surface(
        modifier = Modifier
            .width(295.rw())
            .height(200.rh())
            .alpha(backgroundAlpha)
            .clickable(
                indication = null,  // Disable the default ripple effect
                interactionSource = interactionSource
            ) { onPostClick() },
        shape = RoundedCornerShape(16.r()),
        color = if (post.imageUrl.isNotEmpty()) Color.Black else ViewBlue
    ) {
        // Post image or document icon
        if (post.imageUrl.isNotEmpty()) {
            SubcomposeAsyncImage(
                model = post.imageUrl,
                contentDescription = "Post Image",
                contentScale = ContentScale.Crop,
                alpha = 0.7f,
                loading = { ViewSmallCircularProgress() },
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.width(50.rw()),
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

        // Post title and read time
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.rw()),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontFamily = openSansSemiBold,
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.rh()))
            Text(
                text = calculateTimeAgoString(
                    dateTime = post.createdAt.toDate(),
                    context = context
                ),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontFamily = openSansLight
                )
            )
            Spacer(modifier = Modifier.height(14.rh()))
        }
    }
}