package com.mefy.platemate.presentation.features.admin.moderation.comments.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTextField
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.moderation.comments.ModerationAction
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun ReasonDialog(
    action: ModerationAction,
    reason: String,
    onReasonChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val titleRes = when (action) {
        ModerationAction.REJECT -> R.string.admin_reason_dialog_reject_title
        ModerationAction.REMOVE -> R.string.admin_reason_dialog_remove_title
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { PMText(text = stringResource(titleRes), style = PMTextStyle.Body) },
        text = {
            PMTextField(
                value = reason,
                onValueChange = onReasonChange,
                placeholder = stringResource(R.string.admin_reason_hint),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                PMText(text = stringResource(R.string.common_confirm), style = PMTextStyle.Body)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                PMText(text = stringResource(R.string.common_cancel), style = PMTextStyle.Body)
            }
        }
    )
}

@Preview(name = "ReasonDialog Reject", showBackground = true)
@Composable
private fun ReasonDialogRejectPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ReasonDialog(
            action = ModerationAction.REJECT,
            reason = "",
            onReasonChange = {},
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(name = "ReasonDialog Remove", showBackground = true)
@Composable
private fun ReasonDialogRemovePreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ReasonDialog(
            action = ModerationAction.REMOVE,
            reason = "Uygunsuz içerik",
            onReasonChange = {},
            onConfirm = {},
            onDismiss = {}
        )
    }
}
