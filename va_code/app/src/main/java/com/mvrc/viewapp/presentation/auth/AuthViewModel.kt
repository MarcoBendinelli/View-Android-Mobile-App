package com.mvrc.viewapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.domain.repository.AuthRepository
import com.mvrc.viewapp.domain.repository.OneTapSignInResponse
import com.mvrc.viewapp.domain.repository.SignInWithGoogleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for handling authentication-related logic in the application.
 *
 * @property authRepository The repository responsible for authentication operations.
 * @property oneTapClient The client for one-tap sign-in.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    val oneTapClient: SignInClient
) : ViewModel() {

    // Represents the response of one-tap sign-in operation.
    private val _oneTapSignInResponseStateFlow =
        MutableStateFlow<OneTapSignInResponse>(Success(null))
    val oneTapSignInResponseStateFlow = _oneTapSignInResponseStateFlow.asStateFlow()

    // Represents the response of sign-in with Google operation.
    private val _signInWithGoogleResponseStateFlow =
        MutableStateFlow<SignInWithGoogleResponse>(Success(false))
    val signInWithGoogleResponseStateFlow = _signInWithGoogleResponseStateFlow.asStateFlow()

    // Indicates whether it's the user_profile's first Google login.
    val isFirstGoogleLoginStateFlow: StateFlow<Boolean>
        get() = authRepository.isFirstGoogleLoginStateFlow

    /**
     * Initiates the one-tap sign-in process.
     */
    fun oneTapSignIn() = viewModelScope.launch {
        _oneTapSignInResponseStateFlow.value = Loading
        _oneTapSignInResponseStateFlow.value = authRepository.oneTapSignInWithGoogle()
    }

    /**
     * Initiates the sign-in with Google process using the provided Google credential.
     *
     * @param googleCredential The Google credential for authentication.
     */
    fun signInWithGoogle(googleCredential: AuthCredential) = viewModelScope.launch {
        _oneTapSignInResponseStateFlow.value = Loading
        _signInWithGoogleResponseStateFlow.value =
            authRepository.firebaseSignInWithGoogle(googleCredential)
    }
}
