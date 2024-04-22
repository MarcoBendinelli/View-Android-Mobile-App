package com.mvrc.viewapp.presentation.sign_in

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
import com.mvrc.viewapp.presentation.components.buttons.ViewClickableText
import com.mvrc.viewapp.presentation.components.buttons.ViewIconButton
import com.mvrc.viewapp.presentation.components.buttons.ViewTextButton
import com.mvrc.viewapp.presentation.sign_in.view_model.SignInFormEvent
import com.mvrc.viewapp.presentation.sign_in.view_model.SignInFormState
import com.mvrc.viewapp.presentation.theme.UiConstants.AUTH_CONTENT_H_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.AUTH_CONTENT_TOP_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.AUTH_CONTENT_V_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansRegular
import com.mvrc.viewapp.presentation.theme.ViewBlueVariation

/**
 * [Composable] representing the content of the Sign In screen.
 *
 * @param padding Padding values for layout customization.
 * @param onEvent Callback to handle events related to the Sign In form.
 * @param signIn Callback to initiate the Sign In process.
 * @param navigateToForgotPasswordScreen Callback to navigate to the Forgot Password screen.
 * @param signInFormState The current state of the Sign In form.
 * @param successValidation Flag indicating whether the form has passed validation successfully.
 */
@Composable
fun SignInContent(
    padding: PaddingValues,
    onEvent: (SignInFormEvent) -> Unit,
    signIn: () -> Unit,
    navigateToForgotPasswordScreen: () -> Unit,
    signInFormState: SignInFormState,
    successValidation: Boolean,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    // Focus requester to change the focus on the password text field
    val passwordFocusRequest = remember { FocusRequester() }
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
        // "Sign In Now" title
        Text(
            text = stringResource(id = R.string.sign_in_now),
            style = MaterialTheme.typography.displaySmall.copy(
                color = MaterialTheme.colorScheme.secondary,
                letterSpacing = 0.48.rT()
            ),
        )

        Spacer(modifier = Modifier.height(16.rh()))

        // Subtitle
        Text(
            text = stringResource(id = R.string.log_in_to_see),
            style = MaterialTheme.typography.titleSmall.copy(
                letterSpacing = 0.14.rT()
            ),
        )

        Spacer(modifier = Modifier.height(70.rh()))

        // Email Text Field
        TextFieldWithTitleAndError(
            modifier = Modifier.testTag("emailField"),
            title = stringResource(id = R.string.email),
            label = stringResource(id = R.string.enter_email),
            value = signInFormState.email,
            onValueChange = {
                onEvent(SignInFormEvent.EmailChanged(it))
            },
            error = signInFormState.emailError,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            onAction = KeyboardActions {
                // On action change the focus to the password text field
                passwordFocusRequest.requestFocus()
            })

        Spacer(modifier = Modifier.height(24.rh()))

        // Password Text Field
        TextFieldWithTitleAndError(
            modifier = Modifier.focusRequester(passwordFocusRequest).testTag("passwordField"),
            title = stringResource(id = R.string.password),
            label = stringResource(id = R.string.enter_password),
            value = signInFormState.password,
            onValueChange = {
                onEvent(SignInFormEvent.PasswordChanged(it))
            },
            error = signInFormState.passwordError,
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

        Spacer(modifier = Modifier.height(24.rh()))

        // Forgot Password button
        ViewClickableText(
            modifier = Modifier.testTag("signInForgotPasswordButton"),
            text = stringResource(R.string.forgot_password),
            style = MaterialTheme.typography.labelLarge.copy(
                color = ViewBlueVariation,
                fontFamily = openSansRegular
            )
        ) {
            navigateToForgotPasswordScreen()
        }

        Spacer(modifier = Modifier.height(30.rh()))

        // Sign In button
        ViewTextButton(
            modifier = Modifier.testTag("signInButton"),
            textContent = stringResource(id = R.string.sign_in), enabled = successValidation
        ) {
            keyboardController?.hide()
            signIn()
        }
    }
}