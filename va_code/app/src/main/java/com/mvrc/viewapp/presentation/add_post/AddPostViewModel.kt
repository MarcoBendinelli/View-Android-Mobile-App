package com.mvrc.viewapp.presentation.add_post

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvrc.viewapp.core.Constants.MIN_BODY_WORDS
import com.mvrc.viewapp.core.Constants.MIN_SUBTITLE_WORDS
import com.mvrc.viewapp.core.Constants.MIN_TITLE_WORDS
import com.mvrc.viewapp.core.Utils.estimateReadTime
import com.mvrc.viewapp.core.noEmptyWordsCount
import com.mvrc.viewapp.data.cache.ViewUserCache
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.domain.repository.FirstUserSelectionRepository
import com.mvrc.viewapp.domain.repository.SaveNewPostResponse
import com.mvrc.viewapp.domain.use_case.post.PostUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for handling the Add Post screen state.
 *
 * @property firstUserSelectionRepository The Repository used to retrieve the app filters.
 * @property postUseCases The use case class that encapsulates post-related operations.
 * @property viewUserCache The cache to update the user_profile data.
 */
@HiltViewModel
class AddPostViewModel @Inject constructor(
    private val postUseCases: PostUseCases,
    private val firstUserSelectionRepository: FirstUserSelectionRepository,
    private val viewUserCache: ViewUserCache,
) : ViewModel() {

    private val _titleStateFlow =
        MutableStateFlow("")
    val titleStateFlow = _titleStateFlow.asStateFlow()

    private val _subtitleStateFlow =
        MutableStateFlow("")
    val subtitleStateFlow = _subtitleStateFlow.asStateFlow()

    private val _bodyStateFlow =
        MutableStateFlow("")
    val bodyStateFlow = _bodyStateFlow.asStateFlow()

    // Selected Topic initialized with the first Topic inside the sorted list of topics
    private val _selectedTopicStateFlow =
        MutableStateFlow(
            firstUserSelectionRepository.getTopics().map { viewTopic -> viewTopic.topicName }
                .minOf { it })
    val selectedTopicStateFlow = _selectedTopicStateFlow.asStateFlow()

    private val _areRequiredFieldsCompletedStateFlow =
        MutableStateFlow(false)
    val areRequiredFieldsCompletedStateFlow = _areRequiredFieldsCompletedStateFlow.asStateFlow()

    // Mutable state representing the response of the Add Post operation.
    private val _addPostResponseStateFlow = MutableStateFlow<SaveNewPostResponse>(Success(false))
    val addPostResponseStateFlow = _addPostResponseStateFlow.asStateFlow()

    private var _selectedImageUri: Uri? = null

    /**
     * Updates the corresponding [MutableStateFlow] with the provided input text.
     *
     * @param title The text input provided by the user_profile.
     */
    fun onTitleChange(title: String) {
        _titleStateFlow.value = title
        checkRequiredFieldsCompletion()
    }

    /**
     * Updates the corresponding [MutableStateFlow] with the provided input text.
     *
     * @param title The text input provided by the user_profile.
     */
    fun onSubtitleChange(title: String) {
        _subtitleStateFlow.value = title
        checkRequiredFieldsCompletion()
    }

    /**
     * Updates the corresponding [MutableStateFlow] with the provided input text.
     *
     * @param title The text input provided by the user_profile.
     */
    fun onBodyChange(title: String) {
        _bodyStateFlow.value = title
        checkRequiredFieldsCompletion()
    }

    /**
     * Updates the corresponding [MutableStateFlow] with the provided topic.
     *
     * @param topic The filter selected by the user_profile.
     */
    fun onTopicSelection(topic: String) {
        _selectedTopicStateFlow.value = topic
    }

    /**
     * Checks the completion of the required fields and updates the corresponding state.
     *
     * The completion status is determined based on the number of words of the title, subtitle, and body text.
     * If any of these fields have a length less than the minimum required words, the completion status
     * is set to false; otherwise, it is set to true.
     */
    private fun checkRequiredFieldsCompletion() {
        _areRequiredFieldsCompletedStateFlow.value = !(
                _titleStateFlow.value.noEmptyWordsCount() < MIN_TITLE_WORDS ||
                        _subtitleStateFlow.value.noEmptyWordsCount() < MIN_SUBTITLE_WORDS ||
                        _bodyStateFlow.value.noEmptyWordsCount() < MIN_BODY_WORDS)
    }

    /**
     * Saves the new post on backend.
     */
    fun saveNewPost(context: Context) = viewModelScope.launch {
        _addPostResponseStateFlow.value = Loading

        _addPostResponseStateFlow.value = postUseCases.saveNewPost(
            authorName = viewUserCache.data.username,
            authorImageUrl = viewUserCache.data.photoUrl,
            body = _bodyStateFlow.value,
            imageUri = _selectedImageUri,
            readTime = estimateReadTime(text = _bodyStateFlow.value, context = context),
            subtitle = _subtitleStateFlow.value,
            title = _titleStateFlow.value,
            topic = _selectedTopicStateFlow.value
        )
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
     * Retrieves the list of available topics from the repository.
     *
     * @return A list of [String] objects representing available topics.
     */
    fun getTopics(): List<String> =
        firstUserSelectionRepository.getTopics().map { viewTopic -> viewTopic.topicName }.sorted()
}