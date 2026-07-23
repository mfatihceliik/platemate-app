package com.mefy.platemate.presentation.features.admin.moderation.comments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.components.PMPopup
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTextField
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.moderation.comments.components.PendingCommentCard
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun CommentModerationScreen(
    modifier: Modifier = Modifier,
    state: CommentModerationUiState,
    onAction: (CommentModerationUiAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors

    val onReasonChanged =
        remember(onAction) { { v: String -> onAction(CommentModerationUiAction.ReasonChanged(v)) } }
    val onReasonConfirmed =
        remember(onAction) { { onAction(CommentModerationUiAction.ReasonConfirmed) } }
    val onReasonDismissed =
        remember(onAction) { { onAction(CommentModerationUiAction.ReasonDismissed) } }


    state.reasonDialog?.let { dialog ->
        val titleRes = when (dialog.action) {
            ModerationAction.REJECT -> R.string.admin_reason_dialog_reject_title
            ModerationAction.REMOVE -> R.string.admin_reason_dialog_remove_title
        }
        PMPopup(
            title = stringResource(titleRes),
            icon = Icons.Filled.Gavel,
            iconTint = colors.primary,
            iconContainerColor = colors.primaryContainer,
            primaryText = stringResource(R.string.common_confirm),
            onPrimaryClick = onReasonConfirmed,
            secondaryText = stringResource(R.string.common_cancel),
            onSecondaryClick = onReasonDismissed,
            onDismissRequest = onReasonDismissed
        ) {
            PMTextField(
                value = dialog.reason,
                onValueChange = onReasonChanged,
                placeholder = stringResource(R.string.admin_reason_hint),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (state.isEmpty) {
        Box(
            modifier = modifier.fillMaxSize().padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            PMText(
                text = stringResource(R.string.admin_moderation_empty),
                style = PMTextStyle.Body,
                color = colors.textLabel
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(spacing.s12)
        ) {
            itemsIndexed(items = state.items, key = { _, item -> item.id }) { index, item ->
                if (index >= state.items.lastIndex) {
                    LaunchedEffect(state.items.size) { onAction(CommentModerationUiAction.LoadMore) }
                }
                PendingCommentCard(
                    model = item,
                    isActioning = state.actioningId == item.id,
                    onApprove = { onAction(CommentModerationUiAction.ApproveClicked(item.id)) },
                    onReject = { onAction(CommentModerationUiAction.RejectClicked(item.id)) },
                    onRemove = { onAction(CommentModerationUiAction.RemoveClicked(item.id)) }
                )
            }

            if (state.isLoadingMore) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(spacing.s12), contentAlignment = Alignment.Center) {
                        PMCircularProgressIndicator()
                    }
                }
            }
        }
    }
}

private val commentModerationPreviewState = CommentModerationUiState(
    isLoading = false,
    items = listOf(
        PendingCommentUiModel(
            id = 1L,
            plateCode = "34 EK 0682",
            username = "ahmety",
            rating = 2,
            comment = "Çok agresif araç kullanıyor, dikkatsiz.",
            reportCount = 3,
            tags = listOf("Agresif", "Dikkatsiz"),
            date = "2026-06-20"
        ),
        PendingCommentUiModel(
            id = 2L,
            plateCode = "06 ABC 123",
            username = "meryem",
            rating = 5,
            comment = "Çok nazik bir sürücü, teşekkürler!",
            reportCount = 1,
            tags = emptyList(),
            date = "2026-06-21"
        )
    )
)

@Preview(name = "CommentModeration Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun CommentModerationScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        CommentModerationScreen(
            state = commentModerationPreviewState,
            onAction = {}
        )
    }
}

@Preview(name = "CommentModeration Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun CommentModerationScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        CommentModerationScreen(
            state = commentModerationPreviewState,
            onAction = {}
        )
    }
}

@Preview(name = "CommentModeration Empty", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun CommentModerationScreenEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        CommentModerationScreen(
            state = CommentModerationUiState(isLoading = false),
            onAction = {}
        )
    }
}
