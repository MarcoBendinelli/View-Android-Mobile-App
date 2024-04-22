package com.mvrc.viewapp.domain.use_case.validation

/**
 * Use case for validating repeated passwords. It checks if the provided password matches
 * a repeated password for confirmation.
 *
 * @property passwordsDoNotMatchMessage Custom message retrieved from the resources.
 */
class ValidateRepeatedPassword(private val passwordsDoNotMatchMessage: String) {

    /**
     * Executes the repeated password validation logic.
     *
     * @param password The original password.
     * @param repeatedPassword The repeated password for confirmation.
     * @return A [ValidationResult] indicating the success of the validation.
     */
    fun execute(password: String, repeatedPassword: String): ValidationResult {
        // Check if the provided password matches the repeated password
        if (password != repeatedPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = passwordsDoNotMatchMessage
            )
        }

        // If the passwords match, return a successful result
        return ValidationResult(successful = true)
    }
}
