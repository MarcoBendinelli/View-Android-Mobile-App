package com.mvrc.viewapp.domain.use_case.post

import android.net.Uri
import com.mvrc.viewapp.domain.repository.PostRepository
import com.mvrc.viewapp.domain.repository.SaveNewPostResponse

/**
 * Use case class responsible for saving a new post.
 *
 * @property repository The [PostRepository] responsible for providing data retrieval methods for posts.
 */
class SaveNewPost(
    private val repository: PostRepository
) {
    /**
     * Invokes the use case to save the post.
     *
     * @param authorName The name of the post creator.
     * @param authorImageUrl The name of the post creator.
     * @param title The title of the post to be added.
     * @param subtitle The subtitle of the post to be added.
     * @param imageUri The image of the post to be added.
     * @param topic The topic of the post to be added.
     * @param body The body of the post to be added.
     * @param readTime The estimated read time of the post.
     */
    suspend operator fun invoke(
        authorName: String,
        authorImageUrl: String,
        title: String,
        subtitle: String,
        imageUri: Uri?,
        topic: String,
        body: String,
        readTime: String
    ): SaveNewPostResponse =
        repository.savePost(
            authorName = authorName,
            body = body,
            imageUri = imageUri,
            authorImageUrl = authorImageUrl,
            readTime = readTime,
            subtitle = subtitle,
            title = title,
            topic = topic
        )
}