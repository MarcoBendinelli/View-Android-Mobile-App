package com.mvrc.viewapp.domain.repository

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.mvrc.viewapp.data.model.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

typealias OneTapSignInResponse = Response<BeginSignInResult>
typealias SignInWithGoogleResponse = Response<Boolean>
typealias SignUpResponse = Response<Boolean>
typealias SignInResponse = Response<Boolean>
typealias SendPasswordResetEmailResponse = Response<Boolean>
typealias AuthStateResponse = StateFlow<Boolean>
typealias SignOutResponse = Response<Boolean>

/**
 * It represents the contract for authentication-related operations,
 * including sign-in, sign-up, sign-out, and more.
 */
interface AuthRepository {
    /**
     * Indicates whether the current authentication involves the user_profile's first login using Google.
     */
    val isFirstGoogleLoginStateFlow: StateFlow<Boolean>

    /**
     * Initiates a one-tap sign-in with Google and returns the response.
     */
    suspend fun oneTapSignInWithGoogle(): OneTapSignInResponse

    /**
     * Signs in with the provided Google credentials and returns the response.
     */
    suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): SignInWithGoogleResponse

    /**
     * Signs up a new user_profile with the provided email and password and returns the response.
     */
    suspend fun firebaseSignUpWithEmailAndPassword(email: String, password: String): SignUpResponse

    /**
     * Signs in a user_profile with the provided email and password and returns the response.
     */
    suspend fun firebaseSignInWithEmailAndPassword(email: String, password: String): SignInResponse

    /**
     * Sends a password reset email to the provided email address and returns the response.
     */
    suspend fun sendPasswordResetEmail(email: String): SendPasswordResetEmailResponse

    /**
     * Signs out the current user_profile and returns the response.
     */
    suspend fun signOut(): SignOutResponse

    /**
     * Retrieves the authentication state as a [StateFlow] to observe changes.
     */
    fun getAuthState(viewModelScope: CoroutineScope): AuthStateResponse
}