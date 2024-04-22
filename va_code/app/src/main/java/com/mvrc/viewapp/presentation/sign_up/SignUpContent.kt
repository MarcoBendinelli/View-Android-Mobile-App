package com.mvrc.viewapp.presentation.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rT
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.presentation.components.TextFieldWithTitleAndError
import com.mvrc.viewapp.presentation.components.buttons.ViewIconButton
import com.mvrc.viewapp.presentation.components.buttons.ViewTextButton
import com.mvrc.viewapp.presentation.sign_up.view_model.SignUpFormEvent
import com.mvrc.viewapp.presentation.sign_up.view_model.SignUpFormState
import com.mvrc.viewapp.presentation.theme.UiConstants.AUTH_CONTENT_H_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.AUTH_CONTENT_TOP_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.AUTH_CONTENT_V_PADDING

/**
 * [Composable] for displaying the content of the Sign Up screen.
 *
 * @param padding The padding values for the content.
 * @param onEvent Callback to handle Sign Up form events.
 * @param signUp Callback to initiate the Sign Up process.
 * @param signUpFormState The current state of the Sign Up form.
 * @param successValidation Boolean indicating whether the form validation was successful.
 */
@Composable
@ExperimentalComposeUiApi
fun SignUpContent(
    padding: PaddingValues,
    onEvent: (SignUpFormEvent) -> Unit,
    signUp: () -> Unit,
    signUpFormState: SignUpFormState,
    successValidation: Boolean,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    // Focus requester to change the focus on the password text field
    val passwordFocusRequest = remember { FocusRequester() }
    // Focus requester to change the focus on the repeated password text field
    val repeatedPasswordFocusRequest = remember { FocusRequester() }
    // State to manage password visibility
    var isPasswordHidden by rememberSaveable {
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(
                start = AUTH_CONTENT_H_PADDING.rw(),
                end = AUTH_CONTENT_H_PADDING.rw(),
                top = AUTH_CONTENT_TOP_PADDING.rh(),
                bottom = AUTH_CONTENT_V_PADDING.rh()
            ), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start
    ) {
        // "Sign Up Now" title
        Text(
            text = stringResource(id = R.string.sign_up_now),
            style = MaterialTheme.typography.displaySmall.copy(
                color = MaterialTheme.colorScheme.secondary,
                letterSpacing = 0.48.rT()
            ),
        )

        Spacer(modifier = Modifier.height(16.rh()))

        // Subtitle
        Text(
            text = stringResource(id = R.string.sign_up_to_see),
            style = MaterialTheme.typography.titleSmall.copy(
                letterSpacing = 0.14.rT()
            ),
        )

        Spacer(modifier = Modifier.height(50.rh()))

        // Email Text Field
        TextFieldWithTitleAndError(
            modifier = Modifier.testTag("emailField"),
            title = stringResource(id = R.string.email),
            label = stringResource(id = R.string.enter_email),
            value = signUpFormState.email,
            onValueChange = {
                onEvent(SignUpFormEvent.EmailChanged(it))
            },
            error = signUpFormState.emailError,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            onAction = KeyboardActions {
                // On action change the focus to the password text field
                passwordFocusRequest.requestFocus()
            })

        Spacer(modifier = Modifier.height(24.rh()))

        // Password Text Field
        TextFieldWithTitleAndError(
            modifier = Modifier
                .focusRequester(passwordFocusRequest)
                .testTag("passwordField"),
            title = stringResource(id = R.string.password),
            label = stringResource(id = R.string.enter_password),
            value = signUpFormState.password,
            onValueChange = {
                // On action change the focus to the repeated password text field
                onEvent(SignUpFormEvent.PasswordChanged(it))
            },
            error = signUpFormState.passwordError,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next,
            onAction = KeyboardActions {
                repeatedPasswordFocusRequest.requestFocus()
            },
            trailingIcon = {
                ViewIconButton(
                    size = 24,
                    iconId = if (isPasswordHidden) R.drawable.icon_pass_hidden else R.drawable.icon_pass_visible
                ) {
                    isPasswordHidden = !isPasswordHidden
                }
            },
            applyPasswordVisualTransformation = isPasswordHidden
        )

        Spacer(modifier = Modifier.height(24.rh()))

        // Repeated Password Text Field
        TextFieldWithTitleAndError(
            modifier = Modifier
                .focusRequester(repeatedPasswordFocusRequest)
                .testTag("repeatedPasswordField"),
            title = stringResource(id = R.string.confirm_password),
            label = stringResource(id = R.string.enter_confirm_password),
            value = signUpFormState.repeatedPassword,
            onValueChange = {
                onEvent(SignUpFormEvent.RepeatedPasswordChanged(it))
            },
            error = signUpFormState.repeatedPasswordError,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            trailingIcon = {
                ViewIconButton(
                    size = 24,
                    iconId = if (isPasswordHidden) R.drawable.icon_pass_hidden else R.drawable.icon_pass_visible
                ) {
                    isPasswordHidden = !isPasswordHidden
                }
            },
            applyPasswordVisualTransformation = isPasswordHidden
        )

        Spacer(modifier = Modifier.height(30.rh()))

        // Sign Up button
        ViewTextButton(
            modifier = Modifier.testTag("signUpButton"),
            textContent = stringResource(id = R.string.sign_up),
            enabled = successValidation
        ) {
            keyboardController?.hide()
            signUp()
        }
    }
}