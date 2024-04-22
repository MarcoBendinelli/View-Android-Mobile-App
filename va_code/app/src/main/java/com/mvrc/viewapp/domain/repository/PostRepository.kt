package com.mvrc.viewapp.domain.repository

import android.net.Uri
import com.mvrc.viewapp.data.model.Response
import com.mvrc.viewapp.data.model.ViewPost
import kotlinx.coroutines.flow.Flow

typealias PostsResponse = Response<List<ViewPost>>
typealias PostResponse = Response<ViewPost>
typealias BookmarkPostResponse = Response<Boolean>
typealias SaveNewPostResponse = Response<Boolean>

/**
 * Repository contract for handling [ViewPost] fetching.
 */
interface PostRepository {

    /**
     * Retrieve a [Flow] of a [ViewPost] list as [Response]s based on user_profile's selected filter.
     *
     * @param numOfPostsToDisplay The number of posts to display in the flow.
     * @param filter The filter to be applied to posts, such as `Newest` or a specific topic.
     * @return List of [ViewPost] as a [Response].
     */
    fun getForYouPosts(numOfPostsToDisplay: Long, filter: String): Flow<PostsResponse>

    /**
     * Retrieve a [Flow] of a [ViewPost] list as [Response]s based on the number of saves.
     *
     * @return List of [ViewPost] as a [Response].
     */
    fun getTrendingNowPosts(): Flow<PostsResponse>

    /**
     * Retrieve a [Flow] of a [ViewPost] list as [Response]s based on user_profile's following contributors.
     *
     * @param numOfPostsToDisplay The number of posts to display in the flow.
     * @return List of [ViewPost] as a [Response].
     */
    fun getFollowingPosts(numOfPostsToDisplay: Long): Flow<PostsResponse>

    /**
     * Retrieve a [Flow] of a bookmarked [ViewPost] list as [Response]s.
     *
     * @param numOfPostsToDisplay The number of posts to display in the flow.
     * @return List of [ViewPost] as a [Response].
     */
    fun getBookmarkedPosts(numOfPostsToDisplay: Long): Flow<PostsResponse>

    /**
     * Save on backend the bookmarked post by the current user_profile.
     *
     * @param selectedPostId The id of the selected post.
     * @return [Response] indicating the success or failure of the operation.
     */
    suspend fun bookmarkPost(selectedPostId: String): BookmarkPostResponse

    /**
     * Retrieve a [Flow] of a [ViewPost] list as [Response]s based on user_profile's search.
     *
     * @param numOfPostsToDisplay The number of posts to display in the flow.
     * @param searchedText The input text to search the posts.
     * @return List of [ViewPost] as a [Response].
     */
    fun getSearchedPosts(numOfPostsToDisplay: Long, searchedText: String): Flow<PostsResponse>

    /**
     * Save on backend the added post by the current user_profile.
     *
     * @param authorName The name of the post creator.
     * @param title The title of the post to be added.
     * @param subtitle The subtitle of the post to be added.
     * @param imageUri The image of the post to be added.
     * @param authorImageUrl The name of the post creator.
     * @param topic The topic of the post to be added.
     * @param body The body of the post to be added.
     * @param readTime The estimated read time of the post.
     * @return [Response] indicating the success or failure of the operation.
     */
    suspend fun savePost(
        authorName: String,
        authorImageUrl: String,
        body: String,
        imageUri: Uri?,
        readTime: String,
        subtitle: String,
        title: String,
        topic: String
    ): SaveNewPostResponse

    /**
     * Retrieve the post data saved on backend.
     *
     * @param postId The id of the post to fetch.
     * @return [Response] indicating the success or failure of the operation.
     */
    fun getPost(postId: String): Flow<PostResponse>

    /**
     * Retrieve a [Flow] of a [ViewPost] list as [Response]s based on the provided author id.
     *
     * @param numOfPostsToDisplay The number of posts to display in the flow.
     * @param userId The if of the user, posts creator.
     * @return List of [ViewPost] as a [Response].
     */
    fun getUserPosts(numOfPostsToDisplay: Long, userId: String): Flow<PostsResponse>
}