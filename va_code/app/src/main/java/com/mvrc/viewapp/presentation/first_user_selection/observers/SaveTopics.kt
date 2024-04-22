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
 * [Composable] which observes the state of the save topics process
 * and handles the UI accordingly.
 *
 * @param viewModel The ViewModel responsible for first selection logic.
 * @param onSuccess Callback to execute on success.
 * @param showSnackBar Callback to show a snackBar with a given message.
 */
@Composable
fun SaveTopics(
    viewModel: FirstUserSelectionViewModel = hiltViewModel(),
    onSuccess: () -> Unit,
    showSnackBar: (message: String) -> Unit
) {
    // Observe the state of the Save users response
    when (val saveTopicsResponse = viewModel.saveTopicsResponseStateFlow.collectAsState().value) {
        // Loading state - show a circular progress indicator
        is Loading -> ViewCircularProgress()
        is Success -> if (saveTopicsResponse.data!!) onSuccess() else Unit
        // Failure state - log the error and show a SnackBar
        is Failure -> saveTopicsResponse.apply {
            logMessage(
                "Failure",
                "SaveTopics: ${saveTopicsResponse.e.message.toString()}"
            )
            showSnackBar(AuthRepositoryImpl.fromFirebaseException(saveTopicsResponse.e))
        }
    }
}