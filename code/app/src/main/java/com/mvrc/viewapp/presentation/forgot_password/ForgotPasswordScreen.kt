package com.mvrc.viewapp.presentation.forgot_password

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvrc.viewapp.domain.event.ValidationEvent
import com.mvrc.viewapp.presentation.components.top_app_bars.BackSkipTopBar
import com.mvrc.viewapp.presentation.forgot_password.observers.ForgotPassword
import com.mvrc.viewapp.presentation.forgot_password.view_model.ForgotPasswordViewModel
import kotlinx.coroutines.launch

/**
 * [Composable] representing the Forgot Password screen, allowing users to reset their password
 * by entering their email address and receiving a password reset email.
 *
 * @param forgotPasswordViewModel ViewModel for the Forgot Password screen.
 * @param navigateBack Callback to navigate back to the previous screen.
 * @param navigateToEmailSentScreen Callback to navigate to the Email Sent screen after a successful email submission.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    forgotPasswordViewModel: ForgotPasswordViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToEmailSentScreen: () -> Unit
) {
    // Keyboard controller, interaction source, and focus manager to hide the keyboard and
    // remove the focus on the text field when users press outside these
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    // SnackBar related observers: showSnackBar state necessary to not show 2 times
    // the snackBar on configuration changes
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showSnackBar by rememberSaveable { mutableStateOf(false) }

    // State related to validation and UI interaction
    val forgotPasswordState = forgotPasswordViewModel.forgotPasswordStateFlow.collectAsState().value
    // State related to the success validation
    var successValidation by rememberSaveable {
        mutableStateOf(false)
    }

    // Collect validation events from the ViewModel
    LaunchedEffect(key1 = context) {
        forgotPasswordViewModel.validationEvents.collect { event ->
            successValidation = when (event) {
                is ValidationEvent.Success -> true
                is ValidationEvent.Failure -> false
            }
        }
    }

    Scaffold(
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = interactionSource
        ) {
            // Hide keyboard and clear focus on scaffold click
            keyboardController?.hide()
            focusManager.clearFocus()
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            BackSkipTopBar(navigate = {
                keyboardController?.hide()
                navigateBack()
            })
        },
        content = { padding ->
            ForgotPasswordContent(
                padding = padding,
                sendPasswordResetEmail = {
                    showSnackBar = true
                    forgotPasswordViewModel.sendPasswordResetEmail()
                },
                forgotPasswordState = forgotPasswordState,
                onEvent = {
                    forgotPasswordViewModel.onEvent(it)
                },
                successValidation = successValidation
            )
        }
    )

    // Function to show the SnackBar with a specified message
    fun showSnackBar(message: String) = coroutineScope.launch {
        if (showSnackBar) {
            snackBarHostState.showSnackbar(message)
            showSnackBar = false
        }
    }

    // ForgotPassword composable responsible for navigation and showing SnackBars
    ForgotPassword(
        onSuccess = {
            forgotPasswordViewModel.resetSendResetEmailStateFlow()
            navigateToEmailSentScreen()
        },
        showSnackBar = { errorMessage ->
            showSnackBar(errorMessage)
        }
    )
}