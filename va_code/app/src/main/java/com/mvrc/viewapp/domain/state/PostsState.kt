package com.mvrc.viewapp.domain.state

import com.mvrc.viewapp.data.model.ViewPost
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Represents the state of the loaded [ViewPost]s (from the backend) used in the UI layer.
 *
 * @property isLoading Indicates whether data is currently being loaded.
 * @property posts A list of [ViewPost] objects representing the posts to be displayed.
 * @property error A string representing an error message.
 */
data class PostsState(
    val isLoading: Boolean = false,
    val posts: ImmutableList<ViewPost> = persistentListOf(),
    val error: String = ""
)