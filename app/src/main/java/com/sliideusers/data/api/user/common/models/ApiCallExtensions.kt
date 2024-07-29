package com.sliideusers.data.api.user.common.models

import retrofit2.Response
import java.net.UnknownHostException

// General function to handle API calls
suspend fun <T, R> handleApiCall(
    apiCall: suspend () -> Response<T>,
    mapOnSuccess: (T) -> R,
): ApiResult<R> {
    return try {
        val response = apiCall()
        val body = response.body()

        when {
            response.isSuccessful && body != null -> ApiResult.Success(mapOnSuccess(body))
            response.isSuccessful -> ApiResult.NoContent
            else -> ApiResult.Fail.HttpError(response.code())
        }
    } catch (e: UnknownHostException) {
        ApiResult.Fail.NetworkFail
    } catch (e: Exception) {
        ApiResult.Fail.HttpFail
    }
}
