package com.mefy.platemate.presentation.features.main.platedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTextField
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.main.platedetail.ReviewReportUiState
import com.mefy.platemate.presentation.features.uimodel.CommentReportReason
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReviewReportBottomSheet(
    report: ReviewReportUiState,
    onReasonSelected: (CommentReportReason) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val sheetState = rememberModalBottomSheetState()
    val canSubmit = report.selectedReason != null && !report.isSubmitting

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.surface,
        shape = RoundedCornerShape(topStart = dims.radius.r16, topEnd = dims.radius.r16),
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = dims.spacing.s32)
        ) {
            item(key = "title") {
                PMText(
                    text = stringResource(R.string.report_review_title),
                    style = PMTextStyle.Title,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dims.spacing.s24, vertical = dims.spacing.s16)
                )
            }

            item(key = "divider_top") {
                HorizontalDivider(color = colors.surfaceVariant)
            }

            item(key = "user_card") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.surfaceSecondary)
                        .padding(horizontal = dims.spacing.s24, vertical = dims.spacing.s12),
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(dims.sizing.plateBadgeSmall)
                            .clip(CircleShape)
                            .background(colors.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        PMText(
                            text = report.initials,
                            style = PMTextStyle.Body,
                            fontWeight = FontWeight.ExtraBold,
                            color = colors.onPrimaryContainer
                        )
                    }
                    PMText(
                        text = report.reviewerName,
                        style = PMTextStyle.Body,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.textPrimary
                    )
                }
            }

            item(key = "divider_bottom") {
                HorizontalDivider(color = colors.surfaceVariant)
            }

            items(
                items = CommentReportReason.entries.toList(),
                key = { it.name }
            ) { reason ->
                CommentReasonRow(
                    reason = reason,
                    isSelected = reason == report.selectedReason,
                    onSelected = { onReasonSelected(reason) },
                    modifier = Modifier
                        .padding(horizontal = dims.spacing.s24)
                        .padding(top = dims.spacing.s10)
                )
            }

            item(key = "description") {
                PMTextField(
                    value = report.description,
                    onValueChange = onDescriptionChange,
                    placeholder = stringResource(R.string.report_review_description_hint),
                    singleLine = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dims.spacing.s24)
                        .padding(top = dims.spacing.s16)
                )
            }

            item(key = "footer") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dims.spacing.s24)
                        .padding(top = dims.spacing.s16),
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s10)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(dims.sizing.ctaHeight)
                            .clip(MaterialTheme.shapes.small)
                            .background(colors.surfaceVariant)
                            .debouncedClickable(onClick = onDismiss),
                        contentAlignment = Alignment.Center
                    ) {
                        PMText(
                            text = stringResource(R.string.common_cancel),
                            style = PMTextStyle.Body,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.textTertiary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(dims.sizing.ctaHeight)
                            .clip(MaterialTheme.shapes.small)
                            .background(if (canSubmit) colors.error else colors.disabled)
                            .then(
                                if (canSubmit) Modifier.debouncedClickable(onClick = onSubmit) else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        PMText(
                            text = stringResource(R.string.report_user_submit),
                            style = PMTextStyle.Body,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentReasonRow(
    reason: CommentReportReason,
    isSelected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val rowShape = MaterialTheme.shapes.small
    val primary = colors.primary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(rowShape)
            .background(colors.surfaceSecondary)
            .border(
                width = dims.stroke.st2,
                color = if (isSelected) primary else colors.cardBorder,
                shape = rowShape
            )
            .debouncedClickable(onClick = onSelected)
            .padding(dims.spacing.s12),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(dims.spacing.s24)
                .clip(CircleShape)
                .border(dims.stroke.st2, if (isSelected) primary else colors.disabled, CircleShape)
                .background(if (isSelected) primary else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(dims.spacing.s8)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
        }

        PMText(
            text = stringResource(reason.labelRes),
            style = PMTextStyle.Body,
            fontWeight = FontWeight.SemiBold,
            color = colors.textPrimary,
            modifier = Modifier.weight(1f)
        )
    }
}
