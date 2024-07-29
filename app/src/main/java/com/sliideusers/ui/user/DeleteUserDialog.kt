package com.sliideusers.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.sliideusers.R

@Composable
fun DeleteUserDialog(
    userId: Int,
    onDismissRequest: () -> Unit,
    onDeleteConfirmation: (Int) -> Unit,
) {

    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(R.dimen.base_padding_half)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    modifier = Modifier.width(dimensionResource(R.dimen.base_button_width_half)),
                    onClick = onDismissRequest
                ) {
                    Text(stringResource(R.string.cancel))
                }
                Button(
                    modifier = Modifier.width(dimensionResource(R.dimen.base_button_width_half)),
                    onClick = { onDeleteConfirmation(userId) }
                ) {
                    Text(stringResource(R.string.ok))
                }
            }
        },
        title = {
            Text(
                text = stringResource(R.string.delete_user_confirmation_question),
                style = MaterialTheme.typography.titleLarge,
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun DeleteUserDialogPreview() {
    DeleteUserDialog(
        userId = 1,
        onDismissRequest = {},
        onDeleteConfirmation = { _ -> }
    )
}