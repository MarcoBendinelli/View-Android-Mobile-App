package com.mvrc.viewapp

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvrc.viewapp.core.Constants.DEBUG_TAG
import com.mvrc.viewapp.domain.repository.AuthRepository
import com.mvrc.viewapp.domain.state.AppStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing state related to user_profile authentication.
 * */
@HiltViewModel
class AppViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    /**
     * Represents the current status of the app, indicating whether the authentication process is complete.
     * This state is observed by Compose UI observers, triggering recomposition when the state changes.
     *
     * @see [MutableState] for more details on Composes state management.
     */
    private val _appStatus: MutableStateFlow<AppStatus> =
        MutableStateFlow(AppStatus.UNAUTHENTICATED)

    /**
     * Exposes the current [AppStatus] as a read-only property.
     * Clients observing this property will be notified of changes, triggering recomposition when the state changes.
     *
     * @see [appStatus] for the current [AppStatus].
     */
    val appStatus: MutableStateFlow<AppStatus> get() = _appStatus

    /**
     * It represents the redirection state.
     *
     * The [MutableStateFlow] holds a boolean value indicating whether
     * directly redirection to the home screen is needed.
     *
     * @see MutableStateFlow
     */
    private val _redirectUser: MutableStateFlow<Boolean> = MutableStateFlow(true)

    /**
     * Initializes the AppViewModel by observing changes in the authentication state and
     * updates the app status based on the authentication state changes.
     *
     * If users exit the application, they are redirected to the authentication screen.
     * If they are already authenticated and they don't need to do the first selection,
     * (they are not signing up) they are redirected to the Home screen.
     */
    init {
        viewModelScope.launch {
            _redirectUser.collectLatest { redirect ->
                if (redirect) {
                    // Collects the authentication state as a flow
                    getAuthState().collect { isUserNotAuthenticated ->
                        Log.d(DEBUG_TAG, "isUserNotAuthenticated: $isUserNotAuthenticated")
                        if (isUserNotAuthenticated) {
                            // Updates the app status on unauthenticated due to a sign out request.
                            _appStatus.value = AppStatus.UNAUTHENTICATED
                        }
                        if (!isUserNotAuthenticated && _appStatus.value != AppStatus.FIRST_SELECTION) {
                            // Updates the app status on authenticated, the user_profile can be redirected
                            // to the Home Screen.
                            _appStatus.value = AppStatus.AUTHENTICATED
                        }
                    }
                }
            }
        }
        viewModelScope.launch {
            _redirectUser.collect { redirect ->
                Log.d(DEBUG_TAG, "Redirect: $redirect")
            }
        }
        viewModelScope.launch {
            _appStatus.collect { appStatus ->
                Log.d(DEBUG_TAG, "AppStatus: $appStatus")
            }
        }
    }

    /**
     * Gets the authentication state as a flow.
     *
     * @return StateFlow indicating whether the user_profile is authenticated or not.
     */
    private fun getAuthState() = authRepository.getAuthState(viewModelScope)

    /**
     * Marks the start of the user_profile selection process, setting [AppStatus] to first_selection.
     * This occurs when the users start the selection of the topics and users
     * in the first selection process.
     */
    fun onStartUserSelection() {
        _appStatus.value = AppStatus.FIRST_SELECTION
    }

    /**
     * Marks the end of the user_profile selection process, setting [AppStatus] to authenticated.
     * This occurs when the users end the selection of the topics and users phase.
     * Now the user_profile can be redirect to the Home screen.
     */
    fun onEndUserSelection() {
        _appStatus.value = AppStatus.AUTHENTICATED
    }

    /**
     * Enable the redirection of the user_profile to the Home Page.
     */
    fun enableUserRedirection() {
        _redirectUser.value = true
    }

    /**
     * Updates the app status based on the user_profile's first Google sign-in.
     *
     * If it is the user_profile's first time signing in with Google, a selection process
     * is required. Otherwise, the user_profile is redirected to the home screen.
     *
     * @param firstGoogleLogIn True if it's the user_profile's first Google sign-in, false otherwise.
     */
    fun onGoogleSignIn(firstGoogleLogIn: Boolean) {
        _appStatus.value = if (firstGoogleLogIn) {
            AppStatus.FIRST_SELECTION
        } else {
            AppStatus.AUTHENTICATED
        }
        _redirectUser.value = true
    }

    /**
     * Delays redirection to the home or topic selection screen until the user_profile's
     * presence in the database is confirmed. Use this method to temporarily pause
     * listening to the authentication state and await the result of the Google sign-in request.
     * Be sure to reset the state in case of a failure.
     */
    fun notRedirectUser() {
        _redirectUser.value = false
    }
}
