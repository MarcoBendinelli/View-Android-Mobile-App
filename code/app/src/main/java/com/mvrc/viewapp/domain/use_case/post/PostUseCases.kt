package com.mvrc.viewapp.domain.use_case.post

import com.mvrc.viewapp.data.model.ViewPost

/**
 * Represents the different use cases used to [ViewPost] operations.
 *
 * @see [GetForYouPosts]
 * @see [GetTrendingNowPosts]
 * @see [GetFollowingPosts]
 * @see [GetBookmarkedPosts]
 * @see [GetSearchedPosts]
 * @see [BookmarkPost]
 * @see [SaveNewPost]
 * @see [GetPost]
 * @see [GetUserPosts]
 */
data class PostUseCases(
    val getForYouPosts: GetForYouPosts,
    val getTrendingNowPosts: GetTrendingNowPosts,
    val getFollowingPosts: GetFollowingPosts,
    val getBookmarkedPosts: GetBookmarkedPosts,
    val getSearchedPosts: GetSearchedPosts,
    val bookmarkPost: BookmarkPost,
    val saveNewPost: SaveNewPost,
    val getPost: GetPost,
    val getUserPosts: GetUserPosts,
)