package com.mvrc.viewapp.presentation.first_user_selection

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.model.ViewTopic
import com.mvrc.viewapp.domain.repository.FetchingUsersResponse
import com.mvrc.viewapp.domain.repository.FirstUserSelectionRepository
import com.mvrc.viewapp.domain.repository.SaveTopicsResponse
import com.mvrc.viewapp.domain.repository.SaveUsersResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model responsible for handling the user_profile's selection of topics and contributors
 * during the first user_profile experience.
 *
 * @property firstUserSelectionRepository Repository for retrieving and adding topics and contributors.
 */
@HiltViewModel
class FirstUserSelectionViewModel @Inject constructor(
    private val firstUserSelectionRepository: FirstUserSelectionRepository,
) : ViewModel() {

    // State list to store the selected topics.
    private val _selectedTopics = mutableStateListOf<ViewTopic>()
    val selectedTopics: SnapshotStateList<ViewTopic> get() = _selectedTopics

    // State list to store the selected users.
    private val _selectedUsersIds = mutableStateListOf<String>()
    val selectedUsersIds: SnapshotStateList<String> get() = _selectedUsersIds

    // Represents the response from the users fetching operation.
    private val _usersFetchingResponseStateFlow = MutableStateFlow<FetchingUsersResponse>(Loading)
    val usersFetchingResponseStateFlow = _usersFetchingResponseStateFlow.asStateFlow()

    // Represents the response from the users save operation.
    private val _saveUsersResponseStateFlow = MutableStateFlow<SaveUsersResponse>(Success(false))
    val saveUsersResponseStateFlow = _saveUsersResponseStateFlow.asStateFlow()

    // Represents the response from the topics save operation.
    private val _saveTopicsResponseStateFlow = MutableStateFlow<SaveTopicsResponse>(Success(false))
    val saveTopicsResponseStateFlow = _saveTopicsResponseStateFlow.asStateFlow()

    /**
     * Retrieves the list of available topics from the repository.
     *
     * @return A list of [ViewTopic] objects representing available topics.
     */
    fun getTopics(): List<ViewTopic> = firstUserSelectionRepository.getTopics()

    /**
     * Handles the selection or deselection of a topic.
     *
     * @param topic The selected or deselected topic.
     */
    fun handleTopicSelection(topic: ViewTopic) {
        if (!_selectedTopics.contains(topic)) {
            addSelectedTopic(topic)
        } else {
            removeSelectedTopic(topic)
        }
    }

    /**
     * Resets the selected topics by clearing the set.
     */
    fun resetSelectedTopics() {
        _selectedTopics.clear()
    }

    /**
     * Save the selected topics on backend and update the corresponded [MutableStateFlow].
     */
    fun saveTopics() = viewModelScope.launch {
        _saveTopicsResponseStateFlow.value =
            firstUserSelectionRepository.saveTopics(_selectedTopics)
    }

    /**
     * Fetches the most followed users and updates the corresponded [MutableStateFlow].
     */
    fun getUsers() = viewModelScope.launch {
        firstUserSelectionRepository.getMostFollowedUsers().collect { response ->
            _usersFetchingResponseStateFlow.value = response
        }
    }

    /**
     * Handles the selection or deselection of a user_profile, save the current selected users
     * on backend and updates the corresponded [MutableStateFlow].
     *
     * @param selectedUserId The selected or deselected user_profile id.
     */
    fun saveAndHandleUserSelection(selectedUserId: String) = viewModelScope.launch {
        if (!_selectedUsersIds.contains(selectedUserId)) {
            addSelectedUser(selectedUserId)
        } else {
            removeSelectedUser(selectedUserId)
        }
        firstUserSelectionRepository.saveUser(selectedUserId)
    }

    /**
     * Adds the specified topic to the list of selected topics.
     *
     * @param topic The topic to be added.
     */
    private fun addSelectedTopic(topic: ViewTopic) {
        _selectedTopics.add(topic)
    }

    /**
     * Removes the specified topic from the list of selected topics.
     *
     * @param topic The topic to be removed.
     */
    private fun removeSelectedTopic(topic: ViewTopic) {
        _selectedTopics.remove(topic)
    }

    /**
     * Adds the specified user_profile id to the list of selected users.
     *
     * @param userId The user_profile id to be added.
     */
    private fun addSelectedUser(userId: String) {
        _selectedUsersIds.add(userId)
    }

    /**
     * Removes the specified user_profile id from the list of selected users.
     *
     * @param userId The user_profile id to be removed.
     */
    private fun removeSelectedUser(userId: String) {
        _selectedUsersIds.remove(userId)
    }

    /**
     * It resets the [MutableStateFlow] to its initial value.
     */
    fun resetSaveTopicsStateFlow() {
        _saveTopicsResponseStateFlow.value = Success(false)
    }
}