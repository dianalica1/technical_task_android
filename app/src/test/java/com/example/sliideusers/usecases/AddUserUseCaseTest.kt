package com.example.sliideusers.usecases

import com.sliideusers.BaseTest
import com.sliideusers.data.api.user.common.models.ApiResult
import com.sliideusers.data.api.user.common.models.CommonResult
import com.sliideusers.data.api.user.models.AddUserRequest
import com.sliideusers.domain.model.user.User
import com.sliideusers.domain.repositories.user.UserRepository
import com.sliideusers.domain.usercase.user.AddUserUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.whenever

class AddUserUseCaseTest : BaseTest() {
    @Mock
     lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var addUserUseCase: AddUserUseCase

    private var addUserRequest = AddUserRequest(
        name = "name",
        email = "email",
    )

    private var user = User(
        id = 1,
        name = "name",
        email = "email",
    )

    @Test
    fun `GIVEN unexpected server error WHEN adding new user THEN expect error`(): Unit =
        runTest {
            whenever(userRepository.addUser(addUserRequest)).thenReturn(ApiResult.Fail.HttpFail)

            val result = addUserUseCase.invoke("name", "email")

            assert(result is CommonResult.Error)
        }

    @Test
    fun `GIVEN a server error WHEN adding new user THEN expect error`(): Unit =
        runTest {
            whenever(userRepository.addUser(addUserRequest)).thenReturn(ApiResult.Fail.HttpError(500))

            val result = addUserUseCase.invoke("name", "email")

            assert(result is CommonResult.Error)
        }

    @Test
    fun `GIVEN 302 server error WHEN adding new user THEN expect error`(): Unit =
        runTest {
            whenever(userRepository.addUser(addUserRequest)).thenReturn(ApiResult.Fail.HttpError(302))

            val result = addUserUseCase.invoke("name", "email")

            assert(result is CommonResult.Error)
        }

    @Test
    fun `GIVEN no internet connection WHEN adding new user THEN expect error`(): Unit =
        runTest {
            whenever(userRepository.addUser(addUserRequest)).thenReturn(ApiResult.Fail.NetworkFail)

            val result = addUserUseCase.invoke("name", "email")

            assert(result is CommonResult.Error)
        }

    @Test
    fun `GIVEN new info are valid WHEN adding new user THEN expect user info`(): Unit =
        runTest {
            whenever(userRepository.addUser(addUserRequest)).thenReturn(ApiResult.Success(data = user))

            val result = addUserUseCase.invoke("name", "email")

            assert(result is CommonResult.Success)
        }
}