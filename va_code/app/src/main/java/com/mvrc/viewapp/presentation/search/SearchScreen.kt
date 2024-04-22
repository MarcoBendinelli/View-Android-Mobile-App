package com.mvrc.viewapp.presentation.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * [Composable] representing the Search screen, allowing users to search
 * new posts in base on the post topic, author or name.
 *
 * @param paddingValues The padding values from the main Scaffold.
 * @param searchViewModel ViewModel responsible of holding the Search screen states.
 * @param navigateToPostScreen Function to navigate to the Post Screen.
 */
@Composable
fun SearchScreen(
    paddingValues: PaddingValues,
    searchViewModel: SearchViewModel = hiltViewModel(),
    navigateToPostScreen: (postId: String) -> Unit,
) {
    SearchContent(
        paddingValues = paddingValues,
        searchedText = searchViewModel.searchedTextStateFlow.collectAsState().value,
        onSearch = { searchViewModel.onSearch(it) },
        searchPosts = { searchViewModel.searchPosts() },
        getSearchedPostsPaginated = { searchViewModel.getSearchedPostsPaginated() },
        searchedPaginationState = searchViewModel.paginationSearchedPostsState.collectAsState().value,
        searchedPostsState = searchViewModel.searchedPostsStateFlow.collectAsState().value,
        navigateToPostScreen = navigateToPostScreen
    )
}