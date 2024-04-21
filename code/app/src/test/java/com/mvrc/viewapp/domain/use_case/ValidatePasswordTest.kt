package com.mvrc.viewapp.domain.use_case

import com.mvrc.viewapp.domain.use_case.validation.ValidatePassword
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Unit test for the `ValidatePassword` use case.
 *
 * @see passwordValidator_WrongPassword_ReturnsFalse
 * @see passwordValidator_CorrectPassword_ReturnsTrue
 */
class ValidatePasswordTest {

    private lateinit var validatePassword: ValidatePassword

    @Before
    fun setUp() {
        validatePassword = ValidatePassword(
            "The password needs to consist of at least 6 characters",
            "The password needs to contain at least one letter or digit"
        )
    }

    /**
     * Validates the behavior of the use case when provided with
     * poorly formatted password strings. It checks if the validation fails with the expected error
     * messages for various invalid password formats.
     */
    @Test
    fun passwordValidator_WrongPassword_ReturnsFalse() {
        var result = validatePassword.execute("testing")
        assertEquals(result.successful, false)
        assertEquals(
            result.errorMessage,
            "The password needs to contain at least one letter or digit"
        )

        result = validatePassword.execute("a")
        assertEquals(result.successful, false)
        assertEquals(
            result.errorMessage,
            "The password needs to consist of at least 6 characters"
        )
    }

    /**
     * Validates the behavior of the use case when provided with
     * a well-formatted password string. It checks if the validation succeeds without any errors.
     */
    @Test
    fun passwordValidator_CorrectPassword_ReturnsTrue() {
        val result = validatePassword.execute("testing123")
        assertEquals(result.successful, true)
    }
}