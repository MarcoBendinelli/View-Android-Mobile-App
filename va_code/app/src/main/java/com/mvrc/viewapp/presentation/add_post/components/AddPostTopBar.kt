package com.mvrc.viewapp.presentation.add_post.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mvrc.viewapp.R
import com.mvrc.viewapp.presentation.components.buttons.BackIcon
import com.mvrc.viewapp.presentation.components.buttons.ViewClickableText
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansSemiBold
import com.mvrc.viewapp.presentation.theme.ViewBlue

/**
 * [Composable] for rendering a custom top bar with a back button on the left
 * and a publish now button on the right. The publish now button is enabled
 * whenever all the required fields requirements are met.
 *
 * @param onPublishNowTap The callback function when the publish now button is clicked.
 * @param onBackTap The callback function when the back button is clicked.
 * @param areTheRequiredFieldsCompleted Boolean indicating if the "Publish button" can be enabled or not.
 */
@Composable
fun AddPostTopBar(
    onPublishNowTap: () -> Unit,
    onBackTap: () -> Unit,
    areTheRequiredFieldsCompleted: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Icon
        BackIcon {
            onBackTap()
        }

        // Publish Now Button
        ViewClickableText(
            text = stringResource(id = R.string.publish_now),
            style = MaterialTheme.typography.labelMedium.copy(
                color = ViewBlue,
                fontFamily = openSansSemiBold
            ),
            isEnabled = areTheRequiredFieldsCompleted
        ) {
            onPublishNowTap()
        }
    }
}