package com.sliideusers.domain.usercase.user

import com.sliideusers.data.api.user.common.models.ApiResult
import com.sliideusers.data.api.user.common.models.CommonResult
import com.sliideusers.data.api.user.common.models.toCommonResult
import com.sliideusers.domain.repositories.user.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: Int): CommonResult<Unit> {
        val response = userRepository.deleteUser(userId)
        return if (response is ApiResult.NoContent) {
            CommonResult.Success(data = Unit)
        } else {
            response.toCommonResult()
        }
    }
}