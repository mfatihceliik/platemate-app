package com.mefy.platemate.presentation.features.main.platedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMBottomSheet
import com.mefy.platemate.presentation.components.PMBottomSheetActions
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.components.PMRowItem
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTextField
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.pmRowPositionOf
import com.mefy.platemate.presentation.features.main.platedetail.ReportReasonUiModel
import com.mefy.platemate.presentation.features.main.platedetail.ReviewReportUiState
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun ReviewReportBottomSheet(
    report: ReviewReportUiState,
    onReasonSelected: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    PMBottomSheet(
        onDismiss = onDismiss,
        title = stringResource(R.string.report_review_title),
        modifier = modifier
    ) {

        val dims = MaterialTheme.pmDimensions
        val colors = MaterialTheme.pmColors
        val canSubmit = report.selectedReasonCode != null && !report.isSubmitting

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = dims.spacing.s32)
        ) {
            item(key = "user_card") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.surfaceSecondary)
                        .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12),
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(dims.sizing.plateBadgeSm)
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

            if (report.isLoadingReasons) {
                item(key = "reasons_loading") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = dims.spacing.s24),
                        contentAlignment = Alignment.Center
                    ) {
                        PMCircularProgressIndicator()
                    }
                }
            } else {
                itemsIndexed(
                    items = report.reasons,
                    key = { _, reason -> reason.code }
                ) { index, reason ->
                    PMRowItem(
                        title = reason.label,
                        position = pmRowPositionOf(index, report.reasons.size),
                        onClick = { onReasonSelected(reason.code) },
                        showChevron = false,
                        trailing = { RadioIndicator(isSelected = reason.code == report.selectedReasonCode) },
                        modifier = Modifier
                            .padding(horizontal = dims.spacing.s16)
                            .padding(top = if (index == 0) dims.spacing.s16 else dims.spacing.s0)
                    )
                }
            }

            item(key = "description") {
                PMTextField(
                    value = report.description,
                    onValueChange = onDescriptionChange,
                    placeholder = stringResource(R.string.report_review_description_hint),
                    singleLine = false,
                    enabled = report.descriptionEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dims.spacing.s16)
                        .padding(top = dims.spacing.s16)
                )
            }

            item(key = "footer") {
                PMBottomSheetActions(
                    cancelText = stringResource(R.string.common_cancel),
                    submitText = stringResource(R.string.report_user_submit),
                    onCancel = onDismiss,
                    onSubmit = onSubmit,
                    submitEnabled = canSubmit,
                    modifier = Modifier
                        .padding(horizontal = dims.spacing.s16)
                        .padding(top = dims.spacing.s16)
                )
            }
        }
    }
}

@Preview(name = "ReviewReportBottomSheet Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ReviewReportBottomSheetLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ReviewReportBottomSheet(
            report = previewReport().copy(selectedReasonCode = "OTHER"),
            onReasonSelected = {},
            onDescriptionChange = {},
            onDismiss = {},
            onSubmit = {}
        )
    }
}

@Preview(name = "ReviewReportBottomSheet Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ReviewReportBottomSheetDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ReviewReportBottomSheet(
            report = previewReport(),
            onReasonSelected = {},
            onDescriptionChange = {},
            onDismiss = {},
            onSubmit = {}
        )
    }
}

private fun previewReport() = ReviewReportUiState(
    reviewId = 1L,
    reviewerName = "Ahmet Yılmaz",
    initials = "AY",
    description = "",
    reasons = listOf(
        ReportReasonUiModel(code = "HATE_SPEECH", label = "Nefret söylemi", requiresDescription = false),
        ReportReasonUiModel(code = "INSULT", label = "Hakaret", requiresDescription = false),
        ReportReasonUiModel(code = "SPAM", label = "Spam", requiresDescription = false),
        ReportReasonUiModel(code = "OTHER", label = "Diğer", requiresDescription = true)
    )
)
