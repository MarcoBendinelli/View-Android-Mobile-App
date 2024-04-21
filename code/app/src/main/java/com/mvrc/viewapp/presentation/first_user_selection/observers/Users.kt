package com.mvrc.viewapp.presentation.first_user_selection.observers

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
import com.mvrc.viewapp.core.Utils.logMessage
import com.mvrc.viewapp.data.model.Response.Failure
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.model.ViewUser
import com.mvrc.viewapp.presentation.components.ViewCircularProgress
import com.mvrc.viewapp.presentation.first_user_selection.FirstUserSelectionViewModel

/**
 * [Composable] which observes the state of the users fetching process
 * and handles the UI accordingly.
 *
 * @param viewModel The ViewModel responsible for first selection logic.
 * @param content The composable lambda function responsible for rendering the UI when users
 * are successfully loaded.
 */
@Composable
fun Users(
    viewModel: FirstUserSelectionViewModel = hiltViewModel(),
    content: @Composable (posts: List<ViewUser>) -> Unit
) {
    // Observe the state of the fetching users response
    when (val usersFetchingResponse =
        viewModel.usersFetchingResponseStateFlow.collectAsState().value) {
        // Loading state - show a circular progress indicator
        is Loading -> ViewCircularProgress()
        is Success -> content(usersFetchingResponse.data!!)
        // Failure state - log the error and show a SnackBar
        is Failure -> {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.failed_fetch_users),
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.secondary)
                )
            }

            logMessage("Users", usersFetchingResponse.e.message!!)
        }
    }
}