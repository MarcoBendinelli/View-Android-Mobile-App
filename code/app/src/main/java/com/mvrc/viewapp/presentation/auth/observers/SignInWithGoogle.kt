package com.mvrc.viewapp.presentation.auth.observers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvrc.viewapp.core.Utils.logMessage
import com.mvrc.viewapp.data.model.Response.Failure
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.repository.AuthRepositoryImpl.Companion.fromFirebaseException
import com.mvrc.viewapp.presentation.auth.AuthViewModel
import com.mvrc.viewapp.presentation.components.ViewCircularProgress

/**
 * [Composable] for handling Google sign-in within the authentication screen.
 *
 * @param viewModel View model responsible for authentication operations.
 * @param showSnackBar Callback function to show a snackBar with a given message.
 * @param onFailure Callback function executed when the sign in is not successful.
 */
@Composable
fun SignInWithGoogle(
    viewModel: AuthViewModel = hiltViewModel(),
    showSnackBar: (String) -> Unit,
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    // When expression to handle different states of Google sign-in response
    when (val signInWithGoogleResponse =
        viewModel.signInWithGoogleResponseStateFlow.collectAsState().value) {
        // Loading state - display a circular progress indicator
        is Loading -> ViewCircularProgress()
        is Success -> if (signInWithGoogleResponse.data == true) onSuccess() else Unit
        // Failure state - reset the app status, log the error and show a snackBar
        is Failure -> LaunchedEffect(Unit) {
            onFailure()
            logMessage(
                "Failure",
                "SignInWithGoogle: ${signInWithGoogleResponse.e.message.toString()}"
            )
            showSnackBar(fromFirebaseException(signInWithGoogleResponse.e))
        }
    }
}