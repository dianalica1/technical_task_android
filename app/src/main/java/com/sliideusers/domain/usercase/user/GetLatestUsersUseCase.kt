package com.sliideusers.domain.usercase.user

import com.sliideusers.data.api.user.common.models.ApiResult
import com.sliideusers.data.api.user.common.models.CommonResult
import com.sliideusers.data.api.user.common.models.toCommonResult
import com.sliideusers.domain.model.user.User
import com.sliideusers.domain.repositories.user.UserRepository
import javax.inject.Inject

class GetLatestUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): CommonResult<List<User>> {
        return when (val pageTotalResponse = userRepository.getUsersTotalPages()) {
            is ApiResult.Success -> userRepository.getUsers(pageTotalResponse.data).toCommonResult()
            else -> pageTotalResponse.toCommonResult { emptyList() }
        }
    }
}