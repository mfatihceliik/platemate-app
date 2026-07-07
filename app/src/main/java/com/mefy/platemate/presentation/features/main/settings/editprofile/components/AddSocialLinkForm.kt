package com.mefy.platemate.presentation.features.main.settings.editprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.components.PMTextField
import com.mefy.platemate.presentation.components.model.PMIconButtonVariant
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun AddSocialLinkForm(
    url: String,
    errorText: String?,
    isAddEnabled: Boolean,
    onUrlChange: (String) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    // Ayrı buton yerine: URL alanının içinde küçük "+" ekle ikonu (trailingIcon).
    PMTextField(
        value = url,
        onValueChange = onUrlChange,
        placeholder = stringResource(R.string.edit_profile_social_url_placeholder),
        isError = errorText != null,
        errorText = errorText,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { if (isAddEnabled) onAdd() }),
        trailingIcon = {
            PMIconButton(
                onClick = onAdd,
                enabled = isAddEnabled,
                variant = PMIconButtonVariant.Filled,
                size = dims.sizing.iconMd,
                contentDescription = stringResource(R.string.edit_profile_social_add)
            ) {
                PMIcon(
                    imageVector = Icons.Filled.Add,
                    tint = Color.White,
                    size = dims.sizing.iconSm
                )
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

// ── Previews ────────────────────────────────────────────────

@Composable
private fun AddSocialLinkFormPreviewContainer(
    url: String,
    errorText: String?,
    isAddEnabled: Boolean
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(dims.spacing.s16)
    ) {
        AddSocialLinkForm(
            url = url,
            errorText = errorText,
            isAddEnabled = isAddEnabled,
            onUrlChange = {},
            onAdd = {}
        )
    }
}

@Preview(name = "AddSocialLinkForm Empty", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun AddSocialLinkFormEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        AddSocialLinkFormPreviewContainer(
            url = "",
            errorText = null,
            isAddEnabled = false
        )
    }
}

@Preview(name = "AddSocialLinkForm Error", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun AddSocialLinkFormErrorPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        AddSocialLinkFormPreviewContainer(
            url = "https://random-site.com/foo",
            errorText = "Desteklenmeyen platform",
            isAddEnabled = true
        )
    }
}