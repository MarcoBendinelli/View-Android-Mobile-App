package com.mvrc.viewapp.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.data.model.ViewPost
import com.mvrc.viewapp.domain.state.PaginationState
import com.mvrc.viewapp.presentation.components.LazyListScrollHandler
import com.mvrc.viewapp.presentation.components.ViewSmallCircularProgress

/**
 * [Composable] function for displaying a list of [ForYouPostCard]s.
 *
 * @param posts The list of [ViewPost] objects to be displayed as cards.
 * @param paginationState The [PaginationState] representing the pagination state for lazy loading.
 * @param loadMorePosts The callback function to be invoked when more posts need to be loaded.
 * @param navigateToPostScreen Function to navigate to the Post Screen.
 */
@Composable
fun ForYouPostCards(
    posts: List<ViewPost>,
    paginationState: PaginationState,
    loadMorePosts: () -> Unit,
    navigateToPostScreen: (postId: String) -> Unit,
) {
    // Remember the lazy list state for managing scrolling behavior
    val lazyListState = rememberLazyListState()

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            24.rw(), alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically,
        state = lazyListState,
    ) {
        // Display a message when there are no posts
        if (posts.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.no_post),
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.secondary),
                )

            }
        }
        items(
            items = posts,
        ) { post ->
            ForYouPostCard(post = post) { navigateToPostScreen(post.id) }
        }
        // Display a small circular progress indicator if more posts are being loaded
        if (!paginationState.endReached) {
            item {
                ViewSmallCircularProgress()
            }
        }
    }

    // Scroll handler for loading more for you posts
    LazyListScrollHandler(lazyListState = lazyListState) {
        loadMorePosts()
    }
}