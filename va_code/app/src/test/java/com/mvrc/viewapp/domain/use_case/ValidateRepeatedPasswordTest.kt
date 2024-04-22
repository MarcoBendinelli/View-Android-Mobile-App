package com.mvrc.viewapp.domain.use_case

import com.mvrc.viewapp.domain.use_case.validation.ValidateRepeatedPassword
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Unit test for the `ValidateRepeatedPassword` use case.
 *
 * @see repeatedPasswordValidator_NotEqualsPss_ReturnsFalse
 * @see repeatedPasswordValidator_EqualsPss_ReturnsTrue
 */
class ValidateRepeatedPasswordTest {
    private lateinit var validateRepeatedPassword: ValidateRepeatedPassword

    @Before
    fun setUp() {
        validateRepeatedPassword = ValidateRepeatedPassword("The passwords don't match")
    }

    /**
     * Validates the behavior of the use case when provided with
     * two not equal password strings. It checks if the validation fails with the expected error
     * message.
     */
    @Test
    fun repeatedPasswordValidator_NotEqualsPss_ReturnsFalse() {
        val result = validateRepeatedPassword.execute("test", "testing")
        assertEquals(false, result.successful)
        assertEquals("The passwords don't match", result.errorMessage)
    }

    /**
     * Validates the behavior of the use case when provided with
     * two equal password strings. It checks if the validation succeeds without any errors.
     */
    @Test
    fun repeatedPasswordValidator_EqualsPss_ReturnsTrue() {
        val result = validateRepeatedPassword.execute("test", "test")
        assertEquals(true, result.successful)
    }
}