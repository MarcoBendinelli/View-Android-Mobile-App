package com.mvrc.viewapp.data.repository

import com.mvrc.viewapp.data.cache.ViewUserCache
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.model.ViewTopic
import com.mvrc.viewapp.domain.repository.FetchingUsersResponse
import com.mvrc.viewapp.domain.repository.FirstUserSelectionRepository
import com.mvrc.viewapp.domain.repository.SaveTopicsResponse
import com.mvrc.viewapp.domain.repository.SaveUsersResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeFirstUserSelectionRepository @Inject constructor(
    topics: Array<out String>,
    private val viewUserCache: ViewUserCache
) : FirstUserSelectionRepository {

    private val viewTopics = mutableListOf<ViewTopic>()

    init {
        viewTopics.addAll(
            topics
                .map { topic -> ViewTopic(topic) }
        )
    }

    override fun getTopics(): List<ViewTopic> =
        viewTopics.toList()

    override fun getMostFollowedUsers(): Flow<FetchingUsersResponse> {
        return flowOf(Success(emptyList()))
    }

    override suspend fun saveTopics(selectedTopics: List<ViewTopic>): SaveTopicsResponse {
        return Success(true)
    }

    override suspend fun saveUser(selectedUserId: String): SaveUsersResponse {
        return Success(true)
    }

}