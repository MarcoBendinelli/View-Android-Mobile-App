package com.mvrc.viewapp.presentation.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * [Composable] function representing the entire home screen.
 *
 * @param paddingValues Padding values for the entire screen.
 * @param homeViewModel View model containing the states for the home screen.
 * @param navigateToPostScreen Function to navigate to the Post Screen.
 * @param navigateToUserProfileScreen Function to navigate to the User Profile Screen.
 */
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToPostScreen: (postId: String) -> Unit,
    navigateToUserProfileScreen: (userId: String) -> Unit
) {
    HomeContent(
        paddingValues = paddingValues,
        currentUser = homeViewModel.userStateFlow.collectAsState().value,
        forYouPostsState = homeViewModel.forYouPostsStateFlow.collectAsState().value,
        forYouPaginationState = homeViewModel.paginationForYouPostsState.collectAsState().value,
        followingPostsState = homeViewModel.followingPostsStateFlow.collectAsState().value,
        followingPaginationState = homeViewModel.paginationFollowingPostsState.collectAsState().value,
        trendingNowPostsState = homeViewModel.trendingNowPostsStateFlow.collectAsState().value,
        onBookmarkClick = { postId -> homeViewModel.saveOrRemoveBookmarkedPost(postId) },
        selectedFilter = homeViewModel.selectedFilterStateFlow.collectAsState().value,
        selectFilter = { filter -> homeViewModel.selectFilter(filter = filter) },
        loadForYouPosts = { homeViewModel.loadForYouPosts() },
        loadFollowingPosts = { homeViewModel.loadFollowingPosts() },
        loadTrendingNowPosts = { homeViewModel.loadTrendingNowPosts() },
        getForYouPostsPaginated = { homeViewModel.getForYouPostsPaginated() },
        getFollowingPostsPaginated = { homeViewModel.getFollowingPostsPaginated() },
        navigateToPostScreen = navigateToPostScreen,
        navigateToUserProfileScreen = navigateToUserProfileScreen
    )
}