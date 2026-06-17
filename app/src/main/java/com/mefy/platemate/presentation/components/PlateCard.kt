package com.mefy.platemate.presentation.components

import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.model.PlateCardAction
import com.mefy.platemate.presentation.components.model.PlateCardDensity
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.uimodel.PlateReportTagUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
import com.mefy.platemate.presentation.theme.pmIconColors

@Composable
fun PlateCard(
    plateCode: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    density: PlateCardDensity = PlateCardDensity.Compact,
    rank: Int? = null,
    cityName: String? = null,
    reportTags: List<PlateReportTagUiModel> = emptyList(),
    ratingAverage: Double? = null,
    commentCount: Long? = null,
    action: PlateCardAction = PlateCardAction.None
) {
    val spacing = MaterialTheme.pmDimensions.spacing

    val cardPadding = if (density == PlateCardDensity.Compact) spacing.s8 else spacing.s12
    val horizontalGap = if (density == PlateCardDensity.Compact) spacing.s8 else spacing.s12
    val verticalGap = if (density == PlateCardDensity.Compact) spacing.s4 else spacing.s4

    PMCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        padding = PaddingValues(cardPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(horizontalGap),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Rank and Plate Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.s8)
            ) {
                if (rank != null) {
                    PMText(
                        text = stringResource(R.string.discover_rank_format, rank),
                        style = PMTextStyle.Title,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                CompactPlateBadge(plateCode = plateCode)
            }

            // Center: Main Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(verticalGap)
            ) {
                // Row 1: Tags Line (if any)
                if (reportTags.isNotEmpty()) {
                    val maxTags = if (density == PlateCardDensity.Compact) 2 else 3
                    val visibleTags = reportTags.take(maxTags)
                    val extraCount = reportTags.size - maxTags

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spacing.s4)
                    ) {
                        visibleTags.forEach { CompactPlateTag(it) }
                        if (extraCount > 0) {
                            CompactTagOverflow("+$extraCount")
                        }
                    }
                }

                // Row 2: City
                val hasCity = !cityName.isNullOrBlank()
                if (hasCity) {
                    PMText(
                        text = cityName,
                        style = PMTextStyle.Body,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Row 3: Metrics Line
                val hasRating = ratingAverage != null
                val hasComments = commentCount != null
                if (hasRating || hasComments) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spacing.s8)
                    ) {
                        if (hasRating) {
                            InlineMetric(
                                icon = Icons.Filled.Star,
                                text = String.format("%.1f", ratingAverage),
                                color = MaterialTheme.pmIconColors.favorite
                            )
                        }
                        if (hasComments) {
                            InlineMetric(
                                icon = Icons.Filled.ChatBubble,
                                text = commentCount.toString(),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Right: Actions
            when (action) {
                is PlateCardAction.Closable -> {
                    CompactActionIcon(
                        icon = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.search_recent_remove),
                        onClick = action.onClose,
                        tint = MaterialTheme.pmIconColors.destructive
                    )
                }
                is PlateCardAction.Bookmarkable -> {
                    CompactBookmarkIcon(
                        isBookmarked = action.isBookmarked,
                        onClick = action.onBookmark,
                        testTag = action.testTag
                    )
                }
                is PlateCardAction.ClosableAndBookmarkable -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(spacing.s8),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CompactActionIcon(
                            icon = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.search_recent_remove),
                            onClick = action.onClose,
                            tint = MaterialTheme.pmIconColors.destructive
                        )
                        CompactBookmarkIcon(
                            isBookmarked = action.isBookmarked,
                            onClick = action.onBookmark,
                            testTag = action.bookmarkTestTag
                        )
                    }
                }
                PlateCardAction.None -> {}
            }
        }
    }
}

@Composable
private fun CompactPlateBadge(
    plateCode: String,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.pmDimensions.spacing
    val radius = MaterialTheme.pmDimensions.radius
    val stroke = MaterialTheme.pmDimensions.stroke
    val pmColors = MaterialTheme.pmColors

    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(radius.r8))
            .border(stroke.st1, pmColors.primaryContainerBorder, RoundedCornerShape(radius.r8))
            .background(pmColors.primaryContainer),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(spacing.s8)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primary)
        )
        PMText(
            text = plateCode,
            style = PMTextStyle.Title,
            color = pmColors.primaryDark,
            modifier = Modifier.padding(horizontal = spacing.s8, vertical = spacing.s8)
        )
    }
}

@Composable
private fun InlineMetric(icon: ImageVector, text: String, color: Color) {
    val spacing = MaterialTheme.pmDimensions.spacing
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.s4)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(spacing.s12)
        )
        PMText(
            text = text,
            style = PMTextStyle.Caption,
            color = color
        )
    }
}

@Composable
private fun CompactPlateTag(tag: PlateReportTagUiModel) {
    val spacing = MaterialTheme.pmDimensions.spacing
    val radius = MaterialTheme.pmDimensions.radius
    val color = runCatching { Color(parseColor(tag.colorHex)) }.getOrDefault(MaterialTheme.colorScheme.onSurfaceVariant)

    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(radius.r8))
            .border(MaterialTheme.pmDimensions.stroke.st1, color.copy(alpha = 0.3f), RoundedCornerShape(radius.r8))
            .padding(horizontal = spacing.s8, vertical = spacing.s4)
    ) {
        PMText(
            text = tag.label,
            style = PMTextStyle.Caption,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun CompactTagOverflow(text: String) {
    val spacing = MaterialTheme.pmDimensions.spacing
    val radius = MaterialTheme.pmDimensions.radius
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .background(colorScheme.surfaceVariant, RoundedCornerShape(radius.r8))
            .padding(horizontal = spacing.s8, vertical = spacing.s4)
    ) {
        PMText(
            text = text,
            style = PMTextStyle.Caption,
            color = colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CompactActionIcon(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    tint: Color,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.pmDimensions.spacing
    Box(
        modifier = modifier
            .size(spacing.s24)
            .debouncedClickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(spacing.s16)
        )
    }
}

@Composable
private fun CompactBookmarkIcon(
    isBookmarked: Boolean,
    onClick: () -> Unit,
    testTag: String?,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.pmDimensions.spacing
    val activeTint = MaterialTheme.pmIconColors.favorite
    val inactiveTint = MaterialTheme.pmIconColors.favoriteInactive

    val buttonModifier = if (testTag == null) {
        modifier
    } else {
        modifier.testTag(testTag)
    }

    Box(
        modifier = buttonModifier
            .size(spacing.s24)
            .debouncedClickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
            contentDescription = null,
            tint = if (isBookmarked) activeTint else inactiveTint,
            modifier = Modifier.size(spacing.s16)
        )
    }
}

@Preview(name = "Standard - Full Data", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PlateCardStandardFullDataPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateCardPreviewContainer {
            PlateCard(
                plateCode = "34 ABC 123",
                onClick = {},
                density = PlateCardDensity.Standard,
                rank = 1,
                cityName = "Istanbul",
                reportTags = plateCardPreviewTags(4),
                ratingAverage = 4.7,
                commentCount = 128,
                action = PlateCardAction.Bookmarkable(isBookmarked = true, onBookmark = {})
            )
        }
    }
}

@Preview(name = "Compact - Minimal Data", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PlateCardCompactMinimalPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateCardPreviewContainer {
            PlateCard(
                plateCode = "06 XYZ 06",
                onClick = {},
                density = PlateCardDensity.Compact,
                cityName = "Ankara",
                action = PlateCardAction.Closable(onClose = {})
            )
        }
    }
}

@Preview(name = "Standard - Bookmarked Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PlateCardStandardDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PlateCardPreviewContainer {
            PlateCard(
                plateCode = "35 IZM 35",
                onClick = {},
                density = PlateCardDensity.Standard,
                cityName = "Izmir",
                reportTags = emptyList(),
                ratingAverage = 3.5,
                commentCount = 12,
                action = PlateCardAction.Bookmarkable(isBookmarked = true, onBookmark = {})
            )
        }
    }
}

@Preview(name = "Long Text Edge Case", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PlateCardLongTextPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateCardPreviewContainer {
            PlateCard(
                plateCode = "99 EXTRA LONG PLATE 99",
                onClick = {},
                density = PlateCardDensity.Standard,
                cityName = "A very long city name that should be truncated properly without breaking the layout",
                reportTags = plateCardPreviewTags(2),
                ratingAverage = 4.1,
                commentCount = 9999,
                action = PlateCardAction.ClosableAndBookmarkable(
                    onClose = {},
                    isBookmarked = false,
                    onBookmark = {}
                )
            )
        }
    }
}

@Composable
private fun PlateCardPreviewContainer(
    content: @Composable ColumnScope.() -> Unit
) {
    val spacing = MaterialTheme.pmDimensions.spacing

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(spacing.s16),
        verticalArrangement = Arrangement.spacedBy(spacing.s12),
        content = content
    )
}

private fun plateCardPreviewTags(count: Int): List<PlateReportTagUiModel> {
    val allTags = listOf(
        PlateReportTagUiModel(code = "CUTS", label = "Cuts lanes", severity = "HIGH", colorHex = "#FF6A3D"),
        PlateReportTagUiModel(code = "SPEEDING", label = "Speeding", severity = "MEDIUM", colorHex = "#FFB300"),
        PlateReportTagUiModel(code = "TAILGATING", label = "Tailgating", severity = "MEDIUM", colorHex = "#FFB300"),
        PlateReportTagUiModel(code = "HONKING", label = "Honking", severity = "LOW", colorHex = "#4CAF50")
    )
    return allTags.take(count)
}
