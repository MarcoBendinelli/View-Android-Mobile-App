package com.mvrc.viewapp.presentation.forgot_password.view_model

/**
 * Sealed class representing events related to the Forgot Password screen.
 */
sealed class ForgotPasswordEvent {
    /**
     * Event indicating a change in the email input.
     *
     * @param email The updated email value.
     */
    data class EmailChanged(val email: String) : ForgotPasswordEvent()
}