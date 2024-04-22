package com.mvrc.viewapp.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.presentation.components.buttons.ViewClickableText
import com.mvrc.viewapp.presentation.components.top_app_bars.NameBackTopBar
import com.mvrc.viewapp.presentation.settings.components.SettingsRow
import com.mvrc.viewapp.presentation.theme.UiConstants.HOME_CONTENT_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.HOME_CONTENT_TOP_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansSemiBold
import com.mvrc.viewapp.presentation.theme.ViewRed

@Composable
fun SettingsContent(
    signOut: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToEditProfileScreen: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(
                start = HOME_CONTENT_PADDING.rw(),
                end = HOME_CONTENT_PADDING.rw(),
                top = HOME_CONTENT_TOP_PADDING.rh()
            )
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        // Top Bar
        NameBackTopBar(
            title = stringResource(id = R.string.settings),
            showBackIcon = false,
        )

        Spacer(
            modifier = Modifier.height(40.rh())
        )

        SettingsRow(settingName = stringResource(id = R.string.edit_profile)) {
            navigateToEditProfileScreen()
        }

        Divider(modifier = Modifier.padding(vertical = 20.rh()))

        ViewClickableText(
            modifier = Modifier.testTag("logoutButton"),
            text = stringResource(id = R.string.log_out),
            style = MaterialTheme.typography.labelMedium.copy(
                fontFamily = openSansSemiBold,
                color = ViewRed
            )
        ) {
            signOut()
        }

    }
}