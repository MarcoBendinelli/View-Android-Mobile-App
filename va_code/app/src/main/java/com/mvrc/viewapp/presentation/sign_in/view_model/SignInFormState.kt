package com.mvrc.viewapp.presentation.sign_in.view_model

/**
 * Data class representing the state of the Sign In form.
 *
 * @property email The email entered in the form.
 * @property emailError Error message related to the email field, or null if there's no error.
 * @property password The password entered in the form.
 * @property passwordError Error message related to the password field, or null if there's no error.
 */
data class SignInFormState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
)