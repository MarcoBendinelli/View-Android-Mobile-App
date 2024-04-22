package com.mvrc.viewapp.presentation.user_profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.core.Utils
import com.mvrc.viewapp.data.model.ViewUser
import com.mvrc.viewapp.domain.state.PaginationState
import com.mvrc.viewapp.domain.state.PostsState
import com.mvrc.viewapp.presentation.components.LazyListScrollHandler
import com.mvrc.viewapp.presentation.components.ViewCircularProgress
import com.mvrc.viewapp.presentation.components.ViewSmallCircularProgress
import com.mvrc.viewapp.presentation.components.post.ViewSmallPostPreview
import com.mvrc.viewapp.presentation.components.top_app_bars.NameBackTopBar
import com.mvrc.viewapp.presentation.theme.UiConstants.HOME_CONTENT_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.HOME_CONTENT_TOP_PADDING
import com.mvrc.viewapp.presentation.user_profile.components.UserSummary

/**
 * [Composable] function representing the content of the User Profile screen.
 *
 * @param padding The Scaffold padding values.
 * @param followUser Callback function to follow / unfollow a new user.
 * @param navigateBack Callback to navigate back to the previous screen.
 * @param navigateToPostScreen Function to navigate to the Post Screen.
 * @param user The [ViewUser] to display.
 * @param currentUser The current [ViewUser] inside the cache.
 * @param userPostsState State of the user posts.
 * @param userPostsPaginationState Pagination state for user posts.
 * @param getUserPostsPaginated Callback function to load more user posts.
 * @param comingFromPostId The id of the post from which the user clicked the avatar to navigate to the User Screen.
 */
@Composable
fun UserProfileContent(
    padding: PaddingValues,
    navigateBack: () -> Unit,
    navigateToPostScreen: (postId: String) -> Unit,
    user: ViewUser,
    userPostsState: PostsState,
    userPostsPaginationState: PaginationState,
    getUserPostsPaginated: () -> Unit,
    comingFromPostId: String,
    currentUser: ViewUser,
    followUser: () -> Unit,
) {
    // Remember the lazy list state for managing scrolling behavior
    val lazyListState = rememberLazyListState()

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
            title = stringResource(id = R.string.user),
            showBackIcon = true,
            navigateBack = navigateBack
        )

        Spacer(modifier = Modifier.height(HOME_CONTENT_TOP_PADDING.rh()))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = lazyListState
        ) {

            item {
                UserSummary(
                    user = user,
                    currentUserId = currentUser.id,
                    userPostsState = userPostsState,
                    followUser = followUser
                )
            }

            // "Posts from" or "Posts" title
            item {
                Text(
                    text = if (user.username.contains("@")) stringResource(id = R.string.posts)
                    else stringResource(id = R.string.posts_from) + " " + user.username.trim()
                        .split("\\s+".toRegex())[0],
                    style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.secondary)
                )
            }

            item {
                Spacer(modifier = Modifier.height(30.rh()))
            }

            // User Posts

            when {
                // Loading
                userPostsState.isLoading -> {
                    item {
                        ViewCircularProgress()
                    }
                }
                // Error
                userPostsState.error.isNotEmpty() -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.failed_fetch_posts),
                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.secondary)
                            )
                        }
                        Utils.logMessage("Posts", userPostsState.error)
                    }
                }
                // Success
                else -> {
                    if (userPostsState.posts.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.no_post),
                                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.secondary),
                                )
                            }
                        }
                    }

                    items(userPostsState.posts) { post ->
                        Column {
                            ViewSmallPostPreview(
                                post = post
                            ) {
                                if (comingFromPostId == post.id)
                                    navigateBack()
                                else
                                    navigateToPostScreen(post.id)
                            }

                            if (userPostsState.posts.last() != post) {
                                Divider(modifier = Modifier.padding(vertical = 20.rh()))
                            }
                        }
                    }

                    // Display a small circular progress indicator if more posts are being loaded
                    if (!userPostsPaginationState.endReached) {
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ViewSmallCircularProgress()
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(HOME_CONTENT_PADDING.rh()))
            }
        }
    }

    // Scroll handler for loading more posts
    LazyListScrollHandler(lazyListState = lazyListState) {
        getUserPostsPaginated()
    }
}