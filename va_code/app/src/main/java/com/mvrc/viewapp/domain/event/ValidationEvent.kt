package com.mvrc.viewapp.domain.event

/**
 * Sealed class representing events that can occur during a validation process.
 */
sealed class ValidationEvent {
    /**
     * Object representing a successful validation event.
     */
    object Success : ValidationEvent()

    /**
     * Object representing an error in the validation process.
     */
    object Failure : ValidationEvent()
}
