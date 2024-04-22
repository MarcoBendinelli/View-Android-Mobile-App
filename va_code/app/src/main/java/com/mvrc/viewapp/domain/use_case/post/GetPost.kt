package com.mvrc.viewapp.domain.use_case.post

import com.mvrc.viewapp.domain.repository.PostRepository

/**
 * Use case class responsible for retrieving the post with the specified id.
 *
 * @property repository The [PostRepository] responsible for providing data retrieval methods for posts.
 */
class GetPost(
    private val repository: PostRepository
) {
    /**
     * Invokes the use case to retrieve the post data.
     *
     * @param postId The id of the post to fetch.
     */
    operator fun invoke(
        postId: String,
    ) = repository.getPost(postId)
}