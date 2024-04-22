package com.mvrc.viewapp.domain.state

/**
 * Represents the pagination state for a list of items used in paginated data retrieval.
 * Here the large set of data is fetched into smaller chunks.
 *
 * @property numOfItems The number of items to retrieve in pagination.
 * @property endReached Indicates whether the end of the data has been reached. When set to `true`,
 * it means that there are no more items to load.
 */
data class PaginationState(
    val numOfItems: Long,
    val endReached: Boolean = true
)