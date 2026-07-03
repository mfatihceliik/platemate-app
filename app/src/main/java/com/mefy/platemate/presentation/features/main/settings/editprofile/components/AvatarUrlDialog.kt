package com.mefy.platemate.presentation.features.main.settings.editprofile.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTextField
import com.mefy.platemate.presentation.components.model.PMTextStyle

@Composable
internal fun AvatarUrlDialog(
    url: String,
    onUrlChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { PMText(text = stringResource(R.string.edit_profile_avatar_dialog_title), style = PMTextStyle.Body) },
        text = {
            PMTextField(
                value = url,
                onValueChange = onUrlChange,
                placeholder = stringResource(R.string.edit_profile_avatar_dialog_hint),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                PMText(text = stringResource(R.string.common_save), style = PMTextStyle.Body)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                PMText(text = stringResource(R.string.common_cancel), style = PMTextStyle.Body)
            }
        }
    )
}