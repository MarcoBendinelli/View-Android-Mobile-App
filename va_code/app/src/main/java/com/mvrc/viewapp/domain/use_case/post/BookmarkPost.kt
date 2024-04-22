package com.mvrc.viewapp.domain.use_case.post

import com.mvrc.viewapp.domain.repository.BookmarkPostResponse
import com.mvrc.viewapp.domain.repository.PostRepository

/**
 * Use case class responsible for bookmarking a selected post.
 *
 * @property repository The [PostRepository] responsible for providing data retrieval methods for posts.
 */
class BookmarkPost(
    private val repository: PostRepository
) {
    /**
     * Invokes the use case to bookmark the post.
     *
     * @param selectedPostId The id of the selected post.
     */
    suspend operator fun invoke(
        selectedPostId: String,
    ): BookmarkPostResponse =
        repository.bookmarkPost(selectedPostId)
}