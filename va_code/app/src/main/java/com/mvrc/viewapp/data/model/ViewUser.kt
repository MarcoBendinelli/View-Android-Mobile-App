package com.mvrc.viewapp.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.mvrc.viewapp.core.Constants.USERS_CREATED_AT_FIELD
import com.mvrc.viewapp.core.Constants.USERS_EMAIL_FIELD
import com.mvrc.viewapp.core.Constants.USERS_FOLLOWERS_FIELD
import com.mvrc.viewapp.core.Constants.USERS_FOLLOWING_FIELD
import com.mvrc.viewapp.core.Constants.USERS_ID_FIELD
import com.mvrc.viewapp.core.Constants.USERS_PHOTO_URL_FIELD
import com.mvrc.viewapp.core.Constants.USERS_PROFESSION_FIELD
import com.mvrc.viewapp.core.Constants.USERS_TOPICS_FIELD
import com.mvrc.viewapp.core.Constants.USERS_USERNAME_FIELD

/**
 * Data class representing an authenticated user_profile in the application.
 *
 * The [ViewUser] data class encapsulates information about an authenticated user_profile,
 * including their unique identifier ([id]), email address ([email]),
 * display name ([username]), profession ([profession]),
 * photo URL ([photoUrl]), interested topics ([topics]),
 * list of following users ([following]), list of followers ([followers]),
 * and the creation timestamp ([createdAt]).
 *
 * @property id The current user_profile's id.
 * @property email The current user_profile's email address.
 * @property username The current user_profile's name (display name).
 * @property profession Profession of the user_profile.
 * @property photoUrl URL for the current user_profile's photo.
 * @property topics List of topics.
 * @property following List of following users id.
 * @property followers List of followers id.
 * @property createdAt The timestamp creation of the user_profile.
 */
data class ViewUser(
    val id: String,
    val email: String,
    val username: String,
    val profession: String,
    val photoUrl: String,
    val topics: List<String>,
    val following: List<String>,
    val followers: List<String>,
    val createdAt: Timestamp
) {
    companion object {

        /**
         * Factory method to create a [ViewUser] instance from Firestore snapshot data.
         *
         * @param snapshot Firestore document snapshot.
         *
         * @return A new [ViewUser] instance created from the Firestore snapshot.
         */
        fun fromFirestore(
            snapshot: DocumentSnapshot
        ): ViewUser {
            val data = snapshot.data

            return ViewUser(
                email = data?.get(USERS_EMAIL_FIELD) as String,
                id = snapshot.id,
                username = data[USERS_USERNAME_FIELD] as String,
                profession = data[USERS_PROFESSION_FIELD] as String,
                photoUrl = data[USERS_PHOTO_URL_FIELD] as String,
                topics = (data[USERS_TOPICS_FIELD] as List<*>).mapNotNull { it as String },
                following = (data[USERS_FOLLOWING_FIELD] as List<*>).mapNotNull { it as String },
                followers = (data[USERS_FOLLOWERS_FIELD] as List<*>).mapNotNull { it as String },
                createdAt = data[USERS_CREATED_AT_FIELD] as Timestamp
            )
        }

        /**
         * Creates an empty [ViewUser] representing an unauthenticated user_profile.
         */
        fun empty(): ViewUser {
            return ViewUser(
                id = "",
                email = "",
                username = "",
                profession = "",
                photoUrl = "",
                topics = emptyList(),
                following = emptyList(),
                followers = emptyList(),
                createdAt = Timestamp(0, 0)
            )
        }
    }

    /**
     * Converts the [ViewUser] to a map for Firestore.
     *
     * @return A map representation of the [ViewUser] suitable for Firestore.
     */
    fun toMap(): Map<String, Any?> {
        return mapOf(
            USERS_ID_FIELD to id,
            USERS_EMAIL_FIELD to email,
            USERS_USERNAME_FIELD to username,
            USERS_PROFESSION_FIELD to profession,
            USERS_PHOTO_URL_FIELD to photoUrl,
            USERS_TOPICS_FIELD to topics,
            USERS_FOLLOWING_FIELD to following,
            USERS_FOLLOWERS_FIELD to followers,
            USERS_CREATED_AT_FIELD to createdAt
        )
    }
}