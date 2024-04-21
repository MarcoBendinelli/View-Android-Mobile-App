package com.mvrc.viewapp.domain.use_case.validation

/**
 * Use case for password validation. It checks if the provided password meets certain criteria:
 * - Has a minimum length of characters.
 * - Contains at least one letter and one digit.
 *
 * @property atLeastCharactersMessage Custom message retrieved from the resources.
 * @property atLeastDigitOrNumberMessage Custom message retrieved from the resources.
 */
class ValidatePassword(
    private val atLeastCharactersMessage: String,
    private val atLeastDigitOrNumberMessage: String
) {

    // Minimum characters required for the password
    private val minCharacters = 6

    /**
     * Executes the password validation logic.
     *
     * @param password The password to be validated.
     * @return A [ValidationResult] indicating the success of the validation.
     */
    fun execute(password: String): ValidationResult {

        // Check if the password meets the minimum length requirement
        val hasMinimum = validateMinimum(password)

        if (!hasMinimum) {
            return ValidationResult(
                successful = false,
                errorMessage = atLeastCharactersMessage
            )
        }

        // Check if the password contains at least one letter and one digit
        val containsLettersAndDigits =
            password.any { it.isDigit() } && password.any { it.isLetter() }

        if (!containsLettersAndDigits) {
            return ValidationResult(
                successful = false,
                errorMessage = atLeastDigitOrNumberMessage
            )
        }

        // If both checks pass, return a successful result
        return ValidationResult(successful = true)
    }

    /**
     * Validates if the password meets the minimum length requirement.
     *
     * @param password The password to be validated.
     * @return `true` if the password meets the minimum length requirement, `false` otherwise.
     */
    private fun validateMinimum(password: String): Boolean =
        password.length >= minCharacters
}
