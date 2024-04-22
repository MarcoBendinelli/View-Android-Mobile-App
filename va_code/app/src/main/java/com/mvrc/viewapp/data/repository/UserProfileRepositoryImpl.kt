package com.mvrc.viewapp.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mvrc.viewapp.core.Constants
import com.mvrc.viewapp.core.Constants.USERS_COLLECTION
import com.mvrc.viewapp.core.Constants.USERS_FOLLOWERS_FIELD
import com.mvrc.viewapp.core.Constants.USERS_FOLLOWING_FIELD
import com.mvrc.viewapp.core.Constants.USERS_PHOTO_URL_FIELD
import com.mvrc.viewapp.core.Constants.USERS_PROFESSION_FIELD
import com.mvrc.viewapp.core.Constants.USERS_TOPICS_FIELD
import com.mvrc.viewapp.core.Constants.USERS_USERNAME_FIELD
import com.mvrc.viewapp.data.cache.ViewUserCache
import com.mvrc.viewapp.data.model.Response.Failure
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.model.ViewUser
import com.mvrc.viewapp.domain.repository.FollowUserResponse
import com.mvrc.viewapp.domain.repository.UpdateUserResponse
import com.mvrc.viewapp.domain.repository.UserProfileRepository
import com.mvrc.viewapp.domain.repository.UserResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Implementation of the [UserProfileRepository] for managing user_profile profile-related operations.
 *
 * @property auth An instance of [FirebaseAuth] for retrieving the current user_profile logged in.
 * @property firestore An instance of [FirebaseFirestore] for Firestore database operations.
 * @property cloudStorage An instance of [FirebaseStorage] for accessing to the Cloud Storage.
 * @property viewUserCache The [ViewUserCache] to save the user_profile data.
 */
class UserProfileRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val cloudStorage: FirebaseStorage,
    private val viewUserCache: ViewUserCache
) : UserProfileRepository {

    override fun getUser(userId: String): Flow<UserResponse> = callbackFlow {
        val listener = firestore.collection(USERS_COLLECTION)
            .document(userId)
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
                    val user = ViewUser.fromFirestore(snapshot)
                    trySend(Success(user))
                }
            }

        // Remove the listener when the flow is cancelled
        awaitClose { listener.remove() }
    }.onStart { emit(Loading) }

    override suspend fun followUser(selectedUserId: String): FollowUserResponse = try {
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

    override suspend fun updateProfile(
        username: String,
        profession: String,
        topics: List<String>,
        photoUrl: Uri?
    ): UpdateUserResponse = try {
        val userId = auth.currentUser?.uid!!
        val userRef = firestore
            .collection(USERS_COLLECTION)
            .document(userId)

        // Create a map to store the fields that need to be updated
        val updatedFields = mutableMapOf(
            USERS_USERNAME_FIELD to username,
            USERS_PROFESSION_FIELD to profession,
            USERS_TOPICS_FIELD to topics
        )

        // Update the User info inside the cache
        viewUserCache.updateUserInfo(username = username, profession = profession, topics = topics)

        if (photoUrl != null) {
            val imageUrl: String = uploadImage(uri = photoUrl, id = userRef.id)
            updatedFields[USERS_PHOTO_URL_FIELD] = imageUrl
            viewUserCache.updateUserInfo(photoUrl = imageUrl)
        }

        userRef.update(
            updatedFields
        ).await()

        Success(true)
    } catch (e: Exception) {
        Failure(e)
    }

    /**
     * Uploads an image to Firebase Storage under the [USERS_COLLECTION]
     * with the provided [id] and returns the download URL of the uploaded image
     * upon successful upload. If any exception occurs during the upload process,
     * an empty string is returned.
     *
     * @param uri The image uri to be uploaded.
     * @param id The identifier used to name the image in Firebase Storage.
     * @return The download URL of the uploaded image or an empty string on failure.
     */
    private suspend fun uploadImage(uri: Uri, id: String): String {
        lateinit var imageURL: String
        val referenceImages =
            cloudStorage.reference.child(USERS_COLLECTION)
        val imageRef = referenceImages.child(id)

        return try {
            // Upload the image file to Firebase Storage.
            imageRef.putFile(uri).await()
            // Retrieve the download URL of the uploaded image.
            imageURL = imageRef.downloadUrl.await().toString()
            imageURL
        } catch (e: Exception) {
            Log.d(Constants.DEBUG_TAG, "savePost: $e")
            ""
        }
    }
}