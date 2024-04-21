package com.mvrc.viewapp.presentation.sign_up.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.domain.event.ValidationEvent
import com.mvrc.viewapp.domain.repository.AuthRepository
import com.mvrc.viewapp.domain.repository.SignUpResponse
import com.mvrc.viewapp.domain.use_case.validation.ValidateEmail
import com.mvrc.viewapp.domain.use_case.validation.ValidatePassword
import com.mvrc.viewapp.domain.use_case.validation.ValidateRepeatedPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf
import javax.inject.Inject

/**
 * ViewModel responsible for managing the Sign Up screen's logic and user_profile interactions.
 *
 * @property authRepository The repository for authentication-related operations.
 * @property validateEmail The use case for validating email input.
 * @property validatePassword The use case for validating password input.
 * @property validateRepeatedPassword The use case for validating repeated password input.
 */
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val validateRepeatedPassword: ValidateRepeatedPassword,
) : ViewModel() {

    // Represents the response from the sign-up operation.
    private val _signUpResponseStateFlow = MutableStateFlow<SignUpResponse>(Success(false))
    val signUpResponseStateFlow = _signUpResponseStateFlow.asStateFlow()

    // Represents the current state of the Sign Up form.
    private val _signUpFormStateFlow = MutableStateFlow(SignUpFormState())
    val signUpFormStateFlow = _signUpFormStateFlow.asStateFlow()

    // Channel for sending validation events to the UI
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    /**
     * Handles user_profile events from the UI, updating the form state and triggering validation.
     *
     * @param event The Sign Up form event triggered by user_profile interaction.
     */
    fun onEvent(event: SignUpFormEvent) {
        // Validate email, password and repeated password based on the current state.
        var emailResult = validateEmail.execute(_signUpFormStateFlow.value.email)
        var passwordResult = validatePassword.execute(_signUpFormStateFlow.value.password)
        var repeatedPasswordResult = validateRepeatedPassword.execute(
            _signUpFormStateFlow.value.password,
            _signUpFormStateFlow.value.repeatedPassword
        )

        // Validate event in base on the type.
        when (event) {
            // Validate email based on the user_profile's action.
            is SignUpFormEvent.EmailChanged -> {
                emailResult = validateEmail.execute(event.email)
                _signUpFormStateFlow.value = _signUpFormStateFlow.value.copy(
                    email = event.email, emailError = emailResult.errorMessage,
                )
            }
            // Validate password based on the user_profile's action.
            is SignUpFormEvent.PasswordChanged -> {
                passwordResult = validatePassword.execute(event.password)
                repeatedPasswordResult = validateRepeatedPassword.execute(
                    event.password,
                    _signUpFormStateFlow.value.repeatedPassword
                )
                _signUpFormStateFlow.value = _signUpFormStateFlow.value.copy(
                    password = event.password, passwordError = passwordResult.errorMessage,
                    repeatedPasswordError = repeatedPasswordResult.errorMessage
                )
            }
            // Validate repeated password based on the user_profile's action.
            is SignUpFormEvent.RepeatedPasswordChanged -> {
                repeatedPasswordResult = validateRepeatedPassword.execute(
                    _signUpFormStateFlow.value.password,
                    event.repeatedPassword
                )
                _signUpFormStateFlow.value = _signUpFormStateFlow.value.copy(
                    repeatedPassword = event.repeatedPassword,
                    repeatedPasswordError = repeatedPasswordResult.errorMessage,
                )
            }
        }

        // Collect validation results and check for errors.
        val errors = immutableListOf(emailResult, passwordResult, repeatedPasswordResult)
        val hasError = errors.any { !it.successful }

        // Send validation result to the UI.
        submitValidationResult(hasError)
    }

    /**
     * Sends the validation result to the UI, indicating whether there are errors.
     *
     * @param hasError Boolean indicating whether there are validation errors.
     */
    private fun submitValidationResult(hasError: Boolean) {
        viewModelScope.launch {
            validationEventChannel.send(if (hasError) ValidationEvent.Failure else ValidationEvent.Success)
        }
    }

    /**
     * Initiates the sign-up process by sending the provided email and password to the authentication repository.
     */
    fun signUpWithEmailAndPassword() = viewModelScope.launch {
        _signUpResponseStateFlow.value = Loading
        _signUpResponseStateFlow.value =
            authRepository.firebaseSignUpWithEmailAndPassword(
                _signUpFormStateFlow.value.email,
                _signUpFormStateFlow.value.password
            )
    }
}