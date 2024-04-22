package com.mvrc.viewapp.domain.repository

import android.net.Uri
import com.mvrc.viewapp.data.model.Response
import com.mvrc.viewapp.data.model.ViewUser
import kotlinx.coroutines.flow.Flow

typealias UserResponse = Response<ViewUser>
typealias FollowUserResponse = Response<Boolean>
typealias UpdateUserResponse = Response<Boolean>

/**
 * Repository contract defining operations related to User Profile.
 */
interface UserProfileRepository {

    /**
     * Retrieve the user profile data saved on backend.
     *
     * @param userId The id of the user_profile to fetch.
     * @return [Response] indicating the success or failure of the operation.
     */
    fun getUser(userId: String): Flow<UserResponse>

    /**
     * Save on backend the followed contributor by the current user.
     *
     * @param selectedUserId The id of the selected post.
     * @return [Response] indicating the success or failure of the operation.
     */
    suspend fun followUser(selectedUserId: String): FollowUserResponse

    /**
     * Update the information of the current user.
     *
     * @param username The updated username of the current user.
     * @param profession The updated profession of the current user.
     * @param topics The list of updated topics of the current user.
     * @param photoUrl The updated photo url of the current user.
     * @return [Response] indicating the success or failure of the operation.
     */
    suspend fun updateProfile(
        username: String,
        profession: String,
        topics: List<String>,
        photoUrl: Uri?
    ): UpdateUserResponse
}