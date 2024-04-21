package com.mvrc.viewapp.presentation.forgot_password.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.domain.event.ValidationEvent
import com.mvrc.viewapp.domain.repository.AuthRepository
import com.mvrc.viewapp.domain.repository.SendPasswordResetEmailResponse
import com.mvrc.viewapp.domain.use_case.validation.ValidateEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf
import javax.inject.Inject

/**
 * ViewModel for the Forgot Password screen, responsible for handling user_profile input, validation, and
 * interacting with the authentication repository to send a password reset email.
 *
 * @param repo Authentication repository for communication with the authentication backend.
 * @param validateEmail Use case for validating email addresses.
 */
@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val validateEmail: ValidateEmail
) : ViewModel() {

    // Holds the response from the password reset email sending operation.
    private val _sendPasswordResetEmailResponseStateFlow =
        MutableStateFlow<SendPasswordResetEmailResponse>(Success(false))
    val sendPasswordResetEmailResponseStateFlow =
        _sendPasswordResetEmailResponseStateFlow.asStateFlow()

    // Holds the state of the Forgot Password screen, including email validation errors.
    private val _forgotPasswordStateFlow =
        MutableStateFlow(ForgotPasswordState())
    val forgotPasswordStateFlow = _forgotPasswordStateFlow.asStateFlow()

    // Channel for sending validation events to the UI.
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    /**
     * Handles user_profile input events from the UI, such as changes in the email field.
     *
     * @param event The event representing the user_profile's action.
     */
    fun onEvent(event: ForgotPasswordEvent) {
        // Validate email based on the current state
        var emailResult = validateEmail.execute(_forgotPasswordStateFlow.value.email)

        when (event) {
            is ForgotPasswordEvent.EmailChanged -> {
                // Validate email based on the user_profile's action
                emailResult = validateEmail.execute(event.email)
                // Update the state
                _forgotPasswordStateFlow.value = _forgotPasswordStateFlow.value.copy(
                    email = event.email, emailError = emailResult.errorMessage,
                )
            }
        }

        // Collect validation errors and check for any errors
        val errors = immutableListOf(emailResult)
        val hasError = errors.any { !it.successful }

        // Submit the validation result to the UI
        submitValidationResult(hasError)
    }

    /**
     * Sends the validation result to the UI using the validation event channel.
     *
     * @param hasError Indicates whether there are validation errors.
     */
    private fun submitValidationResult(hasError: Boolean) {
        viewModelScope.launch {
            validationEventChannel.send(if (hasError) ValidationEvent.Failure else ValidationEvent.Success)
        }
    }

    /**
     * Sends the password reset email using the provided email address.
     */
    fun sendPasswordResetEmail() = viewModelScope.launch {
        // Initiate the loading state before making the request
        _sendPasswordResetEmailResponseStateFlow.value = Loading
        // Send the password reset email and update the response state
        _sendPasswordResetEmailResponseStateFlow.value =
            repo.sendPasswordResetEmail(_forgotPasswordStateFlow.value.email)
    }

    /**
     * Reset [MutableStateFlow] that handles the reset email response to its initial value.
     */
    fun resetSendResetEmailStateFlow() {
        _sendPasswordResetEmailResponseStateFlow.value = Success(false)
    }
}