package com.mvrc.viewapp.presentation.bookmarks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.data.model.ViewUser
import com.mvrc.viewapp.domain.state.PaginationState
import com.mvrc.viewapp.domain.state.PostsState
import com.mvrc.viewapp.presentation.bookmarks.components.BookmarkedPosts
import com.mvrc.viewapp.presentation.components.observers.Posts
import com.mvrc.viewapp.presentation.components.top_app_bars.NameBackTopBar
import com.mvrc.viewapp.presentation.theme.UiConstants.HOME_CONTENT_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.HOME_CONTENT_TOP_PADDING
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * [Composable] function representing the content of the bookmarks screen.
 *
 * @param paddingValues The Scaffold padding values.
 * @param bookmarkedPostsState State of the bookmarked posts.
 * @param bookmarkedPaginationState Pagination state for bookmarked posts.
 * @param onBookmarkClick Callback to add / remove a post from the favorites.
 * @param loadBookmarkedPosts Callback function to load initial bookmarked posts.
 * @param getBookmarkedPostsPaginated Callback function to load more bookmarked posts.
 * @param userStateFlow The current [ViewUser] inside the cache exposed as a [SharedFlow].
 * @param navigateToPostScreen Function to navigate to the Post Screen.
 * @param navigateToUserProfileScreen Function to navigate to the User Profile Screen.
 */
@Composable
fun BookmarksContent(
    paddingValues: PaddingValues,
    bookmarkedPostsState: PostsState,
    bookmarkedPaginationState: PaginationState,
    loadBookmarkedPosts: () -> Unit,
    getBookmarkedPostsPaginated: () -> Unit,
    onBookmarkClick: (String) -> Unit,
    userStateFlow: StateFlow<ViewUser>,
    navigateToPostScreen: (postId: String) -> Unit,
    navigateToUserProfileScreen: (userId: String) -> Unit
) {

    // Fetch posts at the startup
    LaunchedEffect(key1 = true) {
        loadBookmarkedPosts()
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
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
            title = stringResource(id = R.string.saved),
            showBackIcon = false,
        )

        Spacer(
            modifier = Modifier.height(HOME_CONTENT_TOP_PADDING.rh())
        )

        Posts(postsState = bookmarkedPostsState) { posts ->
            BookmarkedPosts(
                posts = posts,
                bookmarkedPaginationState = bookmarkedPaginationState,
                onBookmarkClick = onBookmarkClick,
                userStateFlow = userStateFlow,
                getBookmarkedPostsPaginated = getBookmarkedPostsPaginated,
                navigateToPostScreen = navigateToPostScreen,
                navigateToUserProfileScreen = navigateToUserProfileScreen
            )
        }
    }
}