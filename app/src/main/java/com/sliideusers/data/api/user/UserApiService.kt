package com.sliideusers.data.api.user

import com.sliideusers.data.api.user.models.AddUserRequest
import com.sliideusers.data.api.user.models.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {

    @GET("v2/users")
    suspend fun getUsers(
        @Query("page") page: Int = 0,
    ): Response<List<UserDto>>

    @POST("v2/users")
    suspend fun addUser(
        @Body user: AddUserRequest
    ): Response<UserDto>

    @DELETE("v2/users/{userId}")
    suspend fun deleteUser(
        @Path("userId") userId: Int
    ): Response<Unit>
}