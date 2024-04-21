package com.mvrc.viewapp.data.model

/**
 * Sealed class representing the different states of a response coming from the backend.
 *
 * @param T The type of data associated with the response.
 * @see Loading
 * @see Success
 * @see Failure
 */
sealed class Response<out T> {
    /**
     * Represents the loading state of a response.
     */
    object Loading : Response<Nothing>()

    /**
     * Represents a successful response.
     *
     * @property data The data associated with the successful response.
     */
    data class Success<out T>(
        val data: T?
    ) : Response<T>()

    /**
     * Represents a failure response.
     *
     * @property e The exception associated with the failure.
     */
    data class Failure(
        val e: Exception
    ) : Response<Nothing>()
}