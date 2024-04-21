package com.mvrc.viewapp.presentation.user_profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.Utils
import com.mvrc.viewapp.data.model.ViewUser
import com.mvrc.viewapp.domain.state.PostsState
import com.mvrc.viewapp.presentation.components.ViewAvatar
import com.mvrc.viewapp.presentation.components.ViewCircularProgress
import com.mvrc.viewapp.presentation.components.buttons.FollowButton
import com.mvrc.viewapp.presentation.theme.UiConstants
import com.mvrc.viewapp.presentation.theme.ViewBlue

/**
 * Top section of the user screen, displaying user related information
 * such as profile image, name, profession, and statistics on followers, following, and the number
 * of posts published. It also includes a button for following the user.
 *
 * @param user The [ViewUser] data representing the user's profile.
 * @param userPostsState State of the user posts.
 * @param currentUserId The current user id logged in.
 * @param followUser Callback function to follow / unfollow a new user.
 */
@Composable
fun UserSummary(
    user: ViewUser,
    userPostsState: PostsState,
    followUser: () -> Unit,
    currentUserId: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Profile Image
        ViewAvatar(photoUrl = user.photoUrl, size = 46, iconSize = 32, isEnabled = false) {}

        Spacer(modifier = Modifier.height(30.rh()))

        if (currentUserId != user.id) {
            // Follow the contributor button
            FollowButton(
                style = MaterialTheme.typography.titleLarge.copy(
                    color = ViewBlue,
                    fontFamily = UiConstants.openSansSemiBold
                ),
                widthButton = 104,
                heightButton = 38,
                sizeShape = 14,
                enabled = !user.followers.contains(currentUserId)
            ) {
                followUser()
            }

            Spacer(modifier = Modifier.height(25.rh()))
        }

        Text(
            text = user.username,
            style = MaterialTheme.typography.headlineLarge.copy(
                color = MaterialTheme.colorScheme.secondary
            )
        )

        Spacer(modifier = Modifier.height(10.rh()))

        Text(
            text = user.profession,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.rh()))

        when {
            // Loading
            userPostsState.isLoading -> {
                ViewCircularProgress()
            }
            // Error
            userPostsState.error.isNotEmpty() -> {
                Statistics(
                    numOfFollowers = user.followers.size,
                    numOfFollowing = user.following.size,
                    numOfPosts = 0
                )
                Utils.logMessage("Posts", userPostsState.error)
            }
            // Success
            else -> {
                Statistics(
                    numOfFollowers = user.followers.size,
                    numOfFollowing = user.following.size,
                    numOfPosts = userPostsState.posts.size
                )
            }
        }
        Spacer(modifier = Modifier.height(50.rh()))
    }
}