package com.mefy.platemate.presentation.features.main.profile.userprofile.components

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.main.profile.userprofile.model.ReportReason
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReportUserBottomSheet(
    participantName: String,
    username: String,
    initials: String,
    avatarBg: Color,
    avatarFg: Color,
    selectedReason: ReportReason?,
    onReasonSelected: (ReportReason) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = dims.spacing.s32)
        ) {
            item(key = "title") {
                PMText(
                    text = stringResource(R.string.report_user_title),
                    style = PMTextStyle.Title,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dims.spacing.s24, vertical = dims.spacing.s16)
                )
            }

            item(key = "divider_top") {
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
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
                            .background(avatarBg)
                            .border(dims.stroke.st1, colors.primaryContainerBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        PMText(
                            text = initials,
                            style = PMTextStyle.Body,
                            fontWeight = FontWeight.ExtraBold,
                            color = avatarFg
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)) {
                        PMText(
                            text = participantName,
                            style = PMTextStyle.Body,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.textPrimary
                        )
                        PMText(text = username, style = PMTextStyle.Note, color = colors.textLabel)
                    }
                }
            }

            item(key = "divider_bottom") {
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            }

            items(
                items = ReportReason.entries.toList(),
                key = { it.name }
            ) { reason ->
                ReportReasonRow(
                    reason = reason,
                    isSelected = reason == selectedReason,
                    onSelected = { onReasonSelected(reason) },
                    modifier = Modifier
                        .padding(horizontal = dims.spacing.s24)
                        .padding(top = dims.spacing.s10)
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
                            .background(MaterialTheme.colorScheme.surfaceVariant)
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
                            .shadow(
                                elevation = 4.dp,
                                shape = MaterialTheme.shapes.small,
                                ambientColor = MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                            )
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.error)
                            .debouncedClickable(onClick = onSubmit),
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

@Preview(name = "ReportUserBottomSheet Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ReportUserBottomSheetLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ReportUserBottomSheet(
            participantName = "Ahmet Yılmaz",
            username = "@ahmetyilmaz",
            initials = "AY",
            avatarBg = Color(0xFFECFEFF),
            avatarFg = Color(0xFF0E7490),
            selectedReason = ReportReason.SPAM,
            onReasonSelected = {},
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
            initials = "AY",
            avatarBg = Color(0xFF164E63),
            avatarFg = Color(0xFF67E8F9),
            selectedReason = null,
            onReasonSelected = {},
            onDismiss = {},
            onSubmit = {}
        )
    }
}
