package com.mefy.platemate.presentation.features.main.profile.userprofile.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMBottomSheet
import com.mefy.platemate.presentation.components.PMBottomSheetActions
import com.mefy.platemate.presentation.components.PMCommentField
import com.mefy.platemate.presentation.components.PMRadioButton
import com.mefy.platemate.presentation.components.PMRowItem
import com.mefy.platemate.presentation.components.PMUserCard
import com.mefy.platemate.presentation.components.pmRowPositionOf
import com.mefy.platemate.presentation.features.uimodel.ReportReason
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun ReportUserBottomSheet(
    modifier: Modifier = Modifier,
    participantName: String,
    username: String,
    selectedReason: ReportReason?,
    onReasonSelected: (ReportReason) -> Unit,
    otherReasonText: String = "",
    onOtherReasonTextChanged: (String) -> Unit = {},
    commentMaxLength: Int = 250,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val reasons = ReportReason.entries.toList()

    PMBottomSheet(
        onDismiss = onDismiss,
        title = stringResource(R.string.report_user_title),
        skipPartiallyExpanded = true,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = dims.spacing.s32)
        ) {
            item(key = "user_card") {
                PMUserCard(
                    displayName = participantName,
                    username = username,
                    avatarSize = dims.sizing.plateBadgeSm,
                    containerColor = colors.surfaceSecondary,
                    modifier = Modifier.padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s8)
                )
            }

            item(key = "divider_bottom") {
                HorizontalDivider(color = colors.surfaceVariant)
            }

            item(key = "spacer_top") {
                Spacer(modifier = Modifier.padding(top = dims.spacing.s16))
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
                            onClick = null // Row handles click
                        )
                    },
                    onClick = { onReasonSelected(reason) },
                    modifier = Modifier.padding(horizontal = dims.spacing.s16)
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
                            .padding(horizontal = dims.spacing.s16)
                            .padding(top = dims.spacing.s16)
                    )
                }
            }

            item(key = "footer") {
                PMBottomSheetActions(
                    cancelText = stringResource(R.string.common_cancel),
                    submitText = stringResource(R.string.report_user_submit),
                    onCancel = onDismiss,
                    onSubmit = onSubmit,
                    modifier = Modifier
                        .padding(horizontal = dims.spacing.s16)
                        .padding(top = dims.spacing.s24)
                )
            }
        }
    }
}

@Preview(name = "ReportUserBottomSheet Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ReportUserBottomSheetLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ReportUserBottomSheet(
            participantName = "Ahmet Yılmaz",
            username = "@ahmetyilmaz",
            selectedReason = ReportReason.OTHER,
            onReasonSelected = {},
            otherReasonText = "Bu kişi beni rahatsız ediyor.",
            onOtherReasonTextChanged = {},
            onDismiss = {},
            onSubmit = {}
        )
    }
}

@Preview(name = "ReportUserBottomSheet Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ReportUserBottomSheetDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ReportUserBottomSheet(
            participantName = "Ahmet Yılmaz",
            username = "@ahmetyilmaz",
            selectedReason = null,
            onReasonSelected = {},
            otherReasonText = "",
            onOtherReasonTextChanged = {},
            onDismiss = {},
            onSubmit = {}
        )
    }
}
