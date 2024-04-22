package com.mvrc.viewapp.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw

/**
 * [Composable] representing an app contributor with a profile image, name, and profession.
 *
 * This function is used to display information about contributors, including their
 * profile image, name, and profession. It provides customization options for
 * styling the name and profession text.
 *
 * @param modifier The [Modifier] to customize the component.
 * @param name The name of the contributor.
 * @param nameTextStyle The [TextStyle] for the contributor's name.
 * @param bodyContributor The body text of the Contributor information.
 * @param professionTextStyle The [TextStyle] for the contributor's profession.
 * @param avatarSize The size of the user_profile image specified as the radius in logical pixels.
 * @param profileIconSize The size of the icon that is shown when the user_profile has not a profile image.
 * @param photoUrl The URL of the contributor's profile photo.
 * @param spaceBetweenAvatarAndText The space between the avatar and the texts.
 * @param spaceBetweenTexts The space between the texts.
 * @param isEnabled Boolean flag indicating whether the clickable behaviour of the avatar is enabled.
 * @param onUserAvatarClick The function for navigating to the specific User Screen.
 */
@Composable
fun ViewContributor(
    modifier: Modifier = Modifier,
    name: String,
    nameTextStyle: TextStyle,
    bodyContributor: String,
    professionTextStyle: TextStyle,
    photoUrl: String,
    avatarSize: Int,
    spaceBetweenAvatarAndText: Int,
    spaceBetweenTexts: Int = 0,
    profileIconSize: Int,
    isEnabled: Boolean,
    onUserAvatarClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile image
        ViewAvatar(
            photoUrl = photoUrl,
            size = avatarSize,
            iconSize = profileIconSize,
            isEnabled = isEnabled
        ) {
            onUserAvatarClick()
        }

        Spacer(modifier = Modifier.width(spaceBetweenAvatarAndText.rw()))

        // Name and Profession
        Column(
            modifier = Modifier
                .fillMaxHeight(),
        ) {
            Text(
                text = name,
                style = nameTextStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(spaceBetweenTexts.rh()))
            Text(
                text = bodyContributor,
                style = professionTextStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
