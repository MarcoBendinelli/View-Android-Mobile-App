package com.mvrc.viewapp.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.domain.state.PaginationState
import com.mvrc.viewapp.domain.state.PostsState
import com.mvrc.viewapp.presentation.components.observers.Posts
import com.mvrc.viewapp.presentation.components.top_app_bars.NameBackTopBar
import com.mvrc.viewapp.presentation.search.components.SearchTextField
import com.mvrc.viewapp.presentation.search.components.SearchedPosts
import com.mvrc.viewapp.presentation.theme.UiConstants.HOME_CONTENT_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.HOME_CONTENT_TOP_PADDING

/**
 * [Composable] representing the content of the Search screen.
 *
 * @param paddingValues Padding values from the Scaffold.
 * @param searchedPostsState State of the searched posts.
 * @param searchedPaginationState Pagination state for searched posts.
 * @param getSearchedPostsPaginated Callback function to load more searched posts.
 * @param searchPosts Callback function to load the searched posts.
 * @param searchedText String representing the content of the Search Text Field.
 * @param onSearch Callback executed on Search Text Field value change.
 * @param navigateToPostScreen Function to navigate to the Post Screen.
 */
@Composable
fun SearchContent(
    paddingValues: PaddingValues,
    searchedPostsState: PostsState,
    searchedPaginationState: PaginationState,
    getSearchedPostsPaginated: () -> Unit,
    searchPosts: () -> Unit,
    searchedText: String,
    onSearch: (String) -> Unit,
    navigateToPostScreen: (postId: String) -> Unit
) {
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
            title = stringResource(id = R.string.search),
            showBackIcon = false,
        )

        Spacer(modifier = Modifier.height(HOME_CONTENT_TOP_PADDING.rh()))

        // Search Text Field
        SearchTextField(value = searchedText) { inputText ->
            onSearch(inputText)
            searchPosts()
        }

        Spacer(modifier = Modifier.height(40.rh()))

        // Searched Posts
        Posts(postsState = searchedPostsState) { posts ->
            SearchedPosts(
                posts = posts,
                searchedPaginationState = searchedPaginationState,
                getSearchedPostsPaginated = getSearchedPostsPaginated,
                navigateToPostScreen = navigateToPostScreen
            )
        }
    }
}