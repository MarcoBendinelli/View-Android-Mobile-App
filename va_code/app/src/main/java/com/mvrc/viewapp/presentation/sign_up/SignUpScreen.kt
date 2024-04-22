package com.mvrc.viewapp.presentation.sign_up

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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvrc.viewapp.AppViewModel
import com.mvrc.viewapp.domain.event.ValidationEvent
import com.mvrc.viewapp.presentation.components.top_app_bars.BackSkipTopBar
import com.mvrc.viewapp.presentation.sign_up.observers.SignUp
import com.mvrc.viewapp.presentation.sign_up.view_model.SignUpViewModel
import kotlinx.coroutines.launch

/**
 * [Composable] representing the Sign Up screen.
 *
 * @param signUpViewModel The ViewModel responsible for the Sign Up screen logic.
 * @param navigateBack Callback to navigate back to the previous screen.
 * @param appViewModel The ViewModel responsible for the app state.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@ExperimentalComposeUiApi
fun SignUpScreen(
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    appViewModel: AppViewModel,
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
    val signUpFormState = signUpViewModel.signUpFormStateFlow.collectAsState().value
    // State related to the success validation
    var successValidation by rememberSaveable {
        mutableStateOf(false)
    }

    // Collect validation events from the ViewModel
    LaunchedEffect(key1 = context) {
        signUpViewModel.validationEvents.collect { event ->
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
            keyboardController?.hide()
            focusManager.clearFocus()
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            BackSkipTopBar(navigate = {
                // Hide keyboard and clear focus on scaffold click
                keyboardController?.hide()
                navigateBack()
            })
        },
        content = { padding ->
            SignUpContent(
                padding = padding,
                signUp = {
                    showSnackBar = true
                    // Wait to redirect the user_profile to the Home Page
                    appViewModel.notRedirectUser()
                    signUpViewModel.signUpWithEmailAndPassword()
                },
                signUpFormState = signUpFormState,
                onEvent = {
                    signUpViewModel.onEvent(it)
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

    SignUp(
        showSnackBar = {
            showSnackBar(it)
        },
        onSuccess = {
            // Redirect the user_profile to the selection screen
            appViewModel.onStartUserSelection()
            // Enable redirection to the Home Page
            appViewModel.enableUserRedirection()
        },
        onFailure = {
            // Reset the app status
            appViewModel.enableUserRedirection()
        }
    )
}