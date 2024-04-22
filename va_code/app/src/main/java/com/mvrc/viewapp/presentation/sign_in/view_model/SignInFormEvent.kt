package com.mvrc.viewapp.presentation.sign_in.view_model

/**
 * Sealed class representing events related to the Sign In form.
 */
sealed class SignInFormEvent {
    /**
     * Event indicating a change in the email input field.
     *
     * @param email The updated email value.
     */
    data class EmailChanged(val email: String) : SignInFormEvent()

    /**
     * Event indicating a change in the password input field.
     *
     * @param password The updated password value.
     */
    data class PasswordChanged(val password: String) : SignInFormEvent()
}