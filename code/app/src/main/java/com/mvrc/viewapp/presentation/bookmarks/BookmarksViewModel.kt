package com.mvrc.viewapp.presentation.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvrc.viewapp.core.Constants.BOOKMARKED_POSTS_LIMIT_RATE
import com.mvrc.viewapp.core.PostsRequestHandler
import com.mvrc.viewapp.data.cache.ViewUserCache
import com.mvrc.viewapp.data.model.ViewUser
import com.mvrc.viewapp.domain.state.PaginationState
import com.mvrc.viewapp.domain.state.PostsState
import com.mvrc.viewapp.domain.use_case.post.PostUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] for managing the bookmarks screen states.
 *
 * @property postUseCases The use case class that encapsulates post-related operations.
 * @property viewUserCache The cache to update the user_profile data.
 */
@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val postUseCases: PostUseCases,
    private val viewUserCache: ViewUserCache,
) : ViewModel() {
    // Observable state for the for you posts' state
    private val _bookmarkedPostsStateFlow = MutableStateFlow(PostsState())
    val bookmarkedPostsStateFlow = _bookmarkedPostsStateFlow.asStateFlow()

    // Observable state for pagination information (bookmarks)
    private val _paginationBookmarkedPostsState =
        MutableStateFlow(PaginationState(BOOKMARKED_POSTS_LIMIT_RATE))
    val paginationBookmarkedPostsState = _paginationBookmarkedPostsState.asStateFlow()

    private val postsRequestHandler = PostsRequestHandler()

    /**
     * Exposes a read-only [SharedFlow] of [ViewUser] updates from the cache.
     */
    val userStateFlow: StateFlow<ViewUser> get() = viewUserCache.userUpdates

    /**
     * Handles the click of a post bookmark by saving or removing
     * the selected post by the current user_profile on backend.
     *
     * @param selectedPostId The selected or deselected post id.
     */
    fun saveOrRemoveBookmarkedPost(selectedPostId: String) {
        viewModelScope.launch {
            postUseCases.bookmarkPost(selectedPostId)
        }
    }

    /**
     * Loads the bookmarked posts.
     *
     * @param numOfPostsToDisplay The number of posts to load.
     */
    fun loadBookmarkedPosts(
        numOfPostsToDisplay: Long = _paginationBookmarkedPostsState.value.numOfItems
    ) {
        viewModelScope.launch {
            postUseCases.getBookmarkedPosts(numOfPostsToDisplay = numOfPostsToDisplay,
                onSuccess = { posts ->
                    postsRequestHandler.onRequestSuccess(
                        newPosts = posts,
                        postsStateFlow = _bookmarkedPostsStateFlow,
                        paginationStateFlow = _paginationBookmarkedPostsState,
                        sectionPostsLimitRate = BOOKMARKED_POSTS_LIMIT_RATE
                    )
                },
                onLoading = {
                    postsRequestHandler.onRequestLoading(
                        postsStateFlow = _bookmarkedPostsStateFlow,
                    )
                },
                onError = { error ->
                    postsRequestHandler.onRequestError(
                        message = error, postsStateFlow = _bookmarkedPostsStateFlow,
                    )
                })
        }
    }

    /**
     * Load more bookmarked posts.
     */
    fun getBookmarkedPostsPaginated() {
        if (_bookmarkedPostsStateFlow.value.posts.isEmpty()) {
            return
        }

        if (_paginationBookmarkedPostsState.value.endReached) {
            return
        }

        loadBookmarkedPosts(_paginationBookmarkedPostsState.value.numOfItems)
    }
}