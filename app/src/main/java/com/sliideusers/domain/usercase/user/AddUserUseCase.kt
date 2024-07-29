package com.sliideusers.domain.usercase.user

import com.example.sliideusers.R
import com.sliideusers.data.api.user.common.models.ApiResult
import com.sliideusers.data.api.user.common.models.CommonResult
import com.sliideusers.data.api.user.common.models.toCommonResult
import com.sliideusers.data.api.user.models.AddUserRequest
import com.sliideusers.domain.model.user.User
import com.sliideusers.domain.repositories.user.UserRepository
import java.net.HttpURLConnection
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(name: String, email: String): CommonResult<User> {
        val response = userRepository.addUser(AddUserRequest(name = name, email = email))
        return if (response is ApiResult.Fail.HttpError && response.errorCode == HttpURLConnection.HTTP_MOVED_TEMP) {
            CommonResult.Error(R.string.error_on_save)
        } else {
            response.toCommonResult()
        }
    }
}