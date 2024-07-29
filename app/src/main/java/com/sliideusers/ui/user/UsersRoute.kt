package com.sliideusers.ui.user

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sliideusers.ui.user.viewmodel.UsersActions
import com.sliideusers.ui.user.viewmodel.UsersViewModel

@Composable
fun UsersRoute(
    modifier: Modifier = Modifier,
    viewModel: UsersViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiActions = viewModel as UsersActions
    val addUserUiState by viewModel.addUserUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    uiState.snackbarMessageRes?.let { snackbarMessageRes ->
        val displayMessage = stringResource(snackbarMessageRes)
        LaunchedEffect(key1 = displayMessage, key2 = snackbarHostState) {
            snackbarHostState.showSnackbar(message = displayMessage)
            uiActions.onSnackDisplayed()
        }
    }

    ListAllUsersScreen(
        modifier = modifier,
        uiState = uiState,
        addUserUiState = addUserUiState,
        snackbarHostState = snackbarHostState,
        uiActions = uiActions,
    )
}