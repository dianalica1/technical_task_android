package com.example.sliideusers.repositories

import com.sliideusers.BaseTest
import com.sliideusers.data.api.user.UserApiService
import com.sliideusers.data.api.user.common.models.ApiResult
import com.sliideusers.data.api.user.models.AddUserRequest
import com.sliideusers.data.api.user.models.UserDto
import com.sliideusers.data.api.user.toDomain
import com.sliideusers.data.repositories.user.UserRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection
import okhttp3.Headers

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryImplTest : BaseTest() {
    @Mock
    private lateinit var userApiService: UserApiService

    private lateinit var testCoroutineScheduler: TestCoroutineScheduler
    private lateinit var testDispatcher: TestDispatcher

    private lateinit var userRepository: UserRepositoryImpl

    @Before
    fun setUp() {
        testCoroutineScheduler = TestCoroutineScheduler()
        testDispatcher = StandardTestDispatcher(testCoroutineScheduler)
        Dispatchers.setMain(testDispatcher)
        userRepository = UserRepositoryImpl(
            userApiService = userApiService,
            appCoroutineDispatchers = testDispatcher,
        )
    }

    private val userDtos = listOf(
        UserDto(id = 1, name = "Name1", email = "email1@example.com"),
        UserDto(id = 2, name = "Name2", email = "email2@example.com")
    )

    @Test
    fun `getUsers should return success result on successful API call`() = runTest {
        val response = Response.success(userDtos)
        whenever(userApiService.getUsers(1)).thenReturn(response)

        val result = userRepository.getUsers(1)

        assert(result is ApiResult.Success && result.data == userDtos.map { it.toDomain() })
        verify(userApiService).getUsers(1)
    }

    @Test
    fun `getUsers should return error result on error API call`() = runTest {
        val responseBody = byteArrayOf().toResponseBody()
        val response =
            Response.error<List<UserDto>>(HttpsURLConnection.HTTP_UNAUTHORIZED, responseBody)
        whenever(userApiService.getUsers(1)).thenReturn(response)

        val result = userRepository.getUsers(1)

        assert(result is ApiResult.Fail.HttpError)
        verify(userApiService).getUsers(1)
    }

    @Test
    fun `getUsers should return error result on exception in API call`() = runTest {
        whenever(userApiService.getUsers(1)).thenThrow(RuntimeException())

        val result = userRepository.getUsers(1)

        assert(result is ApiResult.Fail.HttpFail)
        verify(userApiService).getUsers(1)
    }

    @Test
    fun `addUser should return user on successful API call`() = runTest {
        val request = AddUserRequest(
            name = "Test",
            email = "test@gmail.com"
        )
        val response = Response.success(UserDto(id = 1, name = request.name, email = request.email))
        whenever(userApiService.addUser(request)).thenReturn(response)

        val result = userRepository.addUser(request)

        assert(
            result is ApiResult.Success &&
                    result.data.name == request.name &&
                    result.data.email == request.email
        )
        verify(userApiService).addUser(request)
    }

    @Test
    fun `deleteUser should return result on successful API call`() = runTest {
        val userId = 1
        val response = Response.success(Unit)
        whenever(userApiService.deleteUser(userId)).thenReturn(response)

        val result = userRepository.deleteUser(userId)

        assert(result is ApiResult.Success)
        verify(userApiService).deleteUser(userId)
    }

    @Test
    fun `getUsersTotalPages should return success result on successful API call`() = runTest {
        val headers = Headers.Builder()
            .add(UserRepositoryImpl.HEADER_PAGINATION_PAGES, "4")
            .build()
        val response = Response.success(userDtos, headers)
        whenever(userApiService.getUsers()).thenReturn(response)

        val result = userRepository.getUsersTotalPages()

        assert(result is ApiResult.Success && result.data == 4)
        verify(userApiService).getUsers()
    }

    @Test
    fun `getUsersTotalPages should return error result on exception in API call`() = runTest {
        whenever(userApiService.getUsers()).thenThrow(RuntimeException())

        val result = userRepository.getUsersTotalPages()

        assert(result is ApiResult.Fail.HttpFail)
        verify(userApiService).getUsers()
    }
}