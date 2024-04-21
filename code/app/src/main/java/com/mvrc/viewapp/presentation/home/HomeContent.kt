package com.mvrc.viewapp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.core.Utils.logMessage
import com.mvrc.viewapp.data.model.ViewUser
import com.mvrc.viewapp.domain.state.PaginationState
import com.mvrc.viewapp.domain.state.PostsState
import com.mvrc.viewapp.presentation.components.LazyListScrollHandler
import com.mvrc.viewapp.presentation.components.ScrollableFilters
import com.mvrc.viewapp.presentation.components.ViewCircularProgress
import com.mvrc.viewapp.presentation.components.ViewSmallCircularProgress
import com.mvrc.viewapp.presentation.components.observers.Posts
import com.mvrc.viewapp.presentation.components.post.ViewBigPostPreview
import com.mvrc.viewapp.presentation.home.components.ForYouPostCards
import com.mvrc.viewapp.presentation.home.components.HomeTopBar
import com.mvrc.viewapp.presentation.home.components.TrendingNowPosts
import com.mvrc.viewapp.presentation.theme.UiConstants.HOME_CONTENT_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansBold

/**
 * [Composable] function representing the main content of the home screen.
 *
 * @param paddingValues Padding values for the LazyColumn.
 * @param forYouPostsState State of the "For You" posts.
 * @param forYouPaginationState Pagination state for "For You" posts.
 * @param followingPostsState State of the following posts.
 * @param followingPaginationState Pagination state for following posts.
 * @param trendingNowPostsState State of the trending now posts.
 * @param selectedFilter Currently selected filter.
 * @param currentUser The current [ViewUser] inside the cache.
 * @param onBookmarkClick Callback to add / remove a post from the favorites.
 * @param loadForYouPosts Callback function to load initial "For You" posts.
 * @param loadTrendingNowPosts Callback function to load initial "Trending Now" posts.
 * @param loadFollowingPosts Callback function to load initial "Following" posts.
 * @param getForYouPostsPaginated Callback function to load more "For You" posts.
 * @param getFollowingPostsPaginated Callback function to load more following posts.
 * @param selectFilter Callback function to handle filter selection.
 * @param navigateToPostScreen Function to navigate to the Post Screen.
 * @param navigateToUserProfileScreen Function to navigate to the User Profile Screen.
 */
@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    forYouPostsState: PostsState,
    forYouPaginationState: PaginationState,
    followingPostsState: PostsState,
    followingPaginationState: PaginationState,
    trendingNowPostsState: PostsState,
    selectedFilter: String,
    currentUser: ViewUser,
    onBookmarkClick: (String) -> Unit,
    loadForYouPosts: () -> Unit,
    loadTrendingNowPosts: () -> Unit,
    loadFollowingPosts: () -> Unit,
    getForYouPostsPaginated: () -> Unit,
    getFollowingPostsPaginated: () -> Unit,
    selectFilter: (String) -> Unit,
    navigateToPostScreen: (postId: String) -> Unit,
    navigateToUserProfileScreen: (userId: String) -> Unit,
) {
    // Remember the lazy list state for managing scrolling behavior
    val lazyListState = rememberLazyListState()

    // Fetch new posts when selectedFilter changes
    LaunchedEffect(selectedFilter) {
        loadForYouPosts()
    }

    // Fetch posts at the startup
    LaunchedEffect(key1 = true) {
        loadTrendingNowPosts()
        loadFollowingPosts()
    }

    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .padding(
                horizontal = HOME_CONTENT_PADDING.rw(),
            ), state = lazyListState
    ) {
        item {
            Column(
                horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(HOME_CONTENT_PADDING.rh()))

                // Home Top Bar
                HomeTopBar(
                    username = currentUser.username,
                    photoUrl = currentUser.photoUrl,
                    onUserAvatarClick = { navigateToUserProfileScreen(currentUser.id) }
                )

                Spacer(modifier = Modifier.height(30.rh()))

                ScrollableFilters(
                    modifier = Modifier.testTag("homeFilters"),
                    topics = currentUser.topics.sorted(),
                    showNewestTopic = true,
                    selectedFilter = selectedFilter,
                    callback = { selectedFilter ->
                        selectFilter(selectedFilter)
                    })

                Spacer(modifier = Modifier.height(40.rh()))

                // For you
                Text(
                    text = stringResource(id = R.string.for_you),
                    style = MaterialTheme.typography.displaySmall.copy(color = MaterialTheme.colorScheme.secondary)
                )

                Spacer(modifier = Modifier.height(10.rh()))

                // For you body
                Text(
                    text = stringResource(id = R.string.for_you_body),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(20.rh()))

                // For you card posts
                Posts(
                    postsState = forYouPostsState
                ) { posts ->
                    ForYouPostCards(
                        posts = posts, paginationState = forYouPaginationState,
                        loadMorePosts = {
                            getForYouPostsPaginated()
                        },
                        navigateToPostScreen = navigateToPostScreen
                    )
                }

                Spacer(modifier = Modifier.height(40.rh()))

                // Trending Now
                Text(
                    text = stringResource(id = R.string.trending_now),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = MaterialTheme.colorScheme.secondary, fontFamily = openSansBold
                    )
                )

                Spacer(modifier = Modifier.height(20.rh()))

                // Trending Now Posts
                Posts(
                    postsState = trendingNowPostsState
                ) { posts ->
                    TrendingNowPosts(posts = posts, navigateToPostScreen = navigateToPostScreen)
                }

                Spacer(modifier = Modifier.height(40.rh()))

                // Following
                Text(
                    text = stringResource(id = R.string.following),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.secondary, fontFamily = openSansBold
                    )
                )

                Spacer(modifier = Modifier.height(20.rh()))
            }
        }

        // Following Posts

        when {
            // Loading
            followingPostsState.isLoading -> {
                item {
                    ViewCircularProgress()
                }
            }
            // Error
            followingPostsState.error.isNotEmpty() -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.failed_fetch_posts),
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.secondary)
                        )
                    }
                    logMessage("Posts", followingPostsState.error)
                }
            }
            // Success
            else -> {
                if (followingPostsState.posts.isEmpty()) {
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

                items(followingPostsState.posts) { post ->
                    Column {
                        ViewBigPostPreview(
                            currentUserId = currentUser.id,
                            post = post,
                            onBookmarkClick = {
                                onBookmarkClick(post.id)
                            },
                            onPostClick = {
                                navigateToPostScreen(post.id)
                            },
                            navigateToUserProfileScreen = { navigateToUserProfileScreen(post.authorId) }
                        )

                        if (followingPostsState.posts.last() != post) {
                            Divider(modifier = Modifier.padding(vertical = 20.rh()))
                        }
                    }
                }

                // Display a small circular progress indicator if more posts are being loaded
                if (!followingPaginationState.endReached) {
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

    // Scroll handler for loading more following posts
    LazyListScrollHandler(lazyListState = lazyListState) {
        getFollowingPostsPaginated()
    }
}
