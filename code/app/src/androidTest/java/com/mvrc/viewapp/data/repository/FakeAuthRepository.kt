package com.mvrc.viewapp.data.repository

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.mvrc.viewapp.data.cache.ViewUserCache
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.domain.repository.AuthRepository
import com.mvrc.viewapp.domain.repository.AuthStateResponse
import com.mvrc.viewapp.domain.repository.OneTapSignInResponse
import com.mvrc.viewapp.domain.repository.SendPasswordResetEmailResponse
import com.mvrc.viewapp.domain.repository.SignInResponse
import com.mvrc.viewapp.domain.repository.SignInWithGoogleResponse
import com.mvrc.viewapp.domain.repository.SignOutResponse
import com.mvrc.viewapp.domain.repository.SignUpResponse
import com.mvrc.viewapp.testUtils.Stubs
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class FakeAuthRepository @Inject constructor(
    private val viewUserCache: ViewUserCache
) : AuthRepository {
    private val _isFirstGoogleLogin = MutableStateFlow(true)
    private val _authStateFlow = MutableStateFlow(true)

    override val isFirstGoogleLoginStateFlow: StateFlow<Boolean> = _isFirstGoogleLogin

    override suspend fun oneTapSignInWithGoogle(): OneTapSignInResponse {
        val mockBeginSignInResult: BeginSignInResult = mockk()
        return Success(mockBeginSignInResult)
    }

    override suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): SignInWithGoogleResponse {
        // Simulate signing in with Google
        val isNewUser = false // Simulate not being a new user
        if (isNewUser) {
            // Simulate adding user to Firestore
            _isFirstGoogleLogin.value = true
            return Success(true)
        } else {
            _isFirstGoogleLogin.value = false
        }
        return Success(true)
    }

    override suspend fun firebaseSignUpWithEmailAndPassword(
        email: String,
        password: String
    ): SignUpResponse {
        // Simulate signing up with email and password

        _authStateFlow.emit(false)
        viewUserCache.write(Stubs.user)

        return Success(true)
    }

    override suspend fun firebaseSignInWithEmailAndPassword(
        email: String,
        password: String,
    ): SignInResponse {
        // Simulate signing in with email and password

        _authStateFlow.emit(false)
        viewUserCache.write(Stubs.user)

        return Success(true)
    }

    override suspend fun sendPasswordResetEmail(email: String): SendPasswordResetEmailResponse {
        // Simulate sending password reset email
        return Success(true)
    }

    override suspend fun signOut(): SignOutResponse {
        // Simulate signing out

        _authStateFlow.emit(true)

        return Success(true)
    }

    override fun getAuthState(viewModelScope: CoroutineScope): AuthStateResponse {
        return _authStateFlow.asStateFlow()
    }
}