package com.mvrc.viewapp.di

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mvrc.viewapp.R
import com.mvrc.viewapp.data.cache.ViewUserCache
import com.mvrc.viewapp.data.repository.FakeAuthRepository
import com.mvrc.viewapp.data.repository.FakeFirstUserSelectionRepository
import com.mvrc.viewapp.data.repository.FakePostRepository
import com.mvrc.viewapp.data.repository.FakeUserProfileRepository
import com.mvrc.viewapp.domain.repository.AuthRepository
import com.mvrc.viewapp.domain.repository.FirstUserSelectionRepository
import com.mvrc.viewapp.domain.repository.PostRepository
import com.mvrc.viewapp.domain.repository.UserProfileRepository
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
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ViewModule::class]
)
object FakeViewModule {

    /**
     * Provides an instance of [FakeAuthRepository].
     *
     * @param cache The Cache to save the user_profile information.
     */
    @Provides
    @Singleton
    fun provideAuthRepository(cache: ViewUserCache): AuthRepository = FakeAuthRepository(
        viewUserCache = cache
    )

    /**
     * Provides an instance of [FakeUserProfileRepository].
     *
     * @param cache The Cache to save the user_profile information.
     */
    @Provides
    @Singleton
    fun provideProfileRepository(
        cache: ViewUserCache
    ): UserProfileRepository = FakeUserProfileRepository(
        viewUserCache = cache
    )

    /**
     * Provides an instance of [FakeFirstUserSelectionRepository].
     *
     * @param topics The list of topics to display.
     * @param cache The [ViewUserCache] to update the user_profile info.
     */
    @Provides
    @Singleton
    fun provideFirstUserSelectionRepository(
        topics: Array<out String>,
        cache: ViewUserCache
    ): FirstUserSelectionRepository = FakeFirstUserSelectionRepository(
        topics = topics,
        viewUserCache = cache,
    )

    /**
     * Provides an instance of [FakePostRepository].
     */
    @Provides
    @Singleton
    fun providePostRepository(): PostRepository = FakePostRepository()

    /**
     * Provides an instance of [PostUseCases].
     */
    @Provides
    @Singleton
    fun provideUseCases(
        repo: PostRepository
    ) = PostUseCases(
        getForYouPosts = GetForYouPosts(repo),
        getTrendingNowPosts = GetTrendingNowPosts(repo),
        getFollowingPosts = GetFollowingPosts(repo),
        getBookmarkedPosts = GetBookmarkedPosts(repo),
        getSearchedPosts = GetSearchedPosts(repo),
        bookmarkPost = BookmarkPost(repo),
        saveNewPost = SaveNewPost(repo),
        getPost = GetPost(repo),
        getUserPosts = GetUserPosts(repo)
    )

    /**
     * Provides an instance of [ViewUserCache] for caching user_profile data.
     */
    @Provides
    @Singleton
    fun provideCacheClient(): ViewUserCache = ViewUserCache()

    /**
     * Provides the list of topics saved inside the application resources.
     *
     * @param context The application context.
     * @return The array of strings representing the topics.
     */
    @Provides
    @Singleton
    fun provideTopics(@ApplicationContext context: Context): Array<out String> {
        return context.resources.getStringArray(R.array.topics)
    }

    /**
     * Provides "Newest" string saved inside the application resources.
     *
     * @param context The application context.
     * @return The Newest string.
     */
    @Provides
    @Singleton
    fun provideNewestString(@ApplicationContext context: Context): String {
        return context.resources.getString(R.string.newest)
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseStorage() = Firebase.storage
}