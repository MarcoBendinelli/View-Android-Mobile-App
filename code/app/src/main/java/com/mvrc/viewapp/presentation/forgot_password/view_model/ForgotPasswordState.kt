package com.mvrc.viewapp.presentation.forgot_password.view_model

/**
 * Data class representing the state of the Forgot Password screen.
 *
 * @property email The email input value.
 * @property emailError An optional error message related to the email input.
 */
data class ForgotPasswordState(
    val email: String = "",
    val emailError: String? = null,
)