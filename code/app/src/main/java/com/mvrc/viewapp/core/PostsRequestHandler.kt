package com.mvrc.viewapp.core

import com.mvrc.viewapp.core.Constants.MORE_POSTS_LIMIT_RATE
import com.mvrc.viewapp.data.model.ViewPost
import com.mvrc.viewapp.domain.state.PaginationState
import com.mvrc.viewapp.domain.state.PostsState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Helper class for handling common operations related to post requests.
 *
 * This class provides functions to handle success, error, and loading states during the retrieval
 * of posts, it can be reused across different view models.
 */
class PostsRequestHandler {
    /**
     * Handles the successful retrieval of posts.
     *
     * @param newPosts The list of posts successfully retrieved.
     * @param postsStateFlow The current posts [MutableStateFlow].
     * @param paginationStateFlow The nullable pagination [MutableStateFlow].
     */
    fun onRequestSuccess(
        newPosts: List<ViewPost>,
        postsStateFlow: MutableStateFlow<PostsState>,
        paginationStateFlow: MutableStateFlow<PaginationState>?,
        sectionPostsLimitRate: Long?,
    ) {
        if (paginationStateFlow != null) {

            // There are no more posts on the backend.
            val hasMoreData: Boolean = if (newPosts.size < sectionPostsLimitRate!!) {
                false
            } else {
                // Check if there are posts that do not exist in the flow.
                newPosts.any { post ->
                    !postsStateFlow.value.posts.map { it.id }.contains(post.id)
                }
            }

            paginationStateFlow.update {
                it.copy(
                    numOfItems = it.numOfItems + MORE_POSTS_LIMIT_RATE,
                    endReached = !hasMoreData,
                )
            }
        }

        postsStateFlow.update {
            it.copy(
                posts = newPosts.toImmutableList(), isLoading = false, error = ""
            )
        }
    }

    /**
     * Handles errors that may occur during the retrieval of posts.
     *
     * @param message The error message associated with the failure.
     * @param postsStateFlow The current posts [MutableStateFlow].
     */
    fun onRequestError(
        message: String?,
        postsStateFlow: MutableStateFlow<PostsState>,
    ) {
        postsStateFlow.update {
            it.copy(
                error = message ?: "Unexpected Error",
                isLoading = false,
            )
        }
    }

    /**
     * Handles the loading state during the retrieval of posts.
     *
     * @param postsStateFlow The current posts [MutableStateFlow].
     */
    fun onRequestLoading(postsStateFlow: MutableStateFlow<PostsState>) {
        if (postsStateFlow.value.posts.isEmpty()) {
            postsStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }
        }
    }
}