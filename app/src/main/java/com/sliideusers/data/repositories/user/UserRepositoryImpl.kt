package com.sliideusers.data.repositories.user

import com.sliideusers.data.api.user.UserApiService
import com.sliideusers.data.api.user.common.models.ApiResult
import com.sliideusers.data.api.user.common.models.handleApiCall
import com.sliideusers.data.api.user.models.AddUserRequest
import com.sliideusers.data.api.user.toDomain
import com.sliideusers.domain.model.user.User
import com.sliideusers.domain.repositories.user.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService,
    private val appCoroutineDispatchers: CoroutineDispatcher,
) : UserRepository {
    override suspend fun getUsers(page: Int): ApiResult<List<User>> {
        return withContext(appCoroutineDispatchers) {
            handleApiCall(
                apiCall = { userApiService.getUsers(page) },
                mapOnSuccess = { userDtos -> userDtos.map { it.toDomain() } },
            )
        }
    }

    override suspend fun getUsersTotalPages(): ApiResult<Int> {
        return withContext(appCoroutineDispatchers) {
            try {
                val response = userApiService.getUsers()
                val totalUserPages = response.headers()[HEADER_PAGINATION_PAGES]?.toIntOrNull()
                if (totalUserPages != null) {
                    ApiResult.Success(totalUserPages)
                } else {
                    ApiResult.Fail.HttpError(response.code())
                }
            } catch (e: UnknownHostException) {
                ApiResult.Fail.NetworkFail
            } catch (e: Exception) {
                ApiResult.Fail.HttpFail
            }
        }
    }

    override suspend fun addUser(newUser: AddUserRequest): ApiResult<User> {
        return withContext(appCoroutineDispatchers) {
            handleApiCall(
                apiCall = { userApiService.addUser(newUser) },
                mapOnSuccess = { userDto -> userDto.toDomain() }
            )
        }
    }

    override suspend fun deleteUser(userId: Int): ApiResult<Unit> {
        return withContext(appCoroutineDispatchers) {
            handleApiCall(
                apiCall = { userApiService.deleteUser(userId) },
                mapOnSuccess = { }
            )
        }
    }

    companion object {
        const val HEADER_PAGINATION_PAGES = "x-pagination-pages"
    }
}