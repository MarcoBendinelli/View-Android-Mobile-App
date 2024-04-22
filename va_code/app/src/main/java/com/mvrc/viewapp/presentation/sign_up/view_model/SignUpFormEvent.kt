package com.mvrc.viewapp.presentation.sign_up.view_model

/**
 * Sealed class representing events related to the Sign Up form.
 */
sealed class SignUpFormEvent {

    /**
     * Event indicating a change in the email field.
     *
     * @param email The updated email value.
     */
    data class EmailChanged(val email: String) : SignUpFormEvent()

    /**
     * Event indicating a change in the password field.
     *
     * @param password The updated password value.
     */
    data class PasswordChanged(val password: String) : SignUpFormEvent()

    /**
     * Event indicating a change in the repeated password field.
     *
     * @param repeatedPassword The updated repeated password value.
     */
    data class RepeatedPasswordChanged(val repeatedPassword: String) : SignUpFormEvent()
}