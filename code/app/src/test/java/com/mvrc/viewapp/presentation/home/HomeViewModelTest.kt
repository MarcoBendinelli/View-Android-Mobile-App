package com.mvrc.viewapp.presentation.home

import com.mvrc.viewapp.data.cache.ViewUserCache
import com.mvrc.viewapp.data.repository.FakePostRepository
import com.mvrc.viewapp.domain.repository.PostRepository
import com.mvrc.viewapp.domain.use_case.post.BookmarkPost
import com.mvrc.viewapp.domain.use_case.post.GetBookmarkedPosts
import com.mvrc.viewapp.domain.use_case.post.GetFollowingPosts
import com.mvrc.viewapp.domain.use_case.post.GetForYouPosts
import com.mvrc.viewapp.domain.use_case.post.GetPost
import com.mvrc.viewapp.domain.use_case.post.GetSearchedPosts
import com.mvrc.viewapp.domain.use_case.post.GetTrendingNowPosts
import com.mvrc.viewapp.domain.use_case.post.GetUserPosts
import com.mvrc.viewapp.domain.use_case.post.PostUseCases
import com.mvrc.viewapp.domain.use_case.post.SaveNewPost
import com.mvrc.viewapp.testUtils.MainCoroutineRule
import io.mockk.clearAllMocks
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    /**
     * JUnit rule that applies the [MainCoroutineRule] to each test method in the test class.
     * This rule ensures that each test has access to the main dispatcher configured by [MainCoroutineRule].
     */
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: HomeViewModel
    private lateinit var postRepository: PostRepository
    private lateinit var postUseCases: PostUseCases
    private lateinit var viewUserCache: ViewUserCache

    @Before
    fun setup() {
        postRepository = FakePostRepository()
        postUseCases =
            PostUseCases(
                getForYouPosts = GetForYouPosts(postRepository),
                getTrendingNowPosts = GetTrendingNowPosts(postRepository),
                getFollowingPosts = GetFollowingPosts(postRepository),
                getBookmarkedPosts = GetBookmarkedPosts(postRepository),
                getSearchedPosts = GetSearchedPosts(postRepository),
                bookmarkPost = BookmarkPost(postRepository),
                saveNewPost = SaveNewPost(postRepository),
                getPost = GetPost(postRepository),
                getUserPosts = GetUserPosts(postRepository),
            )

        viewUserCache = mockk(relaxed = true)
        viewModel = HomeViewModel(postUseCases, viewUserCache, "Newest")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun selectFilter_Sets_selectedFilterState() {
        val filter = "Popular"
        viewModel.selectFilter(filter)
        assert(viewModel.selectedFilterStateFlow.value == filter)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadForYouPosts_works_correctly() = runTest {
        viewModel.loadForYouPosts(5)
        advanceUntilIdle()
        assertTrue(viewModel.forYouPostsStateFlow.value.posts.isNotEmpty())
    }
}
