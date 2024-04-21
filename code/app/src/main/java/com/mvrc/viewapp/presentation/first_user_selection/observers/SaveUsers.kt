package com.mvrc.viewapp.presentation.first_user_selection.observers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvrc.viewapp.core.Utils.logMessage
import com.mvrc.viewapp.data.model.Response.Failure
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.repository.AuthRepositoryImpl
import com.mvrc.viewapp.presentation.components.ViewCircularProgress
import com.mvrc.viewapp.presentation.first_user_selection.FirstUserSelectionViewModel

/**
 * [Composable] which observes the state of the save users process
 * and handles the UI accordingly.
 *
 * @param viewModel The ViewModel responsible for first selection logic.
 * @param showSnackBar Callback to show a snackBar with a given message.
 * @param onSuccess Callback executed on success.
 */
@Composable
fun SaveUsers(
    viewModel: FirstUserSelectionViewModel = hiltViewModel(),
    showSnackBar: (message: String) -> Unit,
    onSuccess: () -> Unit
) {
    // Observe the state of the Save users response
    when (val saveUsersResponse = viewModel.saveUsersResponseStateFlow.collectAsState().value) {
        // Loading state - show a circular progress indicator
        is Loading -> ViewCircularProgress()
        is Success -> if (saveUsersResponse.data!!) onSuccess() else Unit
        // Failure state - log the error and show a SnackBar
        is Failure -> saveUsersResponse.apply {
            logMessage(
                "Failure",
                "SaveUsers: ${saveUsersResponse.e.message.toString()}"
            )
            showSnackBar(AuthRepositoryImpl.fromFirebaseException(saveUsersResponse.e))
        }
    }
}