package com.mvrc.viewapp.presentation.bookmarks

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * [Composable] function representing the bookmarks screen.
 *
 * @param paddingValues Padding values from the Scaffold screen.
 * @param bookmarksViewModel View model containing the states for the bookmarks screen.
 * @param navigateToPostScreen Function to navigate to the Post Screen.
 * @param navigateToUserProfileScreen Function to navigate to the User Profile Screen.
 */
@Composable
fun BookmarksScreen(
    paddingValues: PaddingValues,
    bookmarksViewModel: BookmarksViewModel = hiltViewModel(),
    navigateToPostScreen: (postId: String) -> Unit,
    navigateToUserProfileScreen: (userId: String) -> Unit
) {
    BookmarksContent(
        paddingValues = paddingValues,
        bookmarkedPostsState = bookmarksViewModel.bookmarkedPostsStateFlow.collectAsState().value,
        bookmarkedPaginationState = bookmarksViewModel.paginationBookmarkedPostsState.collectAsState().value,
        onBookmarkClick = { postId -> bookmarksViewModel.saveOrRemoveBookmarkedPost(postId) },
        loadBookmarkedPosts = { bookmarksViewModel.loadBookmarkedPosts() },
        getBookmarkedPostsPaginated = { bookmarksViewModel.getBookmarkedPostsPaginated() },
        userStateFlow = bookmarksViewModel.userStateFlow,
        navigateToPostScreen = navigateToPostScreen,
        navigateToUserProfileScreen = navigateToUserProfileScreen
    )
}