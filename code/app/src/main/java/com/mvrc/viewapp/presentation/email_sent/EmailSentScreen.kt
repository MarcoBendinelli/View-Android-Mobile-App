package com.mvrc.viewapp.presentation.email_sent

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.r
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.presentation.components.buttons.ViewTextButton
import com.mvrc.viewapp.presentation.theme.UiConstants.AUTH_CONTENT_H_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansMedium
import com.mvrc.viewapp.presentation.theme.ViewBlue

/**
 * [Composable] for rendering the Email Sent screen.
 *
 * @param navigateToLoginScreen Callback function to navigate to the login screen.
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailSentScreen(
    navigateToLoginScreen: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(
                    top = 110.rh(),
                    start = AUTH_CONTENT_H_PADDING.rw(),
                    end = AUTH_CONTENT_H_PADDING.rw(),
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Surface composable for creating a colored circular background
            Surface(
                modifier = Modifier
                    .width(100.r())
                    .height(100.r()),
                color = ViewBlue,
                shape = CircleShape
            ) {
                // Image composable for displaying the message icon
                Image(
                    painter = painterResource(id = R.drawable.icon_message),
                    contentDescription = "Icon Message",
                    contentScale = ContentScale.None,
                    modifier = Modifier.height(48.rh())
                )
            }

            Spacer(modifier = Modifier.height(70.rh()))

            // Text composable for displaying the email sent message
            Text(
                text = stringResource(id = R.string.email_sent),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.secondary,
            )

            Spacer(modifier = Modifier.height(16.rh()))

            // Text composable for displaying the email sent body
            Text(
                text = stringResource(id = R.string.email_sent_body),
                style = MaterialTheme.typography.titleMedium.copy(
                    textAlign = TextAlign.Center,
                ),
            )

            Spacer(modifier = Modifier.height(30.rh()))

            // Back to login screen button
            ViewTextButton(
                modifier = Modifier.testTag("backToLoginButton"),
                colorContainer = MaterialTheme.colorScheme.tertiary,
                textContent = stringResource(id = R.string.back_to_login), enabled = true,
                widthBorder = 1.dp,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = ViewBlue,
                    fontFamily = openSansMedium
                ),
            ) {
                navigateToLoginScreen()
            }
        }
    }
}