package com.mvrc.viewapp.presentation.edit_profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvrc.viewapp.data.cache.ViewUserCache
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.model.ViewUser
import com.mvrc.viewapp.domain.repository.FirstUserSelectionRepository
import com.mvrc.viewapp.domain.repository.UpdateUserResponse
import com.mvrc.viewapp.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for handling the Add Post screen state.
 *
 * @property firstUserSelectionRepository The Repository used to retrieve the app filters.
 * @property userProfileRepository The Repository that handles the user operations.
 * @property viewUserCache The cache to update the user_profile data.
 */
@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val firstUserSelectionRepository: FirstUserSelectionRepository,
    private val userProfileRepository: UserProfileRepository,
    private val viewUserCache: ViewUserCache,
) : ViewModel() {

    private val _usernameStateFlow =
        MutableStateFlow(viewUserCache.data.username)
    val usernameStateFlow = _usernameStateFlow.asStateFlow()

    private val _professionStateFlow =
        MutableStateFlow(viewUserCache.data.profession)
    val professionStateFlow = _professionStateFlow.asStateFlow()

    // Selected Topics initialized with the current user topics
    private val _selectedTopicsStateFlow =
        MutableStateFlow(viewUserCache.data.topics)
    val selectedTopicsStateFlow = _selectedTopicsStateFlow.asStateFlow()

    private var _selectedImageUri: Uri? = null

    private val _areRequiredFieldsCompletedStateFlow =
        MutableStateFlow(_usernameStateFlow.value.isNotEmpty() && _professionStateFlow.value.isNotEmpty())
    val areRequiredFieldsCompletedStateFlow = _areRequiredFieldsCompletedStateFlow.asStateFlow()

    // Mutable state representing the response of the Profile Updating operation.
    private val _updateProfileResponseStateFlow = MutableStateFlow<UpdateUserResponse>(
        Success(false)
    )
    val updateProfileResponseStateFlow = _updateProfileResponseStateFlow.asStateFlow()

    /**
     * Exposes a read-only [SharedFlow] of [ViewUser] updates from the cache.
     */
    val userStateFlow: StateFlow<ViewUser> get() = viewUserCache.userUpdates

    /**
     * Update user data by saving it on backend.
     */
    fun updateUserProfileData() = viewModelScope.launch {
        _updateProfileResponseStateFlow.value = Loading

        _updateProfileResponseStateFlow.value = userProfileRepository.updateProfile(
            username = _usernameStateFlow.value,
            profession = _professionStateFlow.value,
            topics = _selectedTopicsStateFlow.value,
            photoUrl = _selectedImageUri
        )
    }

    /**
     * Updates the corresponding [MutableStateFlow] with the provided input text.
     *
     * @param username The text input provided by the user.
     */
    fun onUsernameChange(username: String) {
        _usernameStateFlow.value = username
        checkRequiredFieldsCompletion()
    }

    /**
     * Updates the corresponding [MutableStateFlow] with the provided input text.
     *
     * @param profession The text input provided by the user.
     */
    fun onProfessionChange(profession: String) {
        _professionStateFlow.value = profession
        checkRequiredFieldsCompletion()
    }

    /**
     * Updates the corresponding [MutableStateFlow] with the provided topic.
     *
     * @param topic The topic selected by the user_profile.
     */
    fun onTopicSelection(topic: String) {
        _selectedTopicsStateFlow.value = _selectedTopicsStateFlow.value.toMutableList().apply {
            if (contains(topic)) remove(topic) else add(topic)
        }
    }

    /**
     * Updates the selected image URI in the ViewModel.
     *
     * @param imageUri The URI of the selected image. It can be null if no image is selected.
     */
    fun selectImage(imageUri: Uri?) {
        _selectedImageUri = imageUri
    }

    /**
     * Checks the completion of the required fields and updates the corresponding state.
     *
     * The completion status is determined based on the emptiness of the username and profession text.
     */
    private fun checkRequiredFieldsCompletion() {
        _areRequiredFieldsCompletedStateFlow.value =
            !(_usernameStateFlow.value.isEmpty() || _professionStateFlow.value.isEmpty())
    }

    /**
     * Retrieves the list of available topics from the repository.
     *
     * @return A list of [String] objects representing available topics.
     */
    fun getTopics(): List<String> =
        firstUserSelectionRepository.getTopics().map { viewTopic -> viewTopic.topicName }.sorted()
}