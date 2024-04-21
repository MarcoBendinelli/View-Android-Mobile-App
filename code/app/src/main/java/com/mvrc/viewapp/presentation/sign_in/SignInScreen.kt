package com.mvrc.viewapp.presentation.sign_in

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
import com.mvrc.viewapp.presentation.sign_in.observers.SignIn
import com.mvrc.viewapp.presentation.sign_in.view_model.SignInViewModel
import kotlinx.coroutines.launch

/**
 * [Composable] representing the Sign In screen.
 *
 * @param signInViewModel The ViewModel responsible for the Sign In screen logic.
 * @param navigateToForgotPasswordScreen Callback to navigate to the Forgot Password screen.
 * @param navigateBack Callback to navigate back to the previous screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    signInViewModel: SignInViewModel = hiltViewModel(),
    navigateToForgotPasswordScreen: () -> Unit,
    navigateBack: () -> Unit,
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
    val signInFormState = signInViewModel.signInFormStateFlow.collectAsState().value
    // State related to the success validation
    var successValidation by rememberSaveable {
        mutableStateOf(false)
    }

    // Collect validation events from the ViewModel
    LaunchedEffect(key1 = context) {
        signInViewModel.validationEvents.collect { event ->
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
            SignInContent(
                padding = padding,
                signIn = {
                    showSnackBar = true
                    signInViewModel.signInWithEmailAndPassword()
                },
                navigateToForgotPasswordScreen = navigateToForgotPasswordScreen,
                signInFormState = signInFormState,
                onEvent = {
                    signInViewModel.onEvent(it)
                },
                successValidation = successValidation
            )
        }
    )

    // Function to show a snackBar
    fun showSnackBar(message: String) = coroutineScope.launch {
        if (showSnackBar) {
            snackBarHostState.showSnackbar(message)
            showSnackBar = false
        }
    }

    SignIn(
        showSnackBar = {
            showSnackBar(it)
        }
    )
}