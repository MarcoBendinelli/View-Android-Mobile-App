package com.mvrc.viewapp.domain.use_case.post

import com.mvrc.viewapp.data.model.Response
import com.mvrc.viewapp.data.model.ViewPost
import com.mvrc.viewapp.domain.repository.PostRepository

/**
 * Use case class responsible for retrieving bookmarked posts.
 *
 * @property repository The [PostRepository] responsible for providing data retrieval methods for posts.
 */
class GetBookmarkedPosts(
    private val repository: PostRepository
) {
    /**
     * Invokes the use case to retrieve posts.
     *
     * @param numOfPostsToDisplay The number of posts to fetch.
     * @param onSuccess Callback function invoked when posts are successfully retrieved.
     * @param onError Callback function invoked when an error occurs during the retrieval process.
     * @param onLoading Callback function invoked when the data retrieval is in progress.
     */
    suspend operator fun invoke(
        numOfPostsToDisplay: Long,
        onSuccess: (posts: List<ViewPost>) -> Unit,
        onError: (message: String?) -> Unit,
        onLoading: () -> Unit
    ) =
        repository.getBookmarkedPosts(numOfPostsToDisplay = numOfPostsToDisplay)
            .collect { response ->
                when (response) {
                    is Response.Success -> response.data?.let { data -> onSuccess(data) }
                    is Response.Failure -> onError(response.e.message)
                    is Response.Loading -> onLoading()
                }
            }
}