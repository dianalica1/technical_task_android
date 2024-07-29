package com.example.sliideusers.usecases

import com.sliideusers.BaseTest
import com.sliideusers.data.api.user.common.models.ApiResult
import com.sliideusers.data.api.user.common.models.CommonResult
import com.sliideusers.domain.repositories.user.UserRepository
import com.sliideusers.domain.usercase.user.DeleteUserUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class DeleteUserUseCaseTest : BaseTest() {
    @Mock
     lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var deleteUserUseCase: DeleteUserUseCase

    @Test
    fun `GIVEN unexpected server error WHEN deleting an user THEN expect error`(): Unit =
        runTest {
            whenever( userRepository.deleteUser(any())).thenReturn(ApiResult.Fail.HttpFail)

            val result = deleteUserUseCase.invoke(any())

            assert(result is CommonResult.Error)
        }

    @Test
    fun `GIVEN a server error WHEN deleting an user THEN expect error`(): Unit =
        runTest {
            whenever( userRepository.deleteUser(any())).thenReturn(ApiResult.Fail.HttpError(500))

            val result = deleteUserUseCase.invoke(any())

            assert(result is CommonResult.Error)
        }

    @Test
    fun `GIVEN no internet connection WHEN deleting an user THEN expect error`(): Unit =
        runTest {
            whenever(userRepository.deleteUser(any())).thenReturn(ApiResult.Fail.NetworkFail)

            val result = deleteUserUseCase.invoke(any())

            assert(result is CommonResult.Error)
        }

    @Test
    fun `GIVEN userId WHEN deleting an user THEN expect success`(): Unit =
        runTest {
            whenever(userRepository.deleteUser(any())).thenReturn(ApiResult.Success(Unit))

            val result = deleteUserUseCase.invoke(any())

            assert(result is CommonResult.Success)
        }
}