package com.mvrc.viewapp.presentation.edit_profile

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.presentation.add_post.AddPostViewModel
import com.mvrc.viewapp.presentation.components.buttons.ViewTextButton
import com.mvrc.viewapp.presentation.components.top_app_bars.NameBackTopBar
import com.mvrc.viewapp.presentation.edit_profile.components.EditProfileTextField
import com.mvrc.viewapp.presentation.edit_profile.components.ScrollableMultiSelectionTopics
import com.mvrc.viewapp.presentation.edit_profile.components.UpdateProfileImage
import com.mvrc.viewapp.presentation.theme.UiConstants.HOME_CONTENT_TOP_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansSemiBold
import com.mvrc.viewapp.presentation.theme.ViewGray

/**
 * [Composable] function representing the content of the Add Profile screen.
 *
 * @param padding The Scaffold padding values.
 * @param navigateBack Callback to navigate back to the previous screen.
 * @param saveChanges Callback function used to save the user data updates.
 * @param areTheRequiredFieldsCompleted Boolean indicating if the "Save button" can be enabled or not.
 * @param usernameInputText String representing the content of the Username Text Field.
 * @param onUsernameChange Callback executed on Username Text Field value change.
 * @param professionInputText String representing the content of the Profession Text Field.
 * @param onProfessionChange Callback executed on Profession Text Field value change.
 * @param topics The List of the App topics.
 * @param onTopicSelection Callback executed when a new Topic is selected.
 * @param selectedTopics The List of the selected topics initialized with the current User topics.
 * @param onImageSelection Callback to save the selected image inside the [AddPostViewModel].
 * @param oldUserProfileImage The current user profile image url.
 */
@Composable
fun EditProfileContent(
    padding: PaddingValues,
    navigateBack: () -> Unit,
    saveChanges: () -> Unit,
    areTheRequiredFieldsCompleted: Boolean,
    usernameInputText: String,
    onUsernameChange: (String) -> Unit,
    professionInputText: String,
    onProfessionChange: (String) -> Unit,
    topics: List<String>,
    selectedTopics: List<String>,
    onTopicSelection: (String) -> Unit,
    onImageSelection: (Uri?) -> Unit,
    oldUserProfileImage: String
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(
                start = 40.rw(),
                end = 40.rw(),
                top = HOME_CONTENT_TOP_PADDING.rh()
            )
            .fillMaxSize(),
    ) {
        // Top Bar
        NameBackTopBar(
            title = stringResource(id = R.string.edit_profile),
            showBackIcon = true,
            navigateBack = navigateBack
        )

        Spacer(modifier = Modifier.height(50.rh()))

        UpdateProfileImage(currentProfileImage = oldUserProfileImage) {
            onImageSelection(it)
        }

        Spacer(modifier = Modifier.height(40.rh()))

        // Personal Info...
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(id = R.string.personal_info),
                style = MaterialTheme.typography.titleSmall.copy(fontFamily = openSansSemiBold),
            )
        }

        Spacer(modifier = Modifier.height(30.rh()))

        // Username
        EditProfileTextField(
            label = stringResource(id = R.string.username),
            textStyle = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.secondary,
                fontFamily = openSansSemiBold
            ),
            labelStyle = MaterialTheme.typography.headlineSmall.copy(color = ViewGray),
            value = usernameInputText,
            onValueChange = { inputText -> onUsernameChange(inputText) }
        )

        Spacer(modifier = Modifier.height(20.rh()))

        // Profession
        EditProfileTextField(
            label = stringResource(id = R.string.profession),
            textStyle = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.secondary,
                fontFamily = openSansSemiBold
            ),
            labelStyle = MaterialTheme.typography.headlineSmall.copy(color = ViewGray),
            value = professionInputText,
            onValueChange = { inputText -> onProfessionChange(inputText) }
        )

        Spacer(modifier = Modifier.height(20.rh()))

        // Topics
        ScrollableMultiSelectionTopics(
            topics = topics,
            selectedTopics = selectedTopics
        ) { selectedTopic ->
            onTopicSelection(selectedTopic)
        }

        Spacer(modifier = Modifier.height(60.rh()))

        // Save Button
        ViewTextButton(
            textContent = stringResource(id = R.string.save),
            enabled = areTheRequiredFieldsCompleted
        ) {
            keyboardController?.hide()
            saveChanges()
        }
    }
}