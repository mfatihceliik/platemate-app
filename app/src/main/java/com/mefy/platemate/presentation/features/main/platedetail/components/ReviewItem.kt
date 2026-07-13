package com.mefy.platemate.presentation.features.main.platedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMAvatar
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.components.PMRatingStars
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.main.platedetail.PlateReviewUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ReviewItem(
    review: PlateReviewUiModel,
    onAvatarClick: () -> Unit = {},
    onReportClick: () -> Unit = {},
    onEditClick: () -> Unit = {}
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    PMCard(modifier = Modifier.fillMaxWidth(), padding = PaddingValues(dims.spacing.s12)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
            verticalAlignment = Alignment.CenterVertically
        ) {

            PMAvatar(
                onClick = onAvatarClick,
                displayName = review.displayName ?: review.username,
                isEditable = false,
                size = dims.sizing.avatarMd,
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                PMText(
                    text = review.displayName ?: review.username,
                    style = PMTextStyle.Body,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textPrimary
                )
                PMText(
                    text = review.timeAgo,
                    style = PMTextStyle.Note,
                    color = colors.textLabel
                )
            }

            when {
                review.canEdit -> PMIconButton(
                    onClick = onEditClick,
                    size = dims.sizing.iconSm,
                    imageVector = Icons.Outlined.Edit,
                    iconColor = colors.iconDefault,
                    contentDescription = stringResource(R.string.review_edit_title)
                )
                review.canReport -> PMIconButton(
                    onClick = onReportClick,
                    size = dims.sizing.iconSm,
                    imageVector = Icons.Outlined.Flag,
                    iconColor = colors.iconDanger,
                    contentDescription = stringResource(R.string.report_review_title)
                )
            }
        }

        PMRatingStars(
            rating = review.rating,
            starSize = dims.sizing.iconSm,
            modifier = Modifier.padding(top = dims.spacing.s8)
        )

        if (!review.comment.isNullOrBlank()) {
            PMText(
                text = review.comment,
                style = PMTextStyle.Body,
                color = colors.textSecondary,
                modifier = Modifier.padding(top = dims.spacing.s8)
            )
        }

        if (review.reportTags.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.padding(top = dims.spacing.s10),
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s4),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                review.reportTags.forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(dims.radius.r8))
                            .background(colors.surfaceVariant)
                            .padding(horizontal = dims.spacing.s10, vertical = dims.spacing.s4),
                        contentAlignment = Alignment.Center
                    ) {
                        PMText(
                            text = tag,
                            style = PMTextStyle.Note,
                            fontWeight = FontWeight.Medium,
                            color = colors.textSecondary
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "ReviewItem Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ReviewItemLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ReviewItemPreviewContent()
    }
}

@Preview(name = "ReviewItem Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ReviewItemDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ReviewItemPreviewContent()
    }
}

@Composable
private fun ReviewItemPreviewContent() {
    val dims = MaterialTheme.pmDimensions
    Column(
        modifier = Modifier.padding(dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        ReviewItem(
            review = PlateReviewUiModel(
                id = 1,
                username = "ahmetk",
                displayName = "Ahmet K.",
                profilePhotoUrl = null,
                rating = 5,
                timeAgo = "2 gün önce",
                comment = "Çok nazik bir sürücü, teşekkür etti.",
                reportTags = listOf("Olumlu Sürücü", "Yol Verdi"),
                canReport = true
            )
        )
        ReviewItem(
            review = PlateReviewUiModel(
                id = 2,
                username = "zeynept",
                displayName = null,
                profilePhotoUrl = null,
                rating = 4,
                timeAgo = "3 gün önce",
                comment = "Kendi yorumum.",
                reportTags = emptyList(),
                canEdit = true
            )
        )
    }
}
