package com.mvrc.viewapp.data.cache

import com.google.firebase.Timestamp
import com.mvrc.viewapp.data.model.ViewUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * An in-memory cache client.
 *
 * Represents a simple in-memory cache for storing a single [ViewUser] instance.
 */
class ViewUserCache {

    /**
     * The in-memory cache used to store the [ViewUser].
     */
    var data: ViewUser = ViewUser.empty()

    /**
     * [StateFlow] for notifying subscribers of updates to the cached [ViewUser].
     */
    private val _userUpdates = MutableStateFlow(ViewUser.empty())
    val userUpdates: StateFlow<ViewUser> get() = _userUpdates.asStateFlow()

    /**
     * Writes the provided [value] to the in-memory cache.
     *
     * It updates the cache value and emits an update event to subscribers.
     *
     * @param value The [ViewUser] to be stored in the cache.
     */
    fun write(value: ViewUser) {
        data = value
        _userUpdates.tryEmit(value)
    }

    /**
     * Reads the [ViewUser] from the in-memory cache.
     *
     * @return The current [ViewUser] stored in the cache.
     */
    fun read(): ViewUser {
        return data
    }

    /**
     * Updates the User info inside the cache.
     *
     * The [updateUserInfo] method modifies the information of the [ViewUser] in the cache.
     * It allows updating specific attributes of the user_profile, such as email, username, etc.
     *
     * @param email New email value.
     * @param username New username value.
     * @param profession New profession value.
     * @param photoUrl New photo URL value.
     * @param topics New list of topics.
     * @param following List of selected user_profile IDs.
     * @param followers New list of followers.
     * @param createdAt New creation timestamp.
     */
    fun updateUserInfo(
        email: String? = null,
        username: String? = null,
        profession: String? = null,
        photoUrl: String? = null,
        topics: List<String>? = null,
        following: List<String>? = null,
        followers: List<String>? = null,
        createdAt: Timestamp? = null
    ) {
        val viewUser = read()

        write(
            value = viewUser.copy(
                email = email ?: viewUser.email,
                username = username ?: viewUser.username,
                profession = profession ?: viewUser.profession,
                photoUrl = photoUrl ?: viewUser.photoUrl,
                topics = topics ?: viewUser.topics,
                following = following ?: viewUser.following,
                followers = followers ?: viewUser.followers,
                createdAt = createdAt ?: viewUser.createdAt
            )
        )
    }
}