package com.mvrc.viewapp.presentation.search.components

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.data.model.ViewPost
import com.mvrc.viewapp.domain.state.PaginationState
import com.mvrc.viewapp.presentation.components.LazyListScrollHandler
import com.mvrc.viewapp.presentation.components.ViewSmallCircularProgress

/**
 * [Composable] function to display the searched posts.
 *
 * @param posts Searched posts.
 * @param searchedPaginationState Pagination state for searched posts.
 * @param getSearchedPostsPaginated Callback function to load more searched posts.
 * @param navigateToPostScreen Function to navigate to the Post Screen.
 */
@Composable
fun SearchedPosts(
    posts: List<ViewPost>,
    searchedPaginationState: PaginationState,
    getSearchedPostsPaginated: () -> Unit,
    navigateToPostScreen: (postId: String) -> Unit,
) {
    // Remember the lazy list state for managing scrolling behavior
    val lazyListState = rememberLazyListState()

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
                SearchedPost(post = post) { navigateToPostScreen(post.id) }
                if (posts.last() != post) {
                    Spacer(modifier = Modifier.height(40.rh()))
                }
            }
        }

        // Display a small circular progress indicator if more posts are being loaded
        if (!searchedPaginationState.endReached) {
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
        getSearchedPostsPaginated()
    }
}