package com.mvrc.viewapp.presentation.auth.observers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.mvrc.viewapp.core.Utils.logMessage
import com.mvrc.viewapp.data.model.Response.Failure
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.repository.AuthRepositoryImpl.Companion.fromFirebaseException
import com.mvrc.viewapp.presentation.auth.AuthViewModel
import com.mvrc.viewapp.presentation.components.ViewCircularProgress

/**
 * [Composable] for handling one-tap sign-in within the authentication screen.
 *
 * @param viewModel View model responsible for authentication operations.
 * @param launch Callback function to launch the one-tap sign-in flow.
 * @param showSnackBar Callback function to show a snackBar with a given message.
 */
@Composable
fun OneTapSignIn(
    viewModel: AuthViewModel = hiltViewModel(),
    launch: (result: BeginSignInResult) -> Unit,
    showSnackBar: (String) -> Unit
) {
    // When expression to handle different states of one-tap sign-in response
    when (val oneTapSignInResponse = viewModel.oneTapSignInResponseStateFlow.collectAsState().value) {
        // Loading state - display a circular progress indicator
        is Loading -> ViewCircularProgress()
        // Success state - launch the one-tap sign-in flow with the result
        is Success -> oneTapSignInResponse.data?.let {
            LaunchedEffect(it) {
                launch(it)
            }
        }
        // Failure state - log the error and show a snackBar with a corresponding message
        is Failure -> LaunchedEffect(Unit) {
            logMessage(
                "Failure",
                "OneTapSignIn: ${oneTapSignInResponse.e.message.toString()}"
            )
            showSnackBar(fromFirebaseException(oneTapSignInResponse.e))
        }
    }
}