package com.mvrc.viewapp.data.repository

import android.net.Uri
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.model.ViewPost
import com.mvrc.viewapp.domain.repository.BookmarkPostResponse
import com.mvrc.viewapp.domain.repository.PostRepository
import com.mvrc.viewapp.domain.repository.PostResponse
import com.mvrc.viewapp.domain.repository.PostsResponse
import com.mvrc.viewapp.domain.repository.SaveNewPostResponse
import com.mvrc.viewapp.testUtils.Stubs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakePostRepository : PostRepository {

    override fun getForYouPosts(numOfPostsToDisplay: Long, filter: String): Flow<PostsResponse> {
        // Return a stub response for getting "For You" posts
        val posts: List<ViewPost> = listOf(Stubs.post1)
        return flowOf(Success(posts))
    }

    override fun getTrendingNowPosts(): Flow<PostsResponse> {
        // Return a stub response for getting "Trending Now" posts
        val posts: List<ViewPost> = listOf(Stubs.post1)
        return flowOf(Success(posts))
    }

    override fun getFollowingPosts(numOfPostsToDisplay: Long): Flow<PostsResponse> {
        // Return a stub response for getting "Following" posts
        val posts: List<ViewPost> = listOf(Stubs.post1)
        return flowOf(Success(posts))
    }

    override fun getBookmarkedPosts(numOfPostsToDisplay: Long): Flow<PostsResponse> {
        // Return a stub response for getting "Bookmarked" posts
        val posts: List<ViewPost> = listOf(Stubs.post1)
        return flowOf(Success(posts))
    }

    override suspend fun bookmarkPost(selectedPostId: String): BookmarkPostResponse {
        // Return a stub response for bookmarking a post
        return Success(true)
    }

    override fun getSearchedPosts(
        numOfPostsToDisplay: Long,
        searchedText: String
    ): Flow<PostsResponse> {
        // Return a stub response for searching posts
        val posts: List<ViewPost> = listOf(Stubs.post1)
        return flowOf(Success(posts))
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
    ): SaveNewPostResponse {
        // Return a stub response for saving a post
        return Success(true)
    }

    override fun getPost(postId: String): Flow<PostResponse> {
        // Return a stub response for getting a single post
        return flowOf(Success(Stubs.post1))
    }

    override fun getUserPosts(numOfPostsToDisplay: Long, userId: String): Flow<PostsResponse> {
        // Return a stub response for getting posts of a specific user
        val posts: List<ViewPost> = listOf(Stubs.post1)
        return flowOf(Success(posts))
    }
}