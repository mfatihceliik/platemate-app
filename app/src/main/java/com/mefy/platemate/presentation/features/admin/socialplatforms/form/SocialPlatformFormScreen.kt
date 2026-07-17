package com.mefy.platemate.presentation.features.admin.socialplatforms.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.features.admin.components.AddLanguageRow
import com.mefy.platemate.presentation.features.admin.reporttypes.components.FormField
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun SocialPlatformFormScreen(
    modifier: Modifier = Modifier,
    state: SocialPlatformFormUiState,
    onAction: (SocialPlatformFormUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues(),
) {
    val dims = MaterialTheme.pmDimensions

    val onSaveClicked = remember(onAction) { { onAction(SocialPlatformFormUiAction.SaveClicked) } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            item {
                PMSectionLabel(text = stringResource(R.string.admin_social_platform_field_code))
                FormField(state.code, !state.isEdit) { onAction(SocialPlatformFormUiAction.CodeChanged(it)) }
            }

            state.labels.forEach { (locale, value) ->
                item {
                    PMSectionLabel(text = "Label (${locale.uppercase()})")
                    FormField(value) { onAction(SocialPlatformFormUiAction.LabelChanged(locale, it)) }
                }
            }
            item {
                AddLanguageRow(onAdd = { onAction(SocialPlatformFormUiAction.AddLabelLanguage(it)) })
            }

            item {
                PMSectionLabel(text = stringResource(R.string.admin_social_platform_field_icon_url))
                FormField(state.iconUrl) { onAction(SocialPlatformFormUiAction.IconUrlChanged(it)) }
            }

            item {
                PMSectionLabel(text = stringResource(R.string.admin_social_platform_field_bg_color))
                FormField(state.backgroundColorHex) { onAction(SocialPlatformFormUiAction.BackgroundColorHexChanged(it)) }
            }

            item {
                PMSectionLabel(text = stringResource(R.string.admin_social_platform_field_tint_color))
                FormField(state.iconTintColorHex) { onAction(SocialPlatformFormUiAction.IconTintColorHexChanged(it)) }
            }

            item {
                PMSectionLabel(text = stringResource(R.string.admin_social_platform_field_sort))
                FormField(state.sortOrder, keyboardType = KeyboardType.Number) {
                    onAction(SocialPlatformFormUiAction.SortOrderChanged(it))
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dims.spacing.s8),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            if (!state.isLoading) {
                PMButton(
                    text = stringResource(R.string.common_save),
                    onClick = onSaveClicked,
                    enabled = state.isSaveEnabled,
                    loading = state.isSaving,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private val socialPlatformFormPreviewState = SocialPlatformFormUiState(
    isLoading = false,
    isEdit = true,
    code = "INSTAGRAM",
    labels = mapOf("tr" to "Instagram", "en" to "Instagram"),
    iconUrl = "https://cdn.example.com/icons/instagram.png",
    backgroundColorHex = "#FDF2F8",
    iconTintColorHex = "#DB2777",
    sortOrder = "1"
)

@Preview(name = "SocialPlatformForm Edit Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun SocialPlatformFormScreenEditLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        SocialPlatformFormScreen(
            state = socialPlatformFormPreviewState,
            onAction = {},
            innerPadding = PaddingValues(0.dp)
        )
    }
}

@Preview(name = "SocialPlatformForm Edit Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun SocialPlatformFormScreenEditDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        SocialPlatformFormScreen(
            state = socialPlatformFormPreviewState,
            onAction = {},
            innerPadding = PaddingValues(0.dp)
        )
    }
}

@Preview(name = "SocialPlatformForm Add", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun SocialPlatformFormScreenAddPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        SocialPlatformFormScreen(
            state = SocialPlatformFormUiState(isEdit = false),
            onAction = {},
            innerPadding = PaddingValues(0.dp)
        )
    }
}
