package com.mefy.platemate.presentation.features.main.messages.conversation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.text.resolve
import com.mefy.platemate.presentation.components.PMBottomSheet
import com.mefy.platemate.presentation.components.PMBottomSheetActions
import com.mefy.platemate.presentation.components.PMCommentField
import com.mefy.platemate.presentation.components.PMRadioButton
import com.mefy.platemate.presentation.components.PMRowItem
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.pmRowPositionOf
import com.mefy.platemate.presentation.features.main.messages.conversation.ReplyPreviewUiModel
import com.mefy.platemate.presentation.features.uimodel.ReportReason
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun ReportMessageBottomSheet(
    modifier: Modifier = Modifier,
    reportTarget: ReplyPreviewUiModel,
    selectedReason: ReportReason?,
    onReasonSelected: (ReportReason) -> Unit,
    otherReasonText: String = "",
    onOtherReasonTextChanged: (String) -> Unit = {},
    commentMaxLength: Int = 250,
    isSubmitting: Boolean = false,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val shape = PMTheme.shapes
    val reasons = ReportReason.entries.toList()

    PMBottomSheet(
        onDismiss = onDismiss,
        title = stringResource(R.string.conversation_report_message_title),
        skipPartiallyExpanded = true,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = spacing.s32)
        ) {
            item(key = "reported_message_preview") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = spacing.s16, vertical = spacing.s8)
                        .clip(shape.medium)
                        .background(colors.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(spacing.s4)
                            .background(colors.primary)
                    )
                    Column(modifier = Modifier.padding(horizontal = spacing.s8, vertical = spacing.s8)) {
                        PMText(
                            text = reportTarget.senderLabel.resolve(),
                            style = PMTextStyle.Caption,
                            color = colors.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        PMText(
                            text = reportTarget.contentPreview,
                            style = PMTextStyle.Caption,
                            color = colors.textSecondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            item(key = "divider_bottom") {
                HorizontalDivider(color = colors.surfaceVariant)
            }

            item(key = "spacer_top") {
                Spacer(modifier = Modifier.padding(top = spacing.s16))
            }

            itemsIndexed(reasons, key = { _, reason -> "reason_${reason.name}" }) { index, reason ->
                val isSelected = reason == selectedReason
                PMRowItem(
                    title = stringResource(reason.labelRes),
                    subtitle = stringResource(reason.descriptionRes),
                    position = pmRowPositionOf(index, reasons.size),
                    showChevron = false,
                    trailing = {
                        PMRadioButton(
                            selected = isSelected,
                            onClick = null
                        )
                    },
                    onClick = { onReasonSelected(reason) },
                    modifier = Modifier.padding(horizontal = spacing.s16)
                )
            }

            if (selectedReason == ReportReason.OTHER) {
                item(key = "other_reason_field") {
                    PMCommentField(
                        value = otherReasonText,
                        onValueChange = onOtherReasonTextChanged,
                        maxLength = commentMaxLength,
                        placeholder = stringResource(R.string.report_reason_other_desc),
                        modifier = Modifier
                            .padding(horizontal = spacing.s16)
                            .padding(top = spacing.s16)
                    )
                }
            }

            item(key = "footer") {
                PMBottomSheetActions(
                    cancelText = stringResource(R.string.common_cancel),
                    submitText = stringResource(R.string.report_user_submit),
                    onCancel = onDismiss,
                    onSubmit = onSubmit,
                    submitEnabled = selectedReason != null && !isSubmitting,
                    modifier = Modifier
                        .padding(horizontal = spacing.s16)
                        .padding(top = spacing.s24)
                )
            }
        }
    }
}

@Preview(name = "ReportMessageBottomSheet Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ReportMessageBottomSheetLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ReportMessageBottomSheet(
            reportTarget = ReplyPreviewUiModel(
                messageId = 1L,
                senderLabel = UiText.Dynamic("Ahmet Yılmaz"),
                contentPreview = "Bu mesajı raporlamak istiyorum çünkü..."
            ),
            selectedReason = ReportReason.OTHER,
            onReasonSelected = {},
            otherReasonText = "Uygunsuz içerik paylaşıyor.",
            onOtherReasonTextChanged = {},
            onDismiss = {},
            onSubmit = {}
        )
    }
}

@Preview(name = "ReportMessageBottomSheet Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ReportMessageBottomSheetDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ReportMessageBottomSheet(
            reportTarget = ReplyPreviewUiModel(
                messageId = 1L,
                senderLabel = UiText.Dynamic("Ahmet Yılmaz"),
                contentPreview = "Merhaba, nasılsın?"
            ),
            selectedReason = null,
            onReasonSelected = {},
            onDismiss = {},
            onSubmit = {}
        )
    }
}
