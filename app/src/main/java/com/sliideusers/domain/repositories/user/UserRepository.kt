package com.sliideusers.domain.repositories.user

import com.sliideusers.data.api.user.common.models.ApiResult
import com.sliideusers.data.api.user.models.AddUserRequest
import com.sliideusers.domain.model.user.User

interface UserRepository {
    suspend fun getUsers(page: Int): ApiResult<List<User>>
    suspend fun getUsersTotalPages(): ApiResult<Int>
    suspend fun addUser(newUser: AddUserRequest): ApiResult<User>
    suspend fun deleteUser(userId: Int): ApiResult<Unit>
}