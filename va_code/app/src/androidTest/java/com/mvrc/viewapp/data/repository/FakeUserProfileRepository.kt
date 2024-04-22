package com.mvrc.viewapp.data.repository

import android.net.Uri
import com.mvrc.viewapp.data.cache.ViewUserCache
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.domain.repository.FollowUserResponse
import com.mvrc.viewapp.domain.repository.UpdateUserResponse
import com.mvrc.viewapp.domain.repository.UserProfileRepository
import com.mvrc.viewapp.domain.repository.UserResponse
import com.mvrc.viewapp.testUtils.Stubs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeUserProfileRepository @Inject constructor(
    private val viewUserCache: ViewUserCache
) : UserProfileRepository {
    override fun getUser(userId: String): Flow<UserResponse> {

        // Simulating success state with a mock user
        return flowOf(Success(Stubs.user))
    }

    override suspend fun followUser(selectedUserId: String): FollowUserResponse {
        return Success(true)
    }

    override suspend fun updateProfile(
        username: String,
        profession: String,
        topics: List<String>,
        photoUrl: Uri?
    ): UpdateUserResponse {
        return Success(true)
    }

}