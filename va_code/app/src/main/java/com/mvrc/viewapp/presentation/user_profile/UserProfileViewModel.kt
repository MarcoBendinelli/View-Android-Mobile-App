package com.mvrc.viewapp.presentation.user_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvrc.viewapp.core.Constants.USER_POSTS_LIMIT_RATE
import com.mvrc.viewapp.core.PostsRequestHandler
import com.mvrc.viewapp.data.cache.ViewUserCache
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.model.ViewUser
import com.mvrc.viewapp.domain.repository.FollowUserResponse
import com.mvrc.viewapp.domain.repository.UserProfileRepository
import com.mvrc.viewapp.domain.repository.UserResponse
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
 * ViewModel for handling the User screen state.
 *
 * @property userRepository The repository for retrieving user_profile related information.
 * @property viewUserCache The cache to update the user_profile data.
 */
@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserProfileRepository,
    private val postUseCases: PostUseCases,
    private val viewUserCache: ViewUserCache,
) : ViewModel() {
    private val _userResponseStateFlow = MutableStateFlow<UserResponse>(Loading)
    val userResponseStateFlow = _userResponseStateFlow.asStateFlow()

    // Observable state for the user posts' state
    private val _userPostsStateFlow = MutableStateFlow(PostsState())
    val userPostsStateFlow = _userPostsStateFlow.asStateFlow()

    // Observable state for pagination information
    private val _paginationUserPostsState =
        MutableStateFlow(PaginationState(USER_POSTS_LIMIT_RATE))
    val paginationUserPostsState = _paginationUserPostsState.asStateFlow()

    // Mutable state representing the response of the Following operation.
    private val _followUserResponseStateFlow = MutableStateFlow<FollowUserResponse>(
        Success(false)
    )
    val followUserResponseStateFlow = _followUserResponseStateFlow.asStateFlow()

    private val postsRequestHandler = PostsRequestHandler()

    /**
     * Exposes a read-only [SharedFlow] of [ViewUser] updates from the cache.
     */
    val userStateFlow: StateFlow<ViewUser> get() = viewUserCache.userUpdates

    /**
     * Follow the selected user saving it on backend and updates the corresponded [MutableStateFlow]
     *
     * @param userId The selected user Id.
     */
    fun followUser(userId: String) = viewModelScope.launch {
        _followUserResponseStateFlow.value = userRepository.followUser(selectedUserId = userId)
    }

    /**
     * Loads a user_profile with the specified [userId]. The result is collected
     * into the [MutableStateFlow] to update the UI.
     *
     * @param userId The identifier of the user_profile to load.
     */
    fun loadUser(userId: String) = viewModelScope.launch {
        userRepository.getUser(userId).collect { response ->
            _userResponseStateFlow.value = response
        }
    }

    /**
     * Loads the for you posts based on the selected filter.
     *
     * @param userId The id of the posts creator.
     * @param numOfPostsToDisplay The number of posts to load.
     */
    fun loadUserPosts(
        userId: String,
        numOfPostsToDisplay: Long = _paginationUserPostsState.value.numOfItems
    ) {
        viewModelScope.launch {
            postUseCases.getUserPosts(numOfPostsToDisplay = numOfPostsToDisplay,
                userId = userId,
                onSuccess = { posts ->
                    postsRequestHandler.onRequestSuccess(
                        newPosts = posts,
                        postsStateFlow = _userPostsStateFlow,
                        paginationStateFlow = _paginationUserPostsState,
                        sectionPostsLimitRate = USER_POSTS_LIMIT_RATE
                    )
                },
                onLoading = { postsRequestHandler.onRequestLoading(postsStateFlow = _userPostsStateFlow) },
                onError = { error ->
                    postsRequestHandler.onRequestError(
                        message = error,
                        postsStateFlow = _userPostsStateFlow,
                    )
                })
        }
    }

    /**
     * Load more user posts.
     *
     * @param userId The id of the posts creator.
     */
    fun getUserPostsPaginated(userId: String) {
        if (_userPostsStateFlow.value.posts.isEmpty()) {
            return
        }

        if (_paginationUserPostsState.value.endReached) {
            return
        }

        loadUserPosts(
            userId = userId,
            numOfPostsToDisplay = _paginationUserPostsState.value.numOfItems
        )
    }
}