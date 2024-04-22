package com.mvrc.viewapp.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.mvrc.viewapp.core.Constants.POSTS_AUTHOR_ID_FIELD
import com.mvrc.viewapp.core.Constants.POSTS_AUTHOR_NAME_FIELD
import com.mvrc.viewapp.core.Constants.POSTS_AUTHOR_PHOTO_URL_FIELD
import com.mvrc.viewapp.core.Constants.POSTS_BODY_FIELD
import com.mvrc.viewapp.core.Constants.POSTS_BOOKMARKED_BY_FIELD
import com.mvrc.viewapp.core.Constants.POSTS_CREATED_AT_FIELD
import com.mvrc.viewapp.core.Constants.POSTS_ID_FIELD
import com.mvrc.viewapp.core.Constants.POSTS_IMAGE_URL_FIELD
import com.mvrc.viewapp.core.Constants.POSTS_READ_TIME_FIELD
import com.mvrc.viewapp.core.Constants.POSTS_SUBTITLE_FIELD
import com.mvrc.viewapp.core.Constants.POSTS_TITLE_FIELD
import com.mvrc.viewapp.core.Constants.POSTS_TOPIC_FIELD

/**
 * Data class representing a post in the application.
 *
 * The [ViewPost] class is an immutable representation of a post, containing
 * information such as [authorId], [authorName], [authorPhotoUrl], post [id], [title], [subtitle], [body],
 * [imageUrl], [topic], [createdAt], list of users ids that bookmarks the post ([bookmarkedBy]),
 * and estimated reading time in hours ([readTime]).
 *
 * @property authorId The id of the author.
 * @property authorName The name of the author.
 * @property authorPhotoUrl The photo url of the author.
 * @property id The id of the post.
 * @property title The title of the post.
 * @property subtitle The subtitle of the post.
 * @property body The body of the post.
 * @property imageUrl The image url of the post.
 * @property createdAt The timestamp of the post.
 * @property topic The topic of the post.
 * @property readTime The post read time.
 * @property bookmarkedBy List of users ids that bookmarks the post.
 */
data class ViewPost(
    val authorId: String,
    val authorName: String,
    val authorPhotoUrl: String,
    val id: String,
    val title: String,
    val subtitle: String,
    val body: String,
    val imageUrl: String,
    val createdAt: Timestamp,
    val topic: ViewTopic,
    val readTime: String,
    val bookmarkedBy: List<String>
) {
    companion object {

        /**
         * Converts a Firestore document snapshot into a [ViewPost] object.
         *
         * @param snapshot The Firestore document snapshot to convert.
         * @return A [ViewPost] object populated with data from the Firestore snapshot.
         */
        fun fromFirestore(snapshot: DocumentSnapshot): ViewPost {
            val data = snapshot.data

            return ViewPost(
                authorId = data?.get(POSTS_AUTHOR_ID_FIELD) as String,
                authorName = data[POSTS_AUTHOR_NAME_FIELD] as String,
                authorPhotoUrl = data[POSTS_AUTHOR_PHOTO_URL_FIELD] as String,
                id = snapshot.id,
                title = data[POSTS_TITLE_FIELD] as String,
                subtitle = data[POSTS_SUBTITLE_FIELD] as String,
                body = data[POSTS_BODY_FIELD] as String,
                imageUrl = data[POSTS_IMAGE_URL_FIELD] as String,
                createdAt = data[POSTS_CREATED_AT_FIELD] as Timestamp,
                topic = ViewTopic(
                    topicName = data[POSTS_TOPIC_FIELD] as String
                ),
                readTime = data[POSTS_READ_TIME_FIELD] as String,
                bookmarkedBy = (data[POSTS_BOOKMARKED_BY_FIELD] as List<*>).mapNotNull { it as String }
            )
        }
    }

    /**
     * Converts the [ViewPost] to a map for Firestore.
     *
     * @return A map representation of the [ViewPost] suitable for Firestore.
     */
    fun toMap(): Map<String, Any?> {
        return mapOf(
            POSTS_AUTHOR_ID_FIELD to authorId,
            POSTS_AUTHOR_NAME_FIELD to authorName,
            POSTS_AUTHOR_PHOTO_URL_FIELD to authorPhotoUrl,
            POSTS_ID_FIELD to id,
            POSTS_TITLE_FIELD to title,
            POSTS_SUBTITLE_FIELD to subtitle,
            POSTS_BODY_FIELD to body,
            POSTS_IMAGE_URL_FIELD to imageUrl,
            POSTS_CREATED_AT_FIELD to createdAt,
            POSTS_TOPIC_FIELD to topic.topicName,
            POSTS_READ_TIME_FIELD to readTime,
            POSTS_BOOKMARKED_BY_FIELD to bookmarkedBy
        )
    }
}