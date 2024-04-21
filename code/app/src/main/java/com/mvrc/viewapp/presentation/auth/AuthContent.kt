package com.mvrc.viewapp.presentation.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.core.SignInOption
import com.mvrc.viewapp.presentation.auth.components.ContinueWithButton
import com.mvrc.viewapp.presentation.components.ViewLogo
import com.mvrc.viewapp.presentation.components.buttons.ViewClickableText
import com.mvrc.viewapp.presentation.theme.UiConstants.AUTH_CONTENT_H_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.AUTH_CONTENT_V_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansSemiBold

/**
 * [Composable] for rendering the content of the authentication screen.
 *
 * @param padding Padding values to apply to the content.
 * @param oneTapSignIn Callback function to handle one-tap sign-in button click.
 * @param navigateToSignInScreen Callback function to navigate to the sign-in screen.
 * @param navigateToSignUpScreen Callback function to navigate to the sign-up screen.
 */
@Composable
fun AuthContent(
    padding: PaddingValues,
    oneTapSignIn: () -> Unit,
    navigateToSignInScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AUTH_CONTENT_H_PADDING.rw())
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(140.rh()))

        // App Logo
        ViewLogo()

        Spacer(modifier = Modifier.height(20.rh()))

        // Welcome text
        Text(
            text = stringResource(id = R.string.welcome),
            style = MaterialTheme.typography.displayMedium.copy(
                color = MaterialTheme.colorScheme.secondary
            ),
        )

        Spacer(modifier = Modifier.height(20.rh()))

        // Welcome subtitle text
        Text(
            text = stringResource(id = R.string.welcome_subtitle),
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center
            ),
        )

        Spacer(modifier = Modifier.height(80.rh()))

        // Google sign in
        ContinueWithButton(
            onClick = oneTapSignIn,
            namePlatform = SignInOption.Google
        )

        Spacer(modifier = Modifier.height(16.rh()))

        // Email and Password sign up
        ContinueWithButton(
            modifier = Modifier.testTag("signUpFromAuthButton"),
            onClick = navigateToSignUpScreen,
            namePlatform = SignInOption.Email
        )

        Spacer(modifier = Modifier.height(45.rh()))

        // Email and Password sign in
        Row {
            Text(
                text = stringResource(id = R.string.already_account) + " ",
                style = MaterialTheme.typography.titleSmall,
            )
            ViewClickableText(
                modifier = Modifier.testTag("signInFromAuthButton"),
                text = stringResource(id = R.string.log_in),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontFamily = openSansSemiBold,
                    color = MaterialTheme.colorScheme.primary,
                ),
                textDecoration = TextDecoration.Underline,
            ) {
                navigateToSignInScreen()
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Policy Terms
        Text(
            text = stringResource(id = R.string.policy_terms),
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
            ),
        )
        Spacer(modifier = Modifier.height(AUTH_CONTENT_V_PADDING.rh()))
    }
}