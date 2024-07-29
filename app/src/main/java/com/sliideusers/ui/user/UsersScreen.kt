package com.sliideusers.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sliideusers.R
import com.sliideusers.domain.model.user.User
import com.sliideusers.ui.theme.SliideUsersTheme
import com.sliideusers.ui.user.viewmodel.AddUserDialogUiState
import com.sliideusers.ui.user.viewmodel.UsersActions
import com.sliideusers.ui.user.viewmodel.UsersUiState

@Composable
fun ListAllUsersScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    uiState: UsersUiState,
    addUserUiState: AddUserDialogUiState,
    uiActions: UsersActions,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            if (uiState.errorMessageRes == null) {
                FloatingActionButton(
                    onClick = uiActions::onFabClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Icon(Icons.Filled.Add, null)
                }
            }
        }
    ) { innerPadding ->
        UsersBody(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
            addUserUiState = addUserUiState,
            uiActions = uiActions,
        )
    }
}

@Composable
fun UsersBody(
    modifier: Modifier = Modifier,
    uiState: UsersUiState,
    addUserUiState: AddUserDialogUiState,
    uiActions: UsersActions,
) {
    var isDeleteDialogVisible by rememberSaveable { mutableStateOf(false) }
    var selectedUserId by rememberSaveable { mutableStateOf<Int?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (uiState.isLoading) {
            LoadingIndicator()
        }

        if (uiState.errorMessageRes != null) {
            ErrorIndicator(
                errorMessage = stringResource(id = uiState.errorMessageRes),
                onRetry = uiActions::onErrorRetry,
            )
        }

        LazyColumn(
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.base_padding_half),
                end = dimensionResource(R.dimen.base_padding_half),
                bottom = dimensionResource(R.dimen.base_padding_half),
            )
        ) {
            items(uiState.users) { user ->
                UserCard(user) {
                    isDeleteDialogVisible = true
                    selectedUserId = user.id
                }
            }
        }

        if (addUserUiState.isVisible) {
            AddUserDialog(
                disableButton = addUserUiState.isLoading,
                errorRes = addUserUiState.errorMessageRes,
                onDismissRequest = uiActions::onAddUserDialogDismissed,
                onSave = uiActions::onAddUser
            )
        }

        if (isDeleteDialogVisible) {
            selectedUserId?.let {
                DeleteUserDialog(
                    userId = it,
                    onDismissRequest = { isDeleteDialogVisible = false },
                    onDeleteConfirmation = { userId ->
                        isDeleteDialogVisible = false
                        uiActions.onDeleteConfirmed(userId)
                    }
                )
            }
        }
    }
}

@Composable
fun UserCard(
    user: User,
    onUserLongPress: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.base_padding_half))
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onUserLongPress(user.id) }
                )
            },
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.base_elevation)),
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.base_padding)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(dimensionResource(R.dimen.name_initials_box_sie))
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.tertiary),
                contentAlignment = Alignment.Center
            ) {
                // Display Initials as no profile picture is provided
                Text(
                    text = user.initials,
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background.copy(alpha = 1f),
                shape = RoundedCornerShape(0.dp)
            )
            .wrapContentSize(Alignment.Center),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.tertiary,
            strokeWidth = dimensionResource(R.dimen.base_padding_quarter)
        )
    }
}

@Composable
fun ErrorIndicator(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 1f))
            .wrapContentSize(Alignment.Center)
            .padding(dimensionResource(R.dimen.base_padding)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.base_padding)))
            Button(
                modifier = Modifier.width(dimensionResource(R.dimen.base_button_width)),
                onClick = onRetry
            ) {
                Text(text = stringResource(R.string.retry))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingIndicatorPreview() {
    SliideUsersTheme {
        ListAllUsersScreen(
            snackbarHostState = SnackbarHostState(),
            uiState = UsersUiState(isLoading = true),
            addUserUiState = AddUserDialogUiState(),
            uiActions = UsersActions.DEFAULT
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorStatePreview() {
    SliideUsersTheme {
        ListAllUsersScreen(
            snackbarHostState = SnackbarHostState(),
            uiState = UsersUiState(errorMessageRes = R.string.error_no_internet),
            addUserUiState = AddUserDialogUiState(),
            uiActions = UsersActions.DEFAULT
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ListAllUsersScreenPreview() {
    val users = MutableList(10) { index ->
        User(id = index, name = "User $index", email = "Email$index")
    }
    SliideUsersTheme {
        ListAllUsersScreen(
            snackbarHostState = SnackbarHostState(),
            uiState = UsersUiState(
                users = users,
                isLoading = false
            ),
            addUserUiState = AddUserDialogUiState(),
            uiActions = UsersActions.DEFAULT
        )
    }
}