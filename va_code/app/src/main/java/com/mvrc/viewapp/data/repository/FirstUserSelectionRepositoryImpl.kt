package com.mvrc.viewapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mvrc.viewapp.core.Constants.NUM_OF_MOST_FOLLOWED_USERS_TO_DISPLAY
import com.mvrc.viewapp.core.Constants.USERS_COLLECTION
import com.mvrc.viewapp.core.Constants.USERS_CREATED_AT_FIELD
import com.mvrc.viewapp.core.Constants.USERS_FOLLOWERS_FIELD
import com.mvrc.viewapp.core.Constants.USERS_FOLLOWING_FIELD
import com.mvrc.viewapp.core.Constants.USERS_ID_FIELD
import com.mvrc.viewapp.core.Constants.USERS_TOPICS_FIELD
import com.mvrc.viewapp.data.cache.ViewUserCache
import com.mvrc.viewapp.data.model.Response
import com.mvrc.viewapp.data.model.Response.Failure
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.model.ViewTopic
import com.mvrc.viewapp.data.model.ViewUser
import com.mvrc.viewapp.domain.repository.FetchingUsersResponse
import com.mvrc.viewapp.domain.repository.FirstUserSelectionRepository
import com.mvrc.viewapp.domain.repository.SaveTopicsResponse
import com.mvrc.viewapp.domain.repository.SaveUsersResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Implementation of [FirstUserSelectionRepository] for managing first user_profile selections.
 *
 * @property auth An instance of [FirebaseAuth] for retrieving the current user_profile logged in.
 * @property firestore Instance of [FirebaseFirestore] for database operations.
 * @property topics [Array] of strings representing the topics.
 * @property viewUserCache The [ViewUserCache] to save the user_profile data.
 */
class FirstUserSelectionRepositoryImpl
@Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val topics: Array<out String>,
    private val viewUserCache: ViewUserCache
) : FirstUserSelectionRepository {

    /**
     * List of [ViewTopic] representing available topics.
     */
    private val viewTopics = mutableListOf<ViewTopic>()

    /**
     * Initializes the repository with predefined topics obtained from application resources.
     */
    init {
        viewTopics.addAll(
            topics
                .map { topic -> ViewTopic(topic) }
        )
    }

    override fun getTopics(): List<ViewTopic> =
        viewTopics.toList()


    override suspend fun saveTopics(selectedTopics: List<ViewTopic>): SaveTopicsResponse =
        try {
            val userRef = firestore
                .collection(USERS_COLLECTION)
                .document(auth.currentUser?.uid!!)

            // Update the User Info on Firestore
            userRef.update(
                USERS_TOPICS_FIELD,
                selectedTopics.map { it.topicName }
            ).await()

            // Update the User Info inside the cache
            viewUserCache.updateUserInfo(
                topics = selectedTopics.map { topic -> topic.topicName })
            Success(true)
        } catch (e: Exception) {
            Failure(e)
        }

    override fun getMostFollowedUsers(): Flow<FetchingUsersResponse> = callbackFlow {
        val listener = firestore.collection(USERS_COLLECTION)
            .whereNotEqualTo(USERS_ID_FIELD, auth.currentUser?.uid!!)
            .orderBy(USERS_ID_FIELD, Query.Direction.DESCENDING)
            .orderBy(USERS_CREATED_AT_FIELD, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    trySend(
                        Failure(
                            exception
                        )
                    )
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    var users = snapshot.documents.map { doc -> ViewUser.fromFirestore(doc) }

                    // Sort users based on the size of their "followers" array in descending order.
                    users.sortedByDescending { it.followers.size }.also { users = it }

                    // Take the top N users.
                    val mostFollowedUsers =
                        users.take(NUM_OF_MOST_FOLLOWED_USERS_TO_DISPLAY).toList()

                    trySend(Success(mostFollowedUsers))
                }
            }

        // Remove the listener when the flow is cancelled
        awaitClose { listener.remove() }
    }.onStart { emit(Response.Loading) }

    override suspend fun saveUser(selectedUserId: String): SaveUsersResponse = try {
        val currentUserId = auth.currentUser?.uid!!
        val userRef = firestore
            .collection(USERS_COLLECTION)
            .document(currentUserId)
        val userToFollowRef = firestore
            .collection(USERS_COLLECTION)
            .document(selectedUserId)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            val userFollowing =
                (snapshot.get(USERS_FOLLOWING_FIELD) as List<*>).mapNotNull { it as String }

            if (selectedUserId in userFollowing) {
                // If the user is already in the array, remove it
                transaction.update(
                    userRef,
                    USERS_FOLLOWING_FIELD,
                    FieldValue.arrayRemove(selectedUserId)
                )
                // Update the User info inside the cache
                val currentFollowing: MutableList<String> =
                    viewUserCache.data.following.toMutableList()
                currentFollowing.remove(selectedUserId)
                viewUserCache.updateUserInfo(
                    following = currentFollowing.toList()
                )
                // Update also the "User to Support" Following array
                transaction.update(
                    userToFollowRef,
                    USERS_FOLLOWERS_FIELD,
                    FieldValue.arrayRemove(currentUserId)
                )
            } else {
                // If the post is not in the array, add it
                transaction.update(
                    userRef,
                    USERS_FOLLOWING_FIELD,
                    FieldValue.arrayUnion(selectedUserId)
                )
                // Update the User info inside the cache
                val currentFollowing: MutableList<String> =
                    viewUserCache.data.following.toMutableList()
                currentFollowing.add(selectedUserId)
                viewUserCache.updateUserInfo(
                    following = currentFollowing.toList()
                )
                // Update also the "User to Support" Following array
                transaction.update(
                    userToFollowRef,
                    USERS_FOLLOWERS_FIELD,
                    FieldValue.arrayUnion(currentUserId)
                )
            }
        }.await()
        Success(true)
    } catch (e: Exception) {
        Failure(e)
    }
}