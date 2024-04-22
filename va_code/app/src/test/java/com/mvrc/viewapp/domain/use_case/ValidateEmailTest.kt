package com.mvrc.viewapp.domain.use_case

import com.mvrc.viewapp.domain.use_case.validation.ValidateEmail
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Unit test for the `ValidateEmail` use case.
 *
 * @see emailValidator_WrongEmail_ReturnsFalse
 * @see emailValidator_CorrectEmail_ReturnsTrue
 */
class ValidateEmailTest {
    private lateinit var validateEmail: ValidateEmail

    @Before
    fun setUp() {
        validateEmail = ValidateEmail(
            "The email can't be blank",
            "That's not a valid email"
        )
    }

    /**
     * Validates the behavior of the use case when provided with
     * poorly formatted email strings. It checks if the validation fails with the expected error
     * messages for various invalid email formats.
     */
    @Test
    fun emailValidator_WrongEmail_ReturnsFalse() {
        var result = validateEmail.execute("")
        assertEquals(false, result.successful)
        assertEquals("The email can't be blank", result.errorMessage)

        result = validateEmail.execute("testing")
        assertEquals(false, result.successful)
        assertEquals("That's not a valid email", result.errorMessage)

        result = validateEmail.execute("test@")
        assertEquals(false, result.successful)
        assertEquals("That's not a valid email", result.errorMessage)

        result = validateEmail.execute("test@test")
        assertEquals(false, result.successful)
        assertEquals("That's not a valid email", result.errorMessage)

        result = validateEmail.execute("test@test.")
        assertEquals(false, result.successful)
        assertEquals("That's not a valid email", result.errorMessage)
    }

    /**
     * Validates the behavior of the use case when provided with
     * a well-formatted email string. It checks if the validation succeeds without any errors.
     */
    @Test
    fun emailValidator_CorrectEmail_ReturnsTrue() {
        val result = validateEmail.execute("mario@mario.org")
        assertEquals(true, result.successful)
    }
}