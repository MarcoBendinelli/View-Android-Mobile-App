package com.mvrc.viewapp.domain.use_case.validation

/**
 * Data class representing the result of a validation operation.
 *
 * @property successful Indicates whether the validation was successful.
 * @property errorMessage An optional error message providing additional information.
 */
data class ValidationResult(val successful: Boolean, val errorMessage: String? = null)
