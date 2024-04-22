package com.mvrc.viewapp.presentation.user_profile.observers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.Utils
import com.mvrc.viewapp.data.model.Response.Failure
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.model.ViewUser
import com.mvrc.viewapp.presentation.components.ViewCircularProgress
import com.mvrc.viewapp.presentation.user_profile.UserProfileViewModel

/**
 * [Composable] which observes the state of the User Profile retrieval
 * and handles the UI accordingly.
 *
 * @param viewModel The ViewModel responsible for retrieving the User data.
 * @param userContent The [Composable] to show the user profile on Success.
 */
@Composable
fun UserProfile(
    viewModel: UserProfileViewModel = hiltViewModel(),
    userContent: @Composable (user: ViewUser) -> Unit
) {
    // Observe the state of the Post retrieval
    when (val userResponse = viewModel.userResponseStateFlow.collectAsState().value) {
        // Loading state - show a circular progress indicator
        is Loading -> ViewCircularProgress()
        is Success -> userResponse.data?.let { userContent(it) }
        // Failure state - log the error and show a SnackBar
        is Failure -> userResponse.apply {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.failed_fetch_user),
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.secondary)
                )
            }
            Utils.logMessage("UserProfile", userResponse.e.message.toString())
        }
    }
}