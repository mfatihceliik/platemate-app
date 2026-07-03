package com.mefy.platemate.presentation.features.admin.moderation.comments.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMButtonStyle
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.moderation.comments.PendingCommentUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun PendingCommentCard(
    model: PendingCommentUiModel,
    isActioning: Boolean,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surfaceVariant, RoundedCornerShape(12.dp))
            .padding(dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMText(text = model.plateCode, style = PMTextStyle.Body, fontWeight = FontWeight.Bold, color = colors.textPrimary)
            PMText(text = "★ ${model.rating}", style = PMTextStyle.Note, color = colors.textLabel)
        }

        PMText(
            text = stringResource(R.string.admin_comment_by, model.username),
            style = PMTextStyle.Note,
            color = colors.textLabel
        )

        PMText(text = model.comment, style = PMTextStyle.Body, color = colors.textPrimary)

        if (model.tags.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
            ) {
                model.tags.forEach { tag -> TagChip(text = tag) }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMText(
                text = stringResource(R.string.admin_comment_report_count, model.reportCount),
                style = PMTextStyle.Note,
                color = colors.textLabel
            )
            PMText(text = model.date, style = PMTextStyle.Note, color = colors.textLabel)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            PMButton(
                text = stringResource(R.string.admin_comment_reject),
                onClick = onReject,
                style = PMButtonStyle.Outlined,
                enabled = !isActioning,
                modifier = Modifier.weight(1f)
            )
            PMButton(
                text = stringResource(R.string.admin_comment_remove),
                onClick = onRemove,
                style = PMButtonStyle.Text,
                enabled = !isActioning,
                modifier = Modifier.weight(1f)
            )
            PMButton(
                text = stringResource(R.string.admin_comment_approve),
                onClick = onApprove,
                enabled = !isActioning,
                loading = isActioning,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private val pendingCommentCardPreviewModel = PendingCommentUiModel(
    id = 1L,
    plateCode = "34 EK 0682",
    username = "ahmety",
    rating = 2,
    comment = "Çok agresif araç kullanıyor, dikkatsiz.",
    reportCount = 3,
    tags = listOf("Agresif", "Dikkatsiz"),
    date = "2026-06-20"
)

@Preview(name = "PendingCommentCard Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PendingCommentCardLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PendingCommentCard(
            model = pendingCommentCardPreviewModel,
            isActioning = false,
            onApprove = {},
            onReject = {},
            onRemove = {}
        )
    }
}

@Preview(name = "PendingCommentCard Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PendingCommentCardDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PendingCommentCard(
            model = pendingCommentCardPreviewModel,
            isActioning = false,
            onApprove = {},
            onReject = {},
            onRemove = {}
        )
    }
}