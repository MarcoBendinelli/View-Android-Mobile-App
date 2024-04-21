package com.mvrc.viewapp.presentation.sign_in.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.domain.event.ValidationEvent
import com.mvrc.viewapp.domain.repository.AuthRepository
import com.mvrc.viewapp.domain.repository.SignInResponse
import com.mvrc.viewapp.domain.use_case.validation.ValidateEmail
import com.mvrc.viewapp.domain.use_case.validation.ValidatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf
import javax.inject.Inject

/**
 * ViewModel for handling the Sign In functionality.
 *
 * @param repo The repository for authentication-related operations.
 * @param validateEmail The use case for validating email input.
 * @param validatePassword The use case for validating password input.
 */
@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
) : ViewModel() {

    // Mutable state representing the response of the Sign In operation.
    private val _signInResponseStateFlow = MutableStateFlow<SignInResponse>(Success(false))
    val signInResponseStateFlow = _signInResponseStateFlow.asStateFlow()

    // Mutable state representing the current state of the Sign In form.
    private val _signInFormStateFlow = MutableStateFlow(SignInFormState())
    val signInFormStateFlow = _signInFormStateFlow.asStateFlow()

    // Channel for sending validation events to the UI
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    /**
     * Handles events coming from the UI, such as changes in email or password fields.
     *
     * @param event The Sign In form event.
     */
    fun onEvent(event: SignInFormEvent) {
        // Validate email and password based on the current state.
        var emailResult = validateEmail.execute(_signInFormStateFlow.value.email)
        var passwordResult = validatePassword.execute(_signInFormStateFlow.value.password)

        // Validate event in base on the type.
        when (event) {
            // Validate email based on the user_profile's action.
            is SignInFormEvent.EmailChanged -> {
                emailResult = validateEmail.execute(event.email)
                _signInFormStateFlow.value = _signInFormStateFlow.value.copy(
                    email = event.email, emailError = emailResult.errorMessage,
                )
            }
            // Validate password based on the user_profile's action.
            is SignInFormEvent.PasswordChanged -> {
                passwordResult = validatePassword.execute(event.password)
                _signInFormStateFlow.value = _signInFormStateFlow.value.copy(
                    password = event.password, passwordError = passwordResult.errorMessage,
                )
            }
        }

        // Collect validation results and check for errors.
        val errors = immutableListOf(emailResult, passwordResult)
        val hasError = errors.any { !it.successful }

        // Send validation result to the UI.
        submitValidationResult(hasError)
    }

    /**
     * Sends validation results to the UI based on the presence of errors.
     *
     * @param hasError Boolean indicating whether there are validation errors.
     */
    private fun submitValidationResult(hasError: Boolean) {
        viewModelScope.launch {
            validationEventChannel.send(if (hasError) ValidationEvent.Failure else ValidationEvent.Success)
        }
    }

    /**
     * Initiates the Sign In with Email and Password operation.
     */
    fun signInWithEmailAndPassword() = viewModelScope.launch {
        _signInResponseStateFlow.value = Loading
        _signInResponseStateFlow.value =
            repo.firebaseSignInWithEmailAndPassword(
                _signInFormStateFlow.value.email,
                _signInFormStateFlow.value.password
            )
    }
}