package com.mvrc.viewapp.presentation.post

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvrc.viewapp.presentation.post.observers.Post

/**
 * [Composable] representing the Post screen allowing the user profile to read the corresponded post.
 *
 * @param postViewModel The ViewModel responsible for retrieving the Post data.
 * @param navigateBack Callback to navigate back to the previous screen.
 * @param postId The id of the post to show passed as argument.
 * @param comingFromUserScreen Whether the Screen has been pushed fro the User Screen.
 * @param navigateToUserProfileScreen Function to navigate to the User Profile Screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    postViewModel: PostViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToUserProfileScreen: (userId: String, postId: String) -> Unit,
    postId: String,
    comingFromUserScreen: Boolean,
) {

    // Fetch new posts when selectedFilter changes
    LaunchedEffect(key1 = true) {
        postViewModel.loadPost(postId)
    }

    Scaffold(
        content = { padding ->
            Post { post ->
                PostContent(
                    padding = padding,
                    post = post,
                    comingFromUserScreen = comingFromUserScreen,
                    navigateBack = navigateBack,
                    navigateToUserProfileScreen = navigateToUserProfileScreen
                )
            }
        },
    )
}