package com.mvrc.viewapp.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.mvrc.viewapp.core.Constants.DEBUG_TAG
import com.mvrc.viewapp.core.Constants.NEWEST_FILTER
import com.mvrc.viewapp.core.Constants.POSTS_AUTHOR_ID_FIELD
import com.mvrc.viewapp.core.Constants.POSTS_BOOKMARKED_BY_FIELD
import com.mvrc.viewapp.core.Constants.POSTS_COLLECTION
import com.mvrc.viewapp.core.Constants.POSTS_CREATED_AT_FIELD
import com.mvrc.viewapp.core.Constants.POSTS_ID_FIELD
import com.mvrc.viewapp.core.Constants.POSTS_IMAGE_URL_FIELD
import com.mvrc.viewapp.core.Constants.POSTS_TOPIC_FIELD
import com.mvrc.viewapp.core.Constants.USERS_COLLECTION
import com.mvrc.viewapp.core.Constants.USERS_FOLLOWING_FIELD
import com.mvrc.viewapp.data.model.Response
import com.mvrc.viewapp.data.model.Response.Failure
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.model.ViewPost
import com.mvrc.viewapp.data.model.ViewTopic
import com.mvrc.viewapp.domain.repository.BookmarkPostResponse
import com.mvrc.viewapp.domain.repository.PostRepository
import com.mvrc.viewapp.domain.repository.PostResponse
import com.mvrc.viewapp.domain.repository.PostsResponse
import com.mvrc.viewapp.domain.repository.SaveNewPostResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject

/**
 * Implementation of the [PostRepository] for managing [ViewPost] fetching.
 *
 * @property auth An instance of [FirebaseAuth] for retrieving the current user_profile logged in.
 * @property firestore An instance of [FirebaseFirestore] for Firestore database operations.
 * @property cloudStorage An instance of [FirebaseStorage] for accessing to the Cloud Storage.
 */
class PostRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val cloudStorage: FirebaseStorage
) : PostRepository {

    override fun getForYouPosts(
        numOfPostsToDisplay: Long, filter: String
    ): Flow<Response<List<ViewPost>>> = callbackFlow {
        val query: Query = if (filter == NEWEST_FILTER || filter == "Nuovi") {
            firestore.collection(POSTS_COLLECTION)
                .orderBy(POSTS_AUTHOR_ID_FIELD, Query.Direction.DESCENDING)
                .whereNotEqualTo(POSTS_AUTHOR_ID_FIELD, auth.currentUser?.uid)
                .limit(numOfPostsToDisplay)
        } else {
            firestore.collection(POSTS_COLLECTION).whereEqualTo(POSTS_TOPIC_FIELD, filter)
                .whereNotEqualTo(POSTS_AUTHOR_ID_FIELD, auth.currentUser?.uid)
                .orderBy(POSTS_AUTHOR_ID_FIELD, Query.Direction.DESCENDING)
                .orderBy(POSTS_CREATED_AT_FIELD, Query.Direction.DESCENDING)
                .limit(numOfPostsToDisplay)
        }

        val listener = query.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                trySend(
                    Failure(
                        exception
                    )
                )
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val posts = snapshot.documents.map { doc -> ViewPost.fromFirestore(doc) }
                trySend(Success(posts))
            }
        }

        // Remove the listener when the flow is cancelled
        awaitClose { listener.remove() }
    }.onStart { emit(Loading) }

    override fun getTrendingNowPosts(): Flow<Response<List<ViewPost>>> = callbackFlow {
        val listener = firestore.collection(POSTS_COLLECTION)
            .orderBy(POSTS_AUTHOR_ID_FIELD, Query.Direction.DESCENDING)
            .whereNotEqualTo(POSTS_AUTHOR_ID_FIELD, auth.currentUser?.uid)
            .orderBy(POSTS_CREATED_AT_FIELD, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

                if (exception != null) {
                    trySend(
                        Failure(
                            exception
                        )
                    )
                    return@addSnapshotListener
                }

                launch {
                    if (snapshot != null) {
                        val posts = postsFromSnapshot(snapshot)
                        trySend(Success(posts))
                    }
                }
            }

        // Remove the listener when the flow is cancelled
        awaitClose { listener.remove() }
    }.onStart { emit(Loading) }


    /**
     * Convert a query snapshot of posts to a list of [ViewPost] objects.
     * Additionally, retrieve the top two posts based on the number of saves
     * and sort the posts in the order specified by the top2PostIds list.
     *
     * @param snapshot The query snapshot of posts from Firestore.
     * @return A [List] of [ViewPost] objects representing the posts.
     */
    private suspend fun postsFromSnapshot(snapshot: QuerySnapshot): List<ViewPost> {
        // Retrieve all posts from Firestore to calculate top two posts.
        val postsSnapshot = firestore.collection(POSTS_COLLECTION).get().await()

        // Map to store the count of bookmarked posts for each post ID.
        var top2PostIds = listOf("Default")

        // Sort posts by the number of users who bookmarks them in descending order.
        val sortedPosts = postsSnapshot.documents.sortedBy {
            (it[POSTS_BOOKMARKED_BY_FIELD] as List<*>).size
        }

        // Retrieve the top two posts from the sorted list.
        val topTwoPosts = sortedPosts.takeLast(2).reversed()

        if (topTwoPosts.isNotEmpty()) {
            top2PostIds = topTwoPosts.map { it.id }
        }

        // Retrieve the posts from the snapshot.
        val trendingPosts = snapshot.documents.map { ViewPost.fromFirestore(it) }
            .filter { top2PostIds.contains(it.id) }.toList()

        // Sort the posts based on their order in top2PostIds list.
        trendingPosts.sortedBy { top2PostIds.indexOf(it.id) }

        return trendingPosts
    }

    override fun getFollowingPosts(
        numOfPostsToDisplay: Long,
    ): Flow<Response<List<ViewPost>>> = callbackFlow {
        // Get the ids of the following contributors
        val followingIdsUsers = getFollowingIdsUsers()

        val listener = firestore.collection(POSTS_COLLECTION)
            .orderBy(POSTS_CREATED_AT_FIELD, Query.Direction.DESCENDING)
            .whereIn(POSTS_AUTHOR_ID_FIELD, followingIdsUsers)
            .limit(numOfPostsToDisplay)
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
                    val posts = snapshot.documents.map { doc -> ViewPost.fromFirestore(doc) }
                    trySend(Success(posts))
                }
            }

        // Remove the listener when the flow is cancelled
        awaitClose { listener.remove() }
    }.onStart { emit(Loading) }

    /**
     * Retrieves the IDs of contributors the user_profile is following.
     *
     * @return A list of user_profile IDs representing contributors that the user_profile is following.
     */
    private suspend fun getFollowingIdsUsers(): List<String> {
        val userDocRef = firestore.collection(USERS_COLLECTION)
            .document(auth.currentUser!!.uid)

        var followingIdsUsers: List<String>

        // Retrieve the IDs of contributors the user_profile is following.
        val data = userDocRef.get().await().data
        followingIdsUsers =
            (data?.get(USERS_FOLLOWING_FIELD) as List<*>).mapNotNull { it as String }

        // [whereIn] filter requires a non-empty [Iterable].
        if (followingIdsUsers.isEmpty()) {
            followingIdsUsers = listOf("Default")
        }

        return followingIdsUsers
    }

    override fun getBookmarkedPosts(
        numOfPostsToDisplay: Long,
    ): Flow<PostsResponse> = callbackFlow {
        val listener = firestore.collection(POSTS_COLLECTION)
            .whereArrayContains(POSTS_BOOKMARKED_BY_FIELD, auth.currentUser!!.uid)
            .orderBy(POSTS_CREATED_AT_FIELD, Query.Direction.DESCENDING)
            .limit(numOfPostsToDisplay).addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    trySend(
                        Failure(
                            exception
                        )
                    )
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val posts = snapshot.documents.map { doc -> ViewPost.fromFirestore(doc) }
                    trySend(Success(posts))
                }
            }

        // Remove the listener when the flow is cancelled
        awaitClose { listener.remove() }
    }.onStart { emit(Loading) }

    override fun getSearchedPosts(
        numOfPostsToDisplay: Long,
        searchedText: String
    ): Flow<PostsResponse> = callbackFlow {
        val listener = firestore.collection(POSTS_COLLECTION)
            .orderBy(POSTS_AUTHOR_ID_FIELD, Query.Direction.DESCENDING)
            .whereNotEqualTo(POSTS_AUTHOR_ID_FIELD, auth.currentUser?.uid)
            .orderBy(POSTS_CREATED_AT_FIELD, Query.Direction.DESCENDING)
            .limit(numOfPostsToDisplay).addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    trySend(
                        Failure(
                            exception
                        )
                    )
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val posts = snapshot.documents.map { doc -> ViewPost.fromFirestore(doc) }
                        .filter { post ->
                            post.authorName.lowercase(Locale.getDefault()).contains(searchedText) ||
                                    post.title.lowercase(Locale.getDefault())
                                        .contains(searchedText) ||
                                    post.subtitle.lowercase(Locale.getDefault())
                                        .contains(searchedText) ||
                                    post.topic.topicName.lowercase(Locale.getDefault())
                                        .contains(searchedText)
                        }
                    trySend(Success(posts))
                }
            }

        // Remove the listener when the flow is cancelled
        awaitClose { listener.remove() }
    }.onStart { emit(Loading) }

    override suspend fun bookmarkPost(selectedPostId: String): BookmarkPostResponse = try {
        val postRef = firestore
            .collection(POSTS_COLLECTION)
            .document(selectedPostId)
        val currentUserId = auth.currentUser?.uid!!

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(postRef)
            val bookmarkedByUsers =
                (snapshot.get(POSTS_BOOKMARKED_BY_FIELD) as List<*>).mapNotNull { it as String }

            if (currentUserId in bookmarkedByUsers) {
                // If the post is already in the array, remove it
                transaction.update(
                    postRef,
                    POSTS_BOOKMARKED_BY_FIELD,
                    FieldValue.arrayRemove(currentUserId)
                )
            } else {
                // If the post is not in the array, add it
                transaction.update(
                    postRef,
                    POSTS_BOOKMARKED_BY_FIELD,
                    FieldValue.arrayUnion(currentUserId)
                )
            }
        }.await()
        Success(true)
    } catch (e: Exception) {
        Failure(e)
    }

    override suspend fun savePost(
        authorName: String,
        authorImageUrl: String,
        body: String,
        imageUri: Uri?,
        readTime: String,
        subtitle: String,
        title: String,
        topic: String
    ): SaveNewPostResponse = try {
        val postRef = firestore
            .collection(POSTS_COLLECTION)
            .document()
        val viewPost = ViewPost(
            authorId = auth.currentUser?.uid!!,
            authorName = authorName,
            authorPhotoUrl = authorImageUrl,
            id = "",
            title = title,
            subtitle = subtitle,
            body = body,
            imageUrl = "",
            createdAt = Timestamp.now(),
            topic = ViewTopic(
                topicName = topic
            ),
            readTime = readTime,
            bookmarkedBy = emptyList()
        )

        postRef.set(viewPost.toMap())
            .await()

        postRef.update(
            mapOf(
                POSTS_ID_FIELD to postRef.id
            )
        ).await()
        if (imageUri != null) {
            val imageUrl: String = uploadImage(uri = imageUri, id = postRef.id)
            postRef.update(
                mapOf(
                    POSTS_IMAGE_URL_FIELD to imageUrl
                )
            ).await()
        }
        Success(true)
    } catch (e: Exception) {
        Failure(e)
    }

    override fun getPost(postId: String): Flow<PostResponse> = callbackFlow {
        val listener = firestore.collection(POSTS_COLLECTION)
            .document(postId)
            .addSnapshotListener { snapshot, exception ->

                if (exception != null) {
                    trySend(
                        Failure(
                            exception
                        )
                    )
                    return@addSnapshotListener
                }

                launch {
                    if (snapshot != null) {
                        val post = ViewPost.fromFirestore(snapshot)
                        trySend(Success(post))
                    }
                }
            }

        // Remove the listener when the flow is cancelled
        awaitClose { listener.remove() }
    }.onStart { emit(Loading) }

    override fun getUserPosts(numOfPostsToDisplay: Long, userId: String): Flow<PostsResponse> =
        callbackFlow {
            val listener = firestore.collection(POSTS_COLLECTION)
                .orderBy(POSTS_AUTHOR_ID_FIELD, Query.Direction.DESCENDING)
                .whereEqualTo(POSTS_AUTHOR_ID_FIELD, userId)
                .orderBy(POSTS_CREATED_AT_FIELD, Query.Direction.DESCENDING)
                .limit(numOfPostsToDisplay).addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        trySend(
                            Failure(
                                exception
                            )
                        )
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val posts = snapshot.documents.map { doc -> ViewPost.fromFirestore(doc) }
                        trySend(Success(posts))
                    }
                }

            // Remove the listener when the flow is cancelled
            awaitClose { listener.remove() }
        }.onStart { emit(Loading) }

    /**
     * Uploads an image to Firebase Storage under the [POSTS_COLLECTION]
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
            cloudStorage.reference.child(POSTS_COLLECTION)
        val imageRef = referenceImages.child(id)

        return try {
            // Upload the image file to Firebase Storage.
            imageRef.putFile(uri).await()
            // Retrieve the download URL of the uploaded image.
            imageURL = imageRef.downloadUrl.await().toString()
            imageURL
        } catch (e: Exception) {
            Log.d(DEBUG_TAG, "savePost: $e")
            ""
        }
    }
}