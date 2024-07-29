package com.sliideusers.ui.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sliideusers.R
import com.sliideusers.data.api.user.common.models.CommonResult
import com.sliideusers.domain.usercase.user.AddUserUseCase
import com.sliideusers.domain.usercase.user.DeleteUserUseCase
import com.sliideusers.domain.usercase.user.GetLatestUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * [UsersViewModel] holds information about users.
 */
@HiltViewModel
class UsersViewModel @Inject constructor(
    val getLatestUsersUseCase: GetLatestUsersUseCase,
    val addUserUseCase: AddUserUseCase,
    val deleteUserUseCase: DeleteUserUseCase,
) : ViewModel(), UsersActions {

    private val _uiState = MutableStateFlow(
        UsersUiState(
            isLoading = true,
            users = emptyList(),
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _addUserUiState = MutableStateFlow(
        AddUserDialogUiState(
            isVisible = false
        )
    )
    val addUserUiState = _addUserUiState.asStateFlow()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessageRes = null) }

            when (val response = getLatestUsersUseCase()) {
                is CommonResult.Success -> {
                    _uiState.update { it.copy(users = response.data, isLoading = false) }
                }
                is CommonResult.Error -> {
                    _uiState.update {
                        it.copy(errorMessageRes = response.errorMessageRes, isLoading = false)
                    }
                }
                is CommonResult.NoContent -> {
                    _uiState.update {
                        it.copy(users = emptyList(), isLoading = false)
                    }
                }
            }
        }
    }

    override fun onErrorRetry() {
        fetchUsers()
    }

    override fun onFabClick() {
        _addUserUiState.update { it.copy(isVisible = true) }
    }

    override fun onAddUser(name: String, email: String) {
        viewModelScope.launch {
            _addUserUiState.update { it.copy(isLoading = true, errorMessageRes = null) }

            when (val result = addUserUseCase(name, email)) {
                is CommonResult.Success -> {
                    val users = _uiState.value.users + result.data
                    _uiState.update {
                        it.copy(
                            users = users,
                            snackbarMessageRes = R.string.user_successfully_created
                        )
                    }
                    _addUserUiState.update { it.copy(isVisible = false, isLoading = false) }
                }
                is CommonResult.Error -> {
                    _addUserUiState.update {
                        it.copy(
                            errorMessageRes = result.errorMessageRes,
                            isLoading = false
                        )
                    }
                }
                is CommonResult.NoContent -> {
                    _addUserUiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    override fun onDeleteConfirmed(userId: Int) {
        viewModelScope.launch {
            val deleteUserResult = deleteUserUseCase(userId)
            if (deleteUserResult is CommonResult.Success) {
                Timber.e("delete user success")
                val users = _uiState.value.users.filterNot { it.id == userId }
                _uiState.update {
                    it.copy(
                        users = users,
                        snackbarMessageRes = R.string.user_successfully_deleted
                    )
                }
            } else {
                Timber.e("delete user error")
                _uiState.update {
                    it.copy(
                        snackbarMessageRes = R.string.user_delete_error
                    )
                }
            }
        }
    }

    override fun onAddUserDialogDismissed() {
        _addUserUiState.update { it.copy(isVisible = false, isLoading = false) }
    }

    override fun onSnackDisplayed() {
        _uiState.update { it.copy(snackbarMessageRes = null) }
    }
}