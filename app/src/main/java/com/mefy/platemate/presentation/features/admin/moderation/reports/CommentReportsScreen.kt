package com.mefy.platemate.presentation.features.admin.moderation.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.variant.PMButtonVariant
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun CommentReportsScreen(
    state: CommentReportsUiState,
    onAction: (CommentReportsUiAction) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    if (state.isEmpty) {
        Box(modifier.fillMaxSize().padding(contentPadding), contentAlignment = Alignment.Center) {
            PMText(text = stringResource(R.string.admin_reports_empty), style = PMTextStyle.Body, color = colors.textLabel)
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(contentPadding),
            contentPadding = PaddingValues(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            itemsIndexed(items = state.items, key = { _, item -> item.id }) { index, item ->
                if (index >= state.items.lastIndex) {
                    LaunchedEffect(state.items.size) { onAction(CommentReportsUiAction.LoadMore) }
                }
                ReportCard(
                    model = item,
                    isActioning = state.actioningId == item.id,
                    onAccept = { onAction(CommentReportsUiAction.AcceptClicked(item.id)) },
                    onReject = { onAction(CommentReportsUiAction.RejectClicked(item.id)) }
                )
            }
            if (state.isLoadingMore) {
                item {
                    Box(Modifier.fillMaxWidth().padding(dims.spacing.s12), contentAlignment = Alignment.Center) {
                        PMCircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportCard(
    model: CommentReportUiModel,
    isActioning: Boolean,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surfaceVariant, RoundedCornerShape(12.dp))
            .padding(dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            PMText(text = model.plateCode, style = PMTextStyle.Body, fontWeight = FontWeight.Bold, color = colors.textPrimary)
            PMText(text = model.reasonCode, style = PMTextStyle.Note, color = colors.primary)
        }
        if (model.description.isNotBlank()) {
            PMText(text = model.description, style = PMTextStyle.Body, color = colors.textPrimary)
        }
        PMText(text = model.date, style = PMTextStyle.Note, color = colors.textLabel)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
            PMButton(
                text = stringResource(R.string.admin_report_reject),
                onClick = onReject,
                variant = PMButtonVariant.Outlined,
                enabled = !isActioning,
                modifier = Modifier.weight(1f)
            )
            PMButton(
                text = stringResource(R.string.admin_report_accept),
                onClick = onAccept,
                enabled = !isActioning,
                loading = isActioning,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private val commentReportPreviewModel = CommentReportUiModel(
    id = 1L,
    plateCode = "34 EK 0682",
    reasonCode = "SPAM",
    description = "Yorum spam içeriyor, alakasız reklam.",
    date = "2026-06-22"
)

private val commentReportsPreviewState = CommentReportsUiState(
    isLoading = false,
    items = listOf(
        commentReportPreviewModel,
        CommentReportUiModel(
            id = 2L,
            plateCode = "06 ABC 123",
            reasonCode = "ABUSE",
            description = "",
            date = "2026-06-23"
        )
    )
)

@Preview(name = "CommentReports Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun CommentReportsScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        CommentReportsScreen(state = commentReportsPreviewState, onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}

@Preview(name = "CommentReports Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun CommentReportsScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        CommentReportsScreen(state = commentReportsPreviewState, onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}

@Preview(name = "CommentReports Empty", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun CommentReportsScreenEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        CommentReportsScreen(state = CommentReportsUiState(isLoading = false), onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}

@Preview(name = "ReportCard", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ReportCardPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ReportCard(model = commentReportPreviewModel, isActioning = false, onAccept = {}, onReject = {})
    }
}
