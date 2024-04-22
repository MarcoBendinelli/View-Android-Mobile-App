package com.mvrc.viewapp.di

import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.Constants.SIGN_IN_REQUEST
import com.mvrc.viewapp.core.Constants.SIGN_UP_REQUEST
import com.mvrc.viewapp.data.cache.ViewUserCache
import com.mvrc.viewapp.data.repository.AuthRepositoryImpl
import com.mvrc.viewapp.data.repository.FirstUserSelectionRepositoryImpl
import com.mvrc.viewapp.data.repository.PostRepositoryImpl
import com.mvrc.viewapp.data.repository.UserProfileRepositoryImpl
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
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing dependencies related to the View App within the ViewModel component.
 * This module includes methods for providing repositories related to authentication, user_profile profiles, and user_profile selections.
 *
 * @see [ViewModule.provideAuthRepository]
 * @see [ViewModule.provideProfileRepository]
 * @see [ViewModule.provideFirstUserSelectionRepository]
 * @see [ViewModule.provideTopics]
 * @see [ViewModule.provideFirebaseFirestore]
 * @see [ViewModule.provideFirebaseAuth]
 */
@Module
@InstallIn(SingletonComponent::class)
object ViewModule {

    /**
     * Provides an instance of [AuthRepository] for handling authentication operations.
     *
     * @param auth The Firebase authentication instance.
     * @param oneTapClient The Google Identity Toolkit sign-in client.
     * @param signInRequest The sign-in request for one-tap sign-in.
     * @param signUpRequest The sign-up request for one-tap sign-in.
     * @param firestore The Firebase Firestore instance.
     * @param cache The Cache to save the user_profile information.
     * @return The implementation of [AuthRepository].
     */
    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        oneTapClient: SignInClient,
        @Named(SIGN_IN_REQUEST)
        signInRequest: BeginSignInRequest,
        @Named(SIGN_UP_REQUEST)
        signUpRequest: BeginSignInRequest,
        firestore: FirebaseFirestore,
        cache: ViewUserCache
    ): AuthRepository = AuthRepositoryImpl(
        auth = auth,
        oneTapClient = oneTapClient,
        signInRequest = signInRequest,
        signUpRequest = signUpRequest,
        firestore = firestore,
        viewUserCache = cache
    )

    /**
     * Provides an instance of [UserProfileRepository] for handling profile-related operations.
     *
     * @param auth The Firebase authentication instance.
     * @param firestore The Firebase Firestore instance.
     * @param cloudStorage The Firebase Storage instance.
     * @param cache The Cache to save the user_profile information.
     * @return The implementation of [UserProfileRepository].
     */
    @Provides
    @Singleton
    fun provideProfileRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth,
        cloudStorage: FirebaseStorage,
        cache: ViewUserCache
    ): UserProfileRepository = UserProfileRepositoryImpl(
        firestore = firestore,
        auth = auth,
        cloudStorage = cloudStorage,
        viewUserCache = cache
    )

    /**
     * Provides an instance of [FirstUserSelectionRepository] for handling user_profile's first selections.
     *
     * @param firestore The Firebase Firestore instance.
     * @param auth The Firebase authentication instance.
     * @param topics The list of topics to display.
     * @param cache The [ViewUserCache] to update the user_profile info.
     * @return The implementation of [FirstUserSelectionRepository].
     */
    @Provides
    @Singleton
    fun provideFirstUserSelectionRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth,
        topics: Array<out String>,
        cache: ViewUserCache
    ): FirstUserSelectionRepository = FirstUserSelectionRepositoryImpl(
        firestore = firestore,
        topics = topics,
        viewUserCache = cache,
        auth = auth
    )

    /**
     * Provides an instance of [PostRepository] for handling posts fetching.
     *
     * @param auth The Firebase authentication instance.
     * @param firestore The Firebase Firestore instance.
     * @param cloudStorage The Firebase Storage instance.
     * @return The implementation of [PostRepository].
     */
    @Provides
    @Singleton
    fun providePostRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        cloudStorage: FirebaseStorage
    ): PostRepository = PostRepositoryImpl(
        firestore = firestore,
        auth = auth,
        cloudStorage = cloudStorage
    )

    /**
     * Provides an instance of [PostUseCases].
     *
     * @param repo The [PostRepository] used for post-related operations.
     * @return An instance of [PostUseCases] initialized with the provided [PostRepository].
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

    /**
     * Provides an instance of [FirebaseFirestore] for accessing Firebase Firestore.
     *
     * @return The Firebase Firestore instance.
     */
    @Provides
    @Singleton
    fun provideFirebaseFirestore() = Firebase.firestore

    /**
     * Provides an instance of [FirebaseAuth] for accessing Firebase Authentication.
     *
     * @return The Firebase Authentication instance.
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth() = Firebase.auth

    /**
     * Provides an instance of [storage] for accessing Firebase Cloud Storage.
     *
     * @return The Firebase Storage instance.
     */
    @Provides
    @Singleton
    fun provideFirebaseStorage() = Firebase.storage
}
