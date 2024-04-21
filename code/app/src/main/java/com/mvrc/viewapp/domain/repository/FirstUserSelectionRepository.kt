package com.mvrc.viewapp.domain.repository

import com.mvrc.viewapp.data.model.Response
import com.mvrc.viewapp.data.model.ViewTopic
import com.mvrc.viewapp.data.model.ViewUser
import kotlinx.coroutines.flow.Flow

typealias FetchingUsersResponse = Response<List<ViewUser>>
typealias SaveUsersResponse = Response<Boolean>
typealias SaveTopicsResponse = Response<Boolean>

/**
 * Repository contract for handling first user_profile selections, such as topics and contributors.
 */
interface FirstUserSelectionRepository {

    /**
     * Retrieves a list of [ViewTopic] representing available topics.
     *
     * @return List of [ViewTopic].
     */
    fun getTopics(): List<ViewTopic>

    /**
     * Retrieves the list of most followed [ViewUser] as a [Flow] [Response].
     */
    fun getMostFollowedUsers(): Flow<FetchingUsersResponse>

    /**
     * Saves the selected topics on backend.
     *
     * @param selectedTopics List of [ViewTopic] to be saved.
     * @return [Response] indicating the success or failure of the operation.
     */
    suspend fun saveTopics(selectedTopics: List<ViewTopic>): SaveTopicsResponse

    /**
     * Saves the ids of the selected users on backend.
     *
     * @param selectedUserId The selected user id to follow / unfollow.
     * @return [Response] indicating the success or failure of the operation.
     */
    suspend fun saveUser(selectedUserId: String): SaveUsersResponse
}