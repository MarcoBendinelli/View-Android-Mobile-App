package com.mvrc.viewapp.domain.use_case.validation

import androidx.core.util.PatternsCompat

/**
 * Use case for email validation. It checks if the provided email is not blank and
 * matches the email format using AndroidX PatternsCompat.
 *
 * @property emailNotBlankMessage Custom message retrieved from the resources.
 * @property invalidEmailMessage Custom message retrieved from the resources.
 */
class ValidateEmail(
    private val emailNotBlankMessage: String,
    private val invalidEmailMessage: String
) {

    /**
     * Executes the email validation logic.
     *
     * @param email The email to be validated.
     * @return A [ValidationResult] indicating the success of the validation.
     */
    fun execute(email: String): ValidationResult {
        // Check if the email is blank
        if (email.isBlank()) {
            return ValidationResult(successful = false, errorMessage = emailNotBlankMessage)
        }
        // Check if the email matches the email format using AndroidX PatternsCompat
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(successful = false, errorMessage = invalidEmailMessage)
        }
        // If both checks pass, return a successful result
        return ValidationResult(successful = true)
    }
}