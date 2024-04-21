package com.mvrc.viewapp.presentation.forgot_password.observers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvrc.viewapp.core.Utils.logMessage
import com.mvrc.viewapp.data.model.Response.Failure
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.repository.AuthRepositoryImpl.Companion.fromFirebaseException
import com.mvrc.viewapp.presentation.components.ViewCircularProgress
import com.mvrc.viewapp.presentation.forgot_password.view_model.ForgotPasswordViewModel

/**
 * [Composable] which observes the state of sending a password reset email
 * and handles the UI accordingly.
 *
 * @param viewModel ViewModel for the Forgot Password screen.
 * @param onSuccess Callback to execute on success.
 * @param showSnackBar Callback to show a SnackBar with a specified error message.
 */
@Composable
fun ForgotPassword(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onSuccess: () -> Unit,
    showSnackBar: (errorMessage: String) -> Unit
) {
    // Observe the state of sending a password reset email
    when (val sendPasswordResetEmailResponse =
        viewModel.sendPasswordResetEmailResponseStateFlow.collectAsState().value) {
        // Loading state - show a circular progress indicator
        is Loading -> ViewCircularProgress()
        // Success state - check if the password reset email is sent and navigate to the Email Sent screen
        is Success -> if (sendPasswordResetEmailResponse.data!!) onSuccess() else Unit
        // Failure state - log the error, show a SnackBar, and handle the error message
        is Failure -> sendPasswordResetEmailResponse.apply {
            logMessage(
                "Failure",
                "ForgotPassword: ${sendPasswordResetEmailResponse.e.message.toString()}"
            )
            showSnackBar(fromFirebaseException(sendPasswordResetEmailResponse.e))
        }
    }
}