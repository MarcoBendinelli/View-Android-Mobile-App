package com.mvrc.viewapp.presentation.sign_up.view_model

/**
 * Data class representing the state of the Sign Up form.
 *
 * @property email The email entered by the user_profile.
 * @property emailError An optional error message related to the email field.
 * @property password The password entered by the user_profile.
 * @property passwordError An optional error message related to the password field.
 * @property repeatedPassword The repeated password entered by the user_profile for confirmation.
 * @property repeatedPasswordError An optional error message related to the repeated password field.
 */
data class SignUpFormState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val repeatedPassword: String = "",
    val repeatedPasswordError: String? = null,
)