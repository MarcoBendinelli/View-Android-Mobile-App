package com.mvrc.viewapp.presentation.bookmarks.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.data.model.ViewPost
import com.mvrc.viewapp.data.model.ViewUser
import com.mvrc.viewapp.domain.state.PaginationState
import com.mvrc.viewapp.presentation.components.LazyListScrollHandler
import com.mvrc.viewapp.presentation.components.ViewSmallCircularProgress
import com.mvrc.viewapp.presentation.components.post.ViewBigPostPreview
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * [Composable] function to display the bookmarked posts.
 *
 * @param posts Bookmarked posts.
 * @param bookmarkedPaginationState Pagination state for bookmarked posts.
 * @param onBookmarkClick Callback to add / remove a post from the favorites.
 * @param getBookmarkedPostsPaginated Callback function to load more bookmarked posts.
 * @param userStateFlow The current [ViewUser] inside the cache exposed as a [SharedFlow].
 * @param navigateToUserProfileScreen Function to navigate to the User Profile Screen.
 */
@Composable
fun BookmarkedPosts(
    posts: List<ViewPost>,
    bookmarkedPaginationState: PaginationState,
    onBookmarkClick: (String) -> Unit,
    getBookmarkedPostsPaginated: () -> Unit,
    userStateFlow: StateFlow<ViewUser>,
    navigateToPostScreen: (postId: String) -> Unit,
    navigateToUserProfileScreen: (userId: String) -> Unit,
) {
    // Remember the lazy list state for managing scrolling behavior
    val lazyListState = rememberLazyListState()
    val currentUser = userStateFlow.collectAsState().value

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyListState,
        verticalArrangement = if (posts.isEmpty()) Arrangement.Center else Arrangement.Top
    ) {
        if (posts.isEmpty()) {
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

        items(posts) { post ->
            Column {
                ViewBigPostPreview(post = post, currentUserId = currentUser.id, onBookmarkClick = {
                    onBookmarkClick(post.id)
                }, onPostClick = {
                    navigateToPostScreen(post.id)
                },
                    navigateToUserProfileScreen = { navigateToUserProfileScreen(post.authorId) }
                )
                if (posts.last() != post) {
                    Divider(modifier = Modifier.padding(vertical = 25.rh()))
                }
            }
        }

        // Display a small circular progress indicator if more posts are being loaded
        if (!bookmarkedPaginationState.endReached) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.rh()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ViewSmallCircularProgress()
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(25.rh()))
        }
    }

    // Scroll handler for loading more following posts
    LazyListScrollHandler(lazyListState = lazyListState) {
        getBookmarkedPostsPaginated()
    }
}