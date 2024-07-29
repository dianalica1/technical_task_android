package com.example.sliideusers.usecases

import com.sliideusers.BaseTest
import com.sliideusers.data.api.user.common.models.ApiResult
import com.sliideusers.data.api.user.common.models.CommonResult
import com.sliideusers.domain.model.user.User
import com.sliideusers.domain.repositories.user.UserRepository
import com.sliideusers.domain.usercase.user.GetLatestUsersUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.whenever

class GetLatestUsersUseCaseTest : BaseTest() {
    @Mock
     lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var getLatestUsersUseCase: GetLatestUsersUseCase

    private var users = listOf(
        User(
            id = 1,
            name = "Name",
            email = "email",
        ),
        User(
            id = 2,
            name = "Name",
            email = "email"
        ),
    )

    @Test
    fun `GIVEN an unexpected server error WHEN getting latest users THEN expect error`(): Unit =
        runTest {
            whenever(userRepository.getUsersTotalPages()).thenReturn(ApiResult.Fail.HttpFail)

            val result = getLatestUsersUseCase.invoke()

            assert(result is CommonResult.Error)
        }

    @Test
    fun `GIVEN a server error on getting the total pages WHEN getting latest users THEN expect error`(): Unit =
        runTest {
            whenever(userRepository.getUsersTotalPages()).thenReturn(ApiResult.Fail.HttpError(500))

            val result = getLatestUsersUseCase.invoke()

            assert(result is CommonResult.Error)
        }

    @Test
    fun `GIVEN no internet connection WHEN getting latest users THEN expect error`(): Unit =
        runTest {
            whenever(userRepository.getUsersTotalPages()).thenReturn(ApiResult.Fail.NetworkFail)

            val result = getLatestUsersUseCase.invoke()

            assert(result is CommonResult.Error)
        }

    @Test
    fun `GIVEN no users available WHEN getting latest users THEN expect no content result`(): Unit =
        runTest {
            whenever(userRepository.getUsersTotalPages()).thenReturn(ApiResult.NoContent)

            val result = getLatestUsersUseCase.invoke()

            assert(result is CommonResult.NoContent)
        }

    @Test
    fun `GIVEN users available WHEN getting latest users THEN expect users info`(): Unit =
        runTest {
            whenever(userRepository.getUsersTotalPages()).thenReturn(ApiResult.Success(data = 2))
            whenever(userRepository.getUsers(2)).thenReturn(ApiResult.Success(data = users))

            val result = getLatestUsersUseCase.invoke()

            assert(result is CommonResult.Success)
        }
}