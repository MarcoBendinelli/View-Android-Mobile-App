package com.mvrc.viewapp.presentation.edit_profile

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvrc.viewapp.presentation.edit_profile.observers.UpdateProfile
import kotlinx.coroutines.launch

/**
 * [Composable] representing the Edit Profile screen allowing the user to modify the profile data.
 *
 * @param navigateBack Callback to navigate back to the previous screen.
 * @param editProfileViewModel View model containing the states for the Edit Profile screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navigateBack: () -> Unit,
    editProfileViewModel: EditProfileViewModel = hiltViewModel()
) {

    // SnackBar related observers: showSnackBar state necessary to not show 2 times
    // the snackBar on configuration changes
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showSnackBar by rememberSaveable { mutableStateOf(false) }

    // Function to show a snackBar
    fun showSnackBar(message: String) = coroutineScope.launch {
        if (showSnackBar) {
            snackBarHostState.showSnackbar(message)
            showSnackBar = false
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        content = { padding ->
            EditProfileContent(
                padding = padding,
                navigateBack = navigateBack,
                areTheRequiredFieldsCompleted = editProfileViewModel.areRequiredFieldsCompletedStateFlow.collectAsState().value,
                usernameInputText = editProfileViewModel.usernameStateFlow.collectAsState().value,
                onUsernameChange = { editProfileViewModel.onUsernameChange(it) },
                professionInputText = editProfileViewModel.professionStateFlow.collectAsState().value,
                onProfessionChange = { editProfileViewModel.onProfessionChange(it) },
                topics = editProfileViewModel.getTopics(),
                onTopicSelection = { editProfileViewModel.onTopicSelection(it) },
                selectedTopics = editProfileViewModel.selectedTopicsStateFlow.collectAsState().value,
                onImageSelection = { editProfileViewModel.selectImage(it) },
                oldUserProfileImage = editProfileViewModel.userStateFlow.collectAsState().value.photoUrl,
                saveChanges = {
                    showSnackBar = true
                    editProfileViewModel.updateUserProfileData()
                }
            )
        },
    )

    UpdateProfile(showSnackBar = { showSnackBar(it) })
}