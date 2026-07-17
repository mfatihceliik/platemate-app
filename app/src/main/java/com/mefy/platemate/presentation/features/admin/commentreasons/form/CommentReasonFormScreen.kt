package com.mefy.platemate.presentation.features.admin.commentreasons.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.components.PMSwitch
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.reporttypes.components.FormField
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun CommentReasonFormScreen(
    modifier: Modifier = Modifier,
    state: CommentReasonFormUiState,
    onAction: (CommentReasonFormUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues(),
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    val onSaveClicked = remember(onAction) { { onAction(CommentReasonFormUiAction.SaveClicked) } }

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
                PMSectionLabel(text = stringResource(R.string.admin_comment_reason_field_code))
                FormField(state.code, !state.isEdit) { onAction(CommentReasonFormUiAction.CodeChanged(it)) }
            }

            item {
                PMSectionLabel(text = stringResource(R.string.admin_comment_reason_field_label))
                FormField(state.label) { onAction(CommentReasonFormUiAction.LabelChanged(it)) }
            }

            item {
                PMSectionLabel(text = stringResource(R.string.admin_comment_reason_field_sort))
                FormField(state.sortOrder, keyboardType = KeyboardType.Number) {
                    onAction(CommentReasonFormUiAction.SortOrderChanged(it))
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dims.spacing.s8),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12)
                ) {
                    PMText(
                        text = stringResource(R.string.admin_comment_reason_field_requires_description),
                        style = PMTextStyle.Body,
                        color = colors.textPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    PMSwitch(
                        checked = state.requiresDescription,
                        onCheckedChange = { onAction(CommentReasonFormUiAction.RequiresDescriptionChanged(it)) }
                    )
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

private val commentReasonFormPreviewState = CommentReasonFormUiState(
    isLoading = false,
    isEdit = true,
    code = "OTHER",
    label = "Diğer",
    requiresDescription = true,
    sortOrder = "7"
)

@Preview(name = "CommentReasonForm Edit Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun CommentReasonFormScreenEditLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        CommentReasonFormScreen(
            state = commentReasonFormPreviewState,
            onAction = {},
            innerPadding = PaddingValues(0.dp)
        )
    }
}

@Preview(name = "CommentReasonForm Edit Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun CommentReasonFormScreenEditDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        CommentReasonFormScreen(
            state = commentReasonFormPreviewState,
            onAction = {},
            innerPadding = PaddingValues(0.dp)
        )
    }
}

@Preview(name = "CommentReasonForm Add", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun CommentReasonFormScreenAddPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        CommentReasonFormScreen(
            state = CommentReasonFormUiState(isEdit = false),
            onAction = {},
            innerPadding = PaddingValues(0.dp)
        )
    }
}
