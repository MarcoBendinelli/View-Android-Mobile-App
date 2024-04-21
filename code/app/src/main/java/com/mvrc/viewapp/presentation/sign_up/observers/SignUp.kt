package com.mvrc.viewapp.presentation.sign_up.observers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvrc.viewapp.core.Utils.logMessage
import com.mvrc.viewapp.data.model.Response.Failure
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.repository.AuthRepositoryImpl.Companion.fromFirebaseException
import com.mvrc.viewapp.presentation.components.ViewCircularProgress
import com.mvrc.viewapp.presentation.sign_up.view_model.SignUpViewModel

/**
 * [Composable] which observes the state of the Sign Up process
 * and handles the UI accordingly
 *
 * @param viewModel The [SignUpViewModel] responsible for managing the Sign Up screen's logic.
 * @param showSnackBar Callback to display a snackBar with a given message.
 * @param onFailure Callback function executed when the sign up is not successful.
 * @param onSuccess Callback function executed when the sign up is successful.
 */
@Composable
fun SignUp(
    viewModel: SignUpViewModel = hiltViewModel(),
    showSnackBar: (message: String) -> Unit,
    onFailure: () -> Unit,
    onSuccess: () -> Unit
) {
    // Observe the state of the Sign Up response
    when (val signUpResponse = viewModel.signUpResponseStateFlow.collectAsState().value) {
        // Loading state - show a circular progress indicator
        is Loading -> ViewCircularProgress()
        is Success -> if (signUpResponse.data!!) onSuccess() else Unit
        // Failure state - reset the app status, log the error and show a SnackBar
        is Failure -> signUpResponse.apply {
            onFailure()
            logMessage(
                "Failure",
                "SignUp: ${signUpResponse.e.message.toString()}"
            )
            showSnackBar(fromFirebaseException(signUpResponse.e))
        }
    }
}