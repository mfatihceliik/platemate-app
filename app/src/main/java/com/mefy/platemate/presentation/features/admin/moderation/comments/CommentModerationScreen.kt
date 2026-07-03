package com.mefy.platemate.presentation.features.admin.moderation.comments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.moderation.comments.components.PendingCommentCard
import com.mefy.platemate.presentation.features.admin.moderation.comments.components.ReasonDialog
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun CommentModerationScreen(
    state: CommentModerationUiState,
    onAction: (CommentModerationUiAction) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    state.reasonDialog?.let { dialog ->
        ReasonDialog(
            action = dialog.action,
            reason = dialog.reason,
            onReasonChange = { onAction(CommentModerationUiAction.ReasonChanged(it)) },
            onConfirm = { onAction(CommentModerationUiAction.ReasonConfirmed) },
            onDismiss = { onAction(CommentModerationUiAction.ReasonDismissed) }
        )
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
            modifier = modifier.fillMaxSize().padding(contentPadding),
            contentPadding = PaddingValues(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
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
                    Box(modifier = Modifier.fillMaxWidth().padding(dims.spacing.s12), contentAlignment = Alignment.Center) {
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
        CommentModerationScreen(state = commentModerationPreviewState, onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}

@Preview(name = "CommentModeration Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun CommentModerationScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        CommentModerationScreen(state = commentModerationPreviewState, onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}

@Preview(name = "CommentModeration Empty", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun CommentModerationScreenEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        CommentModerationScreen(state = CommentModerationUiState(isLoading = false), onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}
