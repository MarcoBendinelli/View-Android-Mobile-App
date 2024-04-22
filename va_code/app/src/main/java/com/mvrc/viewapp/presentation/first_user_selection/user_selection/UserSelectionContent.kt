package com.mvrc.viewapp.presentation.first_user_selection.user_selection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.presentation.components.ViewContributor
import com.mvrc.viewapp.presentation.components.buttons.FollowButton
import com.mvrc.viewapp.presentation.first_user_selection.observers.Users
import com.mvrc.viewapp.presentation.theme.UiConstants
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansRegular
import com.mvrc.viewapp.presentation.theme.ViewBlue

/**
 * [Composable] representing the content of the User Selection screen.
 *
 * @param padding Padding values for layout customization.
 * @param selectedUsers The list of currently selected users ids.
 * @param loadUsers Callback to initially load the most followed users.
 * @param saveUser Function to save the selected user_profile on backend.
 */
@Composable
fun UserSelectionContent(
    padding: PaddingValues,
    selectedUsers: List<String>,
    loadUsers: () -> Unit,
    saveUser: (String) -> Unit
) {
    // Fetch users at the startup
    LaunchedEffect(key1 = true) {
        loadUsers()
    }

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(
                start = UiConstants.AUTH_CONTENT_H_PADDING.rw(),
                end = UiConstants.AUTH_CONTENT_H_PADDING.rw(),
                top = 30.rh()
            ),
    ) {

        // Title
        Text(
            text = stringResource(id = R.string.follow_contributors),
            style = MaterialTheme.typography.displaySmall.copy(
                color = MaterialTheme.colorScheme.secondary
            ),
        )

        Spacer(modifier = Modifier.height(8.rh()))

        // Body
        Text(
            text = stringResource(id = R.string.follow_contributors_body),
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = Modifier.height(30.rh()))

        // Contributors
        Box(
            modifier = Modifier.height(500.rh())
        ) {
            Users { users ->
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(20.rh())
                ) {
                    if (users.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.no_user),
                                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.secondary),
                                )
                            }
                        }
                    }
                    items(users) { user ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Contributor(
                                selectedUsers = selectedUsers,
                                id = user.id,
                                name = user.username,
                                profession = user.profession,
                                photoUrl = user.photoUrl
                            ) {
                                saveUser(user.id)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * [Composable] for displaying contributor information and follow button.
 *
 * @param selectedUsers The current selected users ids.
 * @param id Contributor's id.
 * @param name Contributor's name.
 * @param profession Contributor's profession.
 * @param photoUrl Contributor's photo url.
 * @param onClick Lambda function to be executed when the follow button is clicked.
 */
@Composable
fun Contributor(
    selectedUsers: List<String>,
    id: String,
    name: String,
    profession: String,
    photoUrl: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // User information
        ViewContributor(
            modifier = Modifier.weight(1f),
            name = name,
            nameTextStyle = MaterialTheme.typography.headlineSmall.copy(
                fontFamily = UiConstants.openSansSemiBold,
                color = MaterialTheme.colorScheme.secondary
            ),
            bodyContributor = profession,
            professionTextStyle = MaterialTheme.typography.titleSmall,
            photoUrl = photoUrl,
            avatarSize = 28,
            spaceBetweenAvatarAndText = 16,
            profileIconSize = 22,
            isEnabled = false
        ) {}

        Spacer(modifier = Modifier.width(8.rw()))

        // Follow the contributor button
        FollowButton(
            widthButton = 80,
            heightButton = 40,
            sizeShape = 12,
            style = MaterialTheme.typography.labelLarge.copy(
                color = ViewBlue,
                fontFamily = openSansRegular
            ),
            enabled = selectedUsers.contains(id)
        ) {
            onClick()
        }
    }
}