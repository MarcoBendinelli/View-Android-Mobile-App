package com.mvrc.viewapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvrc.viewapp.core.Constants.FOLLOWING_POSTS_LIMIT_RATE
import com.mvrc.viewapp.core.Constants.FOR_YOU_POSTS_LIMIT_RATE
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
 * [ViewModel] for managing the home screen states.
 *
 * @property postUseCases The use case class that encapsulates post-related operations.
 * @property viewUserCache The cache to update the user_profile data.
 * @param newestString The "Newest" string retrieved from the resources.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postUseCases: PostUseCases,
    private val viewUserCache: ViewUserCache,
    newestString: String,
) : ViewModel() {

    // Observable state for the for you posts' state
    private val _forYouPostsStateFlow = MutableStateFlow(PostsState())
    val forYouPostsStateFlow = _forYouPostsStateFlow.asStateFlow()

    // Observable state for pagination information (for you)
    private val _paginationForYouPostsState =
        MutableStateFlow(PaginationState(FOR_YOU_POSTS_LIMIT_RATE))
    val paginationForYouPostsState = _paginationForYouPostsState.asStateFlow()

    // Observable state for the trending now posts' state
    private val _trendingNowPostsStateFlow = MutableStateFlow(PostsState())
    val trendingNowPostsStateFlow = _trendingNowPostsStateFlow.asStateFlow()

    // Observable state for the following posts' state
    private val _followingPostsStateFlow = MutableStateFlow(PostsState())
    val followingPostsStateFlow = _followingPostsStateFlow.asStateFlow()

    // Observable state for pagination information (following)
    private val _paginationFollowingPostsState =
        MutableStateFlow(PaginationState(FOLLOWING_POSTS_LIMIT_RATE))
    val paginationFollowingPostsState = _paginationFollowingPostsState.asStateFlow()

    // Observable state for the currently selected filter
    private val _selectedFilterStateFlow = MutableStateFlow(newestString)
    val selectedFilterStateFlow = _selectedFilterStateFlow.asStateFlow()

    private val postsRequestHandler = PostsRequestHandler()

    /**
     * Exposes a read-only [SharedFlow] of [ViewUser] updates from the cache.
     */
    val userStateFlow: StateFlow<ViewUser> get() = viewUserCache.userUpdates

    /**
     * Updates the flow state with the selected filter.
     *
     * @param filter The new selected filter by the user_profile.
     */
    fun selectFilter(filter: String) {
        _selectedFilterStateFlow.value = filter
    }

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
     * Loads the for you posts based on the selected filter.
     *
     * @param numOfPostsToDisplay The number of posts to load.
     */
    fun loadForYouPosts(
        numOfPostsToDisplay: Long = _paginationForYouPostsState.value.numOfItems
    ) {
        viewModelScope.launch {
            postUseCases.getForYouPosts(numOfPostsToDisplay = numOfPostsToDisplay,
                filter = _selectedFilterStateFlow.value,
                onSuccess = { posts ->
                    postsRequestHandler.onRequestSuccess(
                        newPosts = posts,
                        postsStateFlow = _forYouPostsStateFlow,
                        paginationStateFlow = _paginationForYouPostsState,
                        sectionPostsLimitRate = FOR_YOU_POSTS_LIMIT_RATE
                    )
                },
                onLoading = { postsRequestHandler.onRequestLoading(postsStateFlow = _forYouPostsStateFlow) },
                onError = { error ->
                    postsRequestHandler.onRequestError(
                        message = error,
                        postsStateFlow = _forYouPostsStateFlow,
                    )
                })
        }
    }

    /**
     * Loads the trending now posts.
     */
    fun loadTrendingNowPosts() {
        viewModelScope.launch {
            postUseCases.getTrendingNowPosts(
                onSuccess = { posts ->
                    postsRequestHandler.onRequestSuccess(
                        newPosts = posts,
                        postsStateFlow = _trendingNowPostsStateFlow,
                        paginationStateFlow = null,
                        sectionPostsLimitRate = null
                    )
                },
                onLoading = {
                    postsRequestHandler.onRequestLoading(
                        postsStateFlow = _trendingNowPostsStateFlow,
                    )
                },
                onError = { error ->
                    postsRequestHandler.onRequestError(
                        message = error, postsStateFlow = _trendingNowPostsStateFlow,
                    )
                })
        }
    }

    /**
     * Loads the following posts based on the following users.
     *
     * @param numOfPostsToDisplay The number of posts to load.
     */
    fun loadFollowingPosts(
        numOfPostsToDisplay: Long = _paginationFollowingPostsState.value.numOfItems
    ) {
        viewModelScope.launch {
            postUseCases.getFollowingPosts(numOfPostsToDisplay = numOfPostsToDisplay,
                onSuccess = { posts ->
                    postsRequestHandler.onRequestSuccess(
                        newPosts = posts,
                        postsStateFlow = _followingPostsStateFlow,
                        paginationStateFlow = _paginationFollowingPostsState,
                        sectionPostsLimitRate = FOLLOWING_POSTS_LIMIT_RATE,
                    )
                },
                onLoading = { postsRequestHandler.onRequestLoading(postsStateFlow = _followingPostsStateFlow) },
                onError = { error ->
                    postsRequestHandler.onRequestError(
                        message = error,
                        postsStateFlow = _followingPostsStateFlow,
                    )
                })
        }
    }

    /**
     * Load more for you posts.
     */
    fun getForYouPostsPaginated() {
        if (_forYouPostsStateFlow.value.posts.isEmpty()) {
            return
        }

        if (_paginationForYouPostsState.value.endReached) {
            return
        }

        loadForYouPosts(_paginationForYouPostsState.value.numOfItems)
    }

    /**
     * Load more following posts.
     */
    fun getFollowingPostsPaginated() {
        if (_followingPostsStateFlow.value.posts.isEmpty()) {
            return
        }

        if (_paginationFollowingPostsState.value.endReached) {
            return
        }

        loadFollowingPosts(_paginationFollowingPostsState.value.numOfItems)
    }
}
