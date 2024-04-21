package com.mvrc.viewapp.domain.use_case.post

import com.mvrc.viewapp.data.model.Response
import com.mvrc.viewapp.data.model.ViewPost
import com.mvrc.viewapp.domain.repository.PostRepository

/**
 * Use case class responsible for retrieving posts based on user_profile search.
 *
 * @property repository The [PostRepository] responsible for providing data retrieval methods for posts.
 */
class GetSearchedPosts(
    private val repository: PostRepository
) {
    /**
     * Invokes the use case to retrieve posts.
     *
     * @param numOfPostsToDisplay The number of posts to fetch.
     * @param searchedText The searched input text.
     * @param onSuccess Callback function invoked when posts are successfully retrieved.
     * @param onError Callback function invoked when an error occurs during the retrieval process.
     * @param onLoading Callback function invoked when the data retrieval is in progress.
     */
    suspend operator fun invoke(
        numOfPostsToDisplay: Long,
        searchedText: String,
        onSuccess: (posts: List<ViewPost>) -> Unit,
        onError: (message: String?) -> Unit,
        onLoading: () -> Unit
    ) =
        repository.getSearchedPosts(
            numOfPostsToDisplay = numOfPostsToDisplay,
            searchedText = searchedText
        )
            .collect { response ->
                when (response) {
                    is Response.Success -> response.data?.let { data -> onSuccess(data) }
                    is Response.Failure -> onError(response.e.message)
                    is Response.Loading -> onLoading()
                }
            }
}