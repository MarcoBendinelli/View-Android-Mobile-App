package com.mvrc.viewapp.presentation.user_profile.observers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvrc.viewapp.core.Utils
import com.mvrc.viewapp.data.model.Response.Failure
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.repository.AuthRepositoryImpl
import com.mvrc.viewapp.presentation.components.ViewCircularProgress
import com.mvrc.viewapp.presentation.user_profile.UserProfileViewModel

/**
 * [Composable] which observes the state of the Following process
 * and handles the UI accordingly.
 *
 * @param viewModel The ViewModel responsible for the User Following logic.
 * @param showSnackBar Callback to show a snackBar with a given message.
 */
@Composable
fun FollowUser(
    viewModel: UserProfileViewModel = hiltViewModel(),
    showSnackBar: (message: String) -> Unit,
) {
    // Observe the state of the User Following response
    when (val followUserResponse = viewModel.followUserResponseStateFlow.collectAsState().value) {
        // Loading state - show a circular progress indicator
        is Loading -> ViewCircularProgress()
        is Success -> Unit
        // Failure state - log the error and show a SnackBar
        is Failure -> followUserResponse.apply {
            Utils.logMessage(
                "Failure",
                "FollowUser: ${followUserResponse.e.message.toString()}"
            )
            showSnackBar(AuthRepositoryImpl.fromFirebaseException(followUserResponse.e))
        }
    }
}