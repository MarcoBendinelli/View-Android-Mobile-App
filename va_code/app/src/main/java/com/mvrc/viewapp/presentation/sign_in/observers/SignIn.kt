package com.mvrc.viewapp.presentation.sign_in.observers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvrc.viewapp.core.Utils.logMessage
import com.mvrc.viewapp.data.model.Response.Failure
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.repository.AuthRepositoryImpl.Companion.fromFirebaseException
import com.mvrc.viewapp.presentation.components.ViewCircularProgress
import com.mvrc.viewapp.presentation.sign_in.view_model.SignInViewModel

/**
 * [Composable] which observes the state of the Sign In process
 * and handles the UI accordingly.
 *
 * @param viewModel The ViewModel responsible for Sign In logic.
 * @param showSnackBar Callback to show a snackBar with a given message.
 */
@Composable
fun SignIn(
    viewModel: SignInViewModel = hiltViewModel(),
    showSnackBar: (message: String) -> Unit
) {
    // Observe the state of the Sign In response
    when (val signInResponse = viewModel.signInResponseStateFlow.collectAsState().value) {
        // Loading state - show a circular progress indicator
        is Loading -> ViewCircularProgress()
        is Success -> Unit
        // Failure state - log the error and show a SnackBar
        is Failure -> signInResponse.apply {
            logMessage(
                "Failure",
                "SignIn: ${signInResponse.e.message.toString()}"
            )
            showSnackBar(fromFirebaseException(signInResponse.e))
        }
    }
}