package com.mefy.platemate.presentation.common.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.mefy.platemate.presentation.components.PMPopup
import com.mefy.platemate.presentation.common.text.resolve
import com.mefy.platemate.presentation.theme.pmColors

@Composable
fun DialogHost(
    state: DialogHostState
) {
    val dialog = state.activeDialog ?: return
    val colors = MaterialTheme.pmColors

    val icon: ImageVector
    val iconTint: Color
    val iconContainer: Color
    when (dialog.variant) {
        DialogVariant.Error -> {
            icon = Icons.Filled.Cancel
            iconTint = colors.error
            iconContainer = colors.errorContainer
        }
        DialogVariant.Success -> {
            icon = Icons.Filled.CheckCircle
            iconTint = colors.success
            iconContainer = colors.categoryGreenBg
        }
        DialogVariant.Info -> {
            icon = Icons.Filled.Info
            iconTint = colors.primary
            iconContainer = colors.categoryOrangeBg //primaryContainer
        }
    }

    val primaryButtonColors = if (dialog.variant == DialogVariant.Error) {
        ButtonDefaults.buttonColors(
            containerColor = colors.error,
            contentColor = colors.onError
        )
    } else {
        null
    }

    val dismissText = dialog.dismissText?.resolve()

    PMPopup(
        title = dialog.title.resolve(),
        message = dialog.message.resolve(),
        icon = icon,
        iconTint = iconTint,
        iconContainerColor = iconContainer,
        primaryText = dialog.confirmText.resolve(),
        onPrimaryClick = {
            dialog.onConfirm?.invoke()
            state.dismissDialog()
        },
        onDismissRequest = {
            if (dialog.dismissible) {
                dialog.onDismiss?.invoke()
                state.dismissDialog()
            }
        },
        secondaryText = dismissText,
        onSecondaryClick = dismissText?.let {
            {
                dialog.onDismiss?.invoke()
                state.dismissDialog()
            }
        },
        primaryButtonColors = primaryButtonColors
    )
}
