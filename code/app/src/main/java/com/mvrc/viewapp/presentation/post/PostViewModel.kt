package com.mvrc.viewapp.presentation.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.domain.repository.PostResponse
import com.mvrc.viewapp.domain.use_case.post.PostUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for handling the Post screen state.
 *
 * @property postUseCases The use case class that encapsulates post-related operations.
 */
@HiltViewModel
class PostViewModel @Inject constructor(
    private val postUseCases: PostUseCases,
) : ViewModel() {
    private val _postResponseStateFlow = MutableStateFlow<PostResponse>(Loading)
    val postResponseStateFlow = _postResponseStateFlow.asStateFlow()

    /**
     * Loads a post with the specified [postId]. The result is collected
     * into the [MutableStateFlow] to update the UI.
     *
     * @param postId The identifier of the post to load.
     */
    fun loadPost(postId: String) = viewModelScope.launch {
        postUseCases.getPost(postId).collect { response ->
            _postResponseStateFlow.value = response
        }
    }
}