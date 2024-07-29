package com.sliideusers.ui.user.viewmodel

import androidx.annotation.StringRes
import com.sliideusers.domain.model.user.User

data class UsersUiState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    @StringRes val errorMessageRes: Int? = null,
    @StringRes val snackbarMessageRes: Int? = null,
)

data class AddUserDialogUiState(
    val isVisible: Boolean = false,
    val isLoading: Boolean = false,
    @StringRes val errorMessageRes: Int? = null,
)