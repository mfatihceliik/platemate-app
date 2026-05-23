package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import com.mefy.platemate.presentation.features.uimodel.PlateReportTagUiModel
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PlateCard(
    plateCode: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    rank: Int? = null,
    cityName: String? = null,
    reportTags: List<PlateReportTagUiModel> = emptyList(),
    ratingAverage: Double? = null,
    commentCount: Long? = null,
    rightBottomContent: @Composable (() -> Unit)? = null
) {
    val spacing = MaterialTheme.pmDimensions.spacing
    val radius = MaterialTheme.pmDimensions.radius
    val colorScheme = MaterialTheme.colorScheme
    val stroke = MaterialTheme.pmDimensions.stroke

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(radius.r18))
            .background(colorScheme.surfaceVariant)
            .border(
                width = stroke.st1,
                color = colorScheme.primary.copy(alpha = 0.5f),
                shape = RoundedCornerShape(radius.r18)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = spacing.s16, vertical = spacing.s16)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlateCardLeadingSection(
                rank = rank,
                plateCode = plateCode,
                cityName = cityName,
                reportTags = reportTags,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = spacing.s8)
            )
            PlateCardTrailingSection(
                ratingAverage = ratingAverage,
                commentCount = commentCount,
                rightBottomContent = rightBottomContent
            )
        }
    }
}

@Composable
private fun PlateCardLeadingSection(
    rank: Int?,
    plateCode: String,
    cityName: String?,
    reportTags: List<PlateReportTagUiModel>,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.pmDimensions.spacing
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing.s12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (rank != null) {
            PMText(
                text = stringResource(R.string.discover_rank_format, rank),
                style = PMTextStyle.Headline,
                color = colorScheme.primary
            )
        }

        PlateBadge(plateCode = plateCode)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(spacing.s4)
        ) {
            PlateCityRow(cityName = cityName)
            PlateReportTagsRow(reportTags = reportTags)
        }
    }
}

@Composable
private fun PlateBadge(
    plateCode: String,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.pmDimensions.spacing
    val radius = MaterialTheme.pmDimensions.radius
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(radius.r8))
            .background(colorScheme.primaryContainer)
            .padding(horizontal = spacing.s12, vertical = spacing.s8)
    ) {
        PMText(
            text = plateCode,
            style = PMTextStyle.Title,
            color = colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun PlateCityRow(
    cityName: String?,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.pmDimensions.spacing
    val colorScheme = MaterialTheme.colorScheme
    val resolvedCityName = cityName?.trim()?.takeIf { it.isNotBlank() } ?: return

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing.s4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = null,
            tint = colorScheme.onSurfaceVariant,
            modifier = Modifier.size(spacing.s14)
        )
        PMText(
            text = resolvedCityName,
            style = PMTextStyle.Body,
            color = colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PlateReportTagsRow(
    reportTags: List<PlateReportTagUiModel>,
    modifier: Modifier = Modifier
) {
    if (reportTags.isEmpty()) return

    val spacing = MaterialTheme.pmDimensions.spacing

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(spacing.s6),
        verticalAlignment = Alignment.CenterVertically
    ) {
        reportTags.forEach { reportTag ->
            ReportTypeCard(tag = reportTag)
        }
    }
}

@Composable
private fun PlateCardTrailingSection(
    ratingAverage: Double?,
    commentCount: Long?,
    rightBottomContent: @Composable (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.pmDimensions.spacing
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(spacing.s8)
    ) {
        PlateScoreBlock(ratingAverage = ratingAverage)

        if (rightBottomContent != null) {
            rightBottomContent()
        } else if (commentCount != null) {
            PMText(
                text = stringResource(R.string.discover_comment_count_format, commentCount),
                style = PMTextStyle.Body,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PlateScoreBlock(
    ratingAverage: Double?,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.pmDimensions.spacing
    val colorScheme = MaterialTheme.colorScheme

    if (ratingAverage == null) {
        Spacer(modifier = modifier.size(spacing.s14))
        return
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing.s4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = colorScheme.error,
            modifier = Modifier.size(spacing.s14)
        )
        PMText(
            text = String.format("%.1f", ratingAverage),
            style = PMTextStyle.Title,
            color = colorScheme.error
        )
    }
}
