package com.mvrc.viewapp.presentation.user_profile

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvrc.viewapp.presentation.user_profile.observers.FollowUser
import com.mvrc.viewapp.presentation.user_profile.observers.UserProfile
import kotlinx.coroutines.launch

/**
 * [Composable] representing the User screen allowing to read the user information.
 *
 * @param userProfileViewModel The ViewModel responsible for retrieving the User data.
 * @param navigateBack Callback to navigate back to the previous screen.
 * @param navigateToPostScreen Function to navigate to the Post Screen.
 * @param userId The id of the user profile to show passed as argument.
 * @param comingFromPostId The id of the post from which the user clicked the avatar to navigate to the User Screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    userProfileViewModel: UserProfileViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToPostScreen: (postId: String) -> Unit,
    userId: String,
    comingFromPostId: String,
) {
    // SnackBar related observers: showSnackBar state necessary to not show 2 times
    // the snackBar on configuration changes
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showSnackBar by rememberSaveable { mutableStateOf(false) }

    // Function to show a snackBar
    fun showSnackBar(message: String) = coroutineScope.launch {
        if (showSnackBar) {
            snackBarHostState.showSnackbar(message)
            showSnackBar = false
        }
    }

    LaunchedEffect(key1 = true) {
        // Load the User
        userProfileViewModel.loadUser(userId)
        // Load the Posts of the User
        userProfileViewModel.loadUserPosts(userId)
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        content = { padding ->
            UserProfile { user ->
                UserProfileContent(
                    padding = padding,
                    navigateBack = navigateBack,
                    user = user,
                    currentUser = userProfileViewModel.userStateFlow.collectAsState().value,
                    userPostsState = userProfileViewModel.userPostsStateFlow.collectAsState().value,
                    userPostsPaginationState = userProfileViewModel.paginationUserPostsState.collectAsState().value,
                    getUserPostsPaginated = { userProfileViewModel.getUserPostsPaginated(userId) },
                    navigateToPostScreen = navigateToPostScreen,
                    comingFromPostId = comingFromPostId,
                    followUser = {
                        showSnackBar = true
                        userProfileViewModel.followUser(user.id)
                    })
            }
        },
    )

    FollowUser(
        showSnackBar = {
            showSnackBar(it)
        })
}