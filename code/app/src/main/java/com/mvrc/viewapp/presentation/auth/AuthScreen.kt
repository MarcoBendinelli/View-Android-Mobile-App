package com.mvrc.viewapp.presentation.auth

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider.getCredential
import com.mvrc.viewapp.AppViewModel
import com.mvrc.viewapp.presentation.auth.observers.OneTapSignIn
import com.mvrc.viewapp.presentation.auth.observers.SignInWithGoogle
import kotlinx.coroutines.launch

/**
 * [Composable] representing the first authentication screen seen by users,
 * allowing them to sign up or sign in with email or Google.
 *
 * @param authViewModel View model responsible for authentication operations.
 * @param appViewModel View model responsible for the app status.
 * @param navigateToSignInScreen Callback function to navigate to the sign-in screen.
 * @param navigateToSignUpScreen Callback function to navigate to the sign-up screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    appViewModel: AppViewModel,
    navigateToSignInScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit
) {
    // SnackBar related observers: showSnackBar state necessary to not show 2 times
    // the snackBar on configuration changes
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showSnackBar by rememberSaveable { mutableStateOf(false) }

    // True if it is the first time accessing with Google
    var isFirstGoogleLogIn by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(authViewModel.isFirstGoogleLoginStateFlow) {
        authViewModel.isFirstGoogleLoginStateFlow.collect { value ->
            isFirstGoogleLogIn = value
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
    ) { padding ->
        // AuthContent composable for the main authentication content
        AuthContent(
            oneTapSignIn = {
                showSnackBar = true
                // Wait for the redirection to retrieve the Google Sign-In result.
                appViewModel.notRedirectUser()
                // Access to Google and check if the user_profile is already present in the database
                authViewModel.oneTapSignIn()
            },
            navigateToSignInScreen = navigateToSignInScreen,
            navigateToSignUpScreen = navigateToSignUpScreen,
            padding = padding
        )
    }

    // Activity result launcher for handling the one-tap sign-in result
    val launcher = rememberLauncherForActivityResult(StartIntentSenderForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            try {
                // Extracting Google credentials from the result
                val credentials =
                    authViewModel.oneTapClient.getSignInCredentialFromIntent(result.data)
                val googleIdToken = credentials.googleIdToken
                val googleCredentials = getCredential(googleIdToken, null)
                authViewModel.signInWithGoogle(googleCredentials)
            } catch (it: ApiException) {
                print(it)
            }
        }
    }

    // Launch the one-tap sign-in flow
    fun launch(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }

    // Function to show a snackBar with a given message
    fun showSnackBar(message: String) = coroutineScope.launch {
        if (showSnackBar) {
            snackBarHostState.showSnackbar(message)
            showSnackBar = false
        }
    }

    OneTapSignIn(
        launch = {
            launch(it)
        },
        showSnackBar = {
            showSnackBar(it)
        }
    )

    SignInWithGoogle(
        showSnackBar = {
            showSnackBar(it)
        },
        onSuccess = {
            // Redirect the user_profile to the correct screen
            appViewModel.onGoogleSignIn(isFirstGoogleLogIn)
        },
        onFailure = {
            // Reset the app status
            appViewModel.enableUserRedirection()
        })
}
