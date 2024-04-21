package com.mvrc.viewapp.presentation.post

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil.compose.SubcomposeAsyncImage
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.r
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.data.model.ViewPost
import com.mvrc.viewapp.presentation.components.ViewContributor
import com.mvrc.viewapp.presentation.components.ViewSmallCircularProgress
import com.mvrc.viewapp.presentation.components.top_app_bars.NameBackTopBar
import com.mvrc.viewapp.presentation.theme.UiConstants.HOME_CONTENT_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.HOME_CONTENT_TOP_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansSemiBold

/**
 * [Composable] function representing the content of the Post screen.
 *
 * @param padding The Scaffold padding values.
 * @param navigateBack Callback to navigate back to the previous screen.
 * @param post The [ViewPost] to display.
 * @param navigateToUserProfileScreen Function to navigate to the User Profile Screen.
 * @param comingFromUserScreen Whether the Screen has been pushed fro the User Screen.
 */
@Composable
fun PostContent(
    padding: PaddingValues,
    navigateBack: () -> Unit,
    navigateToUserProfileScreen: (userId: String, postId: String) -> Unit,
    post: ViewPost,
    comingFromUserScreen: Boolean
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(
                start = HOME_CONTENT_PADDING.rw(),
                end = HOME_CONTENT_PADDING.rw(),
                top = HOME_CONTENT_TOP_PADDING.rh()
            )
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        NameBackTopBar(
            title = stringResource(id = R.string.post),
            showBackIcon = true,
            navigateBack = navigateBack
        )

        Spacer(modifier = Modifier.height(HOME_CONTENT_TOP_PADDING.rh()))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                ViewContributor(
                    name = post.authorName,
                    nameTextStyle = MaterialTheme.typography.headlineLarge
                        .copy(color = MaterialTheme.colorScheme.secondary),
                    bodyContributor = post.title,
                    professionTextStyle = MaterialTheme.typography.titleSmall
                        .copy(fontFamily = openSansSemiBold),
                    photoUrl = post.authorPhotoUrl,
                    avatarSize = 35,
                    spaceBetweenAvatarAndText = 16,
                    spaceBetweenTexts = 12,
                    profileIconSize = 24,
                    isEnabled = true
                ) {
                    if (comingFromUserScreen)
                        navigateBack()
                    else
                        navigateToUserProfileScreen(post.authorId, post.id)
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.rh()))
            }

            // Subtitle
            item {
                Text(
                    text = post.subtitle,
                    style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.secondary)
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.rh()))
            }

            // Image
            if (post.imageUrl != "") {
                item {
                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .clip(RoundedCornerShape(size = 16.r()))
                            .height(197.rh()),
                        model = post.imageUrl,
                        contentDescription = "Post Image",
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        loading = { ViewSmallCircularProgress() },
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(20.rh()))
                }
            }

            // Body
            item {
                Text(
                    text = post.body,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.rh()))
            }
        }
    }
}