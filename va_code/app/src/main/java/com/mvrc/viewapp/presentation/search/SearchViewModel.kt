package com.mvrc.viewapp.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvrc.viewapp.core.Constants.MORE_POSTS_LIMIT_RATE
import com.mvrc.viewapp.core.Constants.SEARCHED_POSTS_LIMIT_RATE
import com.mvrc.viewapp.core.PostsRequestHandler
import com.mvrc.viewapp.domain.state.PaginationState
import com.mvrc.viewapp.domain.state.PostsState
import com.mvrc.viewapp.domain.use_case.post.PostUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val postUseCases: PostUseCases
) : ViewModel() {
    private val _searchedTextStateFlow =
        MutableStateFlow("")
    val searchedTextStateFlow = _searchedTextStateFlow.asStateFlow()

    // Observable state for the searched posts' state
    private val _searchedPostsStateFlow = MutableStateFlow(PostsState())
    val searchedPostsStateFlow = _searchedPostsStateFlow.asStateFlow()

    // Observable state for pagination information (searched posts)
    private val _paginationSearchedPostsState =
        MutableStateFlow(PaginationState(SEARCHED_POSTS_LIMIT_RATE))
    val paginationSearchedPostsState = _paginationSearchedPostsState.asStateFlow()

    private val postsRequestHandler = PostsRequestHandler()

    /**
     * Handles the search input text updating the [_searchedTextStateFlow] with the provided input text.
     *
     * @param inputText The text input provided by the user_profile.
     */
    fun onSearch(inputText: String) {
        _searchedTextStateFlow.value = inputText
    }

    /**
     * Searches for posts based on the specified search text.
     * It handles the case when the user_profile clears the text field and it does not
     * increment the number of items to display as the [loadMoreSearchedPosts] method.
     *
     * @param numOfPostsToDisplay The number of posts to display.
     */
    fun searchPosts(
        numOfPostsToDisplay: Long = _paginationSearchedPostsState.value.numOfItems
    ) {
        viewModelScope.launch {
            postUseCases.getSearchedPosts(numOfPostsToDisplay = numOfPostsToDisplay,
                searchedText = _searchedTextStateFlow.value.lowercase(Locale.getDefault()),
                onSuccess = { newPosts ->

                    // Handle the case when the search text is empty.
                    if (_searchedTextStateFlow.value.isEmpty()) {
                        _paginationSearchedPostsState.update {
                            it.copy(
                                numOfItems = SEARCHED_POSTS_LIMIT_RATE,
                                endReached = true,
                            )
                        }

                        _searchedPostsStateFlow.update {
                            it.copy(
                                posts = persistentListOf(), isLoading = false, error = ""
                            )
                        }
                    } else {

                        // There are no more posts on the backend.
                        val hasMoreData: Boolean = if (newPosts.size < SEARCHED_POSTS_LIMIT_RATE) {
                            false
                        } else {
                            // Check if there are posts that do not exist in the flow.
                            newPosts.any { post ->
                                !_searchedPostsStateFlow.value.posts.map { it.id }.contains(post.id)
                            }
                        }

                        _paginationSearchedPostsState.update {
                            it.copy(
                                numOfItems = SEARCHED_POSTS_LIMIT_RATE + MORE_POSTS_LIMIT_RATE,
                                endReached = !hasMoreData,
                            )
                        }

                        _searchedPostsStateFlow.update {
                            it.copy(
                                posts = newPosts.toImmutableList(), isLoading = false, error = ""
                            )
                        }
                    }
                },
                onLoading = {
                    postsRequestHandler.onRequestLoading(
                        postsStateFlow = _searchedPostsStateFlow,
                    )
                },
                onError = { error ->
                    postsRequestHandler.onRequestError(
                        message = error, postsStateFlow = _searchedPostsStateFlow,
                    )
                })
        }
    }

    /**
     * Load more searched posts.
     *
     * @param numOfPostsToDisplay The number of posts to load.
     */
    private fun loadMoreSearchedPosts(
        numOfPostsToDisplay: Long = _paginationSearchedPostsState.value.numOfItems
    ) {
        viewModelScope.launch {
            postUseCases.getSearchedPosts(numOfPostsToDisplay = numOfPostsToDisplay,
                searchedText = _searchedTextStateFlow.value.lowercase(Locale.getDefault()),
                onSuccess = { posts ->
                    postsRequestHandler.onRequestSuccess(
                        newPosts = posts,
                        postsStateFlow = _searchedPostsStateFlow,
                        paginationStateFlow = _paginationSearchedPostsState,
                        sectionPostsLimitRate = SEARCHED_POSTS_LIMIT_RATE
                    )
                },
                onLoading = {
                    postsRequestHandler.onRequestLoading(
                        postsStateFlow = _searchedPostsStateFlow,
                    )
                },
                onError = { error ->
                    postsRequestHandler.onRequestError(
                        message = error, postsStateFlow = _searchedPostsStateFlow,
                    )
                })
        }
    }

    /**
     * Load more searched posts.
     */
    fun getSearchedPostsPaginated() {
        if (_searchedPostsStateFlow.value.posts.isEmpty()) {
            return
        }

        if (_paginationSearchedPostsState.value.endReached) {
            return
        }

        loadMoreSearchedPosts(_paginationSearchedPostsState.value.numOfItems)
    }
}