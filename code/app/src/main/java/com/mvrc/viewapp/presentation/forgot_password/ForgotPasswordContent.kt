package com.mvrc.viewapp.presentation.forgot_password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.mvrc.viewapp.presentation.components.buttons.ViewTextButton
import com.mvrc.viewapp.presentation.forgot_password.view_model.ForgotPasswordEvent
import com.mvrc.viewapp.presentation.forgot_password.view_model.ForgotPasswordState
import com.mvrc.viewapp.presentation.theme.UiConstants.AUTH_CONTENT_H_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.AUTH_CONTENT_TOP_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.AUTH_CONTENT_V_PADDING

/**
 * [Composable] representing the content of the Forgot Password screen.
 *
 * @param padding Padding values for the screen content.
 * @param sendPasswordResetEmail Callback to initiate the process of sending a password reset email.
 * @param onEvent Callback to handle events specific to the Forgot Password screen.
 * @param forgotPasswordState State representing the current state of the Forgot Password screen.
 * @param successValidation Flag indicating whether the input validation was successful.
 */
@Composable
fun ForgotPasswordContent(
    padding: PaddingValues,
    sendPasswordResetEmail: () -> Unit,
    onEvent: (ForgotPasswordEvent) -> Unit,
    forgotPasswordState: ForgotPasswordState,
    successValidation: Boolean,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

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
        // Title
        Text(
            text = stringResource(id = R.string.reset_password),
            style = MaterialTheme.typography.displaySmall.copy(
                color = MaterialTheme.colorScheme.secondary,
                letterSpacing = 0.48.rT()
            ),
        )

        Spacer(modifier = Modifier.height(16.rh()))

        // Body
        Text(
            text = stringResource(id = R.string.reset_pss_body),
            style = MaterialTheme.typography.titleMedium.copy(
                letterSpacing = 0.14.rT()
            ),
        )

        Spacer(modifier = Modifier.height(60.rh()))

        // Email Text Field
        TextFieldWithTitleAndError(
            modifier = Modifier.testTag("emailField"),
            title = stringResource(id = R.string.email),
            label = stringResource(id = R.string.enter_email),
            value = forgotPasswordState.email,
            onValueChange = {
                onEvent(ForgotPasswordEvent.EmailChanged(it))
            },
            error = forgotPasswordState.emailError,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        )

        Spacer(modifier = Modifier.height(30.rh()))

        // Reset Password button
        ViewTextButton(
            modifier = Modifier.testTag("resetButton"),
            textContent = stringResource(id = R.string.reset_password), enabled = successValidation
        ) {
            keyboardController?.hide()
            sendPasswordResetEmail()
        }
    }
}