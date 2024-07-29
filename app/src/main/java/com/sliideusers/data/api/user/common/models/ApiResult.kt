package com.sliideusers.data.api.user.common.models

import androidx.annotation.StringRes
import com.example.sliideusers.R

/**
 * A generic type used to encapsulate Retrofit responses, distinguishing between successful outcomes and
 * various failure types for detailed error handling.
 */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data object NoContent : ApiResult<Nothing>()

    sealed class Fail : ApiResult<Nothing>() {
        data class HttpError(val errorCode: Int) : ApiResult<Nothing>()
        data object HttpFail : ApiResult<Nothing>()
        data object NetworkFail : ApiResult<Nothing>()
    }
}

sealed class CommonResult<out T> {
    data class Success<T>(val data: T) : CommonResult<T>()
    data object NoContent : CommonResult<Nothing>()
    data class Error<T>(@StringRes val errorMessageRes: Int) : CommonResult<T>()
}

fun <T> ApiResult<T>.toCommonResult(): CommonResult<T> {
    return when (this) {
        is ApiResult.Success -> CommonResult.Success(data)
        ApiResult.NoContent -> CommonResult.NoContent
        is ApiResult.Fail.HttpError,
        is ApiResult.Fail.HttpFail -> CommonResult.Error(R.string.error_occurred)
        is ApiResult.Fail.NetworkFail -> CommonResult.Error(R.string.error_no_internet)
    }
}

fun <T1, T2> ApiResult<T1>.toCommonResult(successMapper: (T1) -> T2): CommonResult<T2> {
    return when (this) {
        is ApiResult.Success -> CommonResult.Success(successMapper(data))
        is ApiResult.NoContent -> CommonResult.NoContent
        is ApiResult.Fail.HttpError,
        is ApiResult.Fail.HttpFail -> CommonResult.Error(R.string.error_occurred)
        is ApiResult.Fail.NetworkFail -> CommonResult.Error(R.string.error_no_internet)
    }
}