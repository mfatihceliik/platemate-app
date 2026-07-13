package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.common.text.NumberFormatter
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.components.variant.PMPlateCardStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMPlateCard(
    id: String,
    rank: Int,
    plateNumber: String,
    rating: String,
    commentCount: Long,
    searchCount: Long,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    style: PMPlateCardStyle = PMPlateCardStyle.Classic,
    isBookmarked: Boolean = false,
    onBookmarkClick: ((String) -> Unit)? = null
) {
    val cardClick = remember(id, onClick) { { onClick(id) } }
    val bookmarkClick = remember(id, onBookmarkClick) {
        onBookmarkClick?.let { callback -> { callback(id) } }
    }

    when (style) {
        PMPlateCardStyle.Classic -> ClassicPlateCard(
            rank = rank,
            plateNumber = plateNumber,
            rating = rating,
            commentCount = commentCount,
            searchCount = searchCount,
            cardClick = cardClick,
            isBookmarked = isBookmarked,
            bookmarkClick = bookmarkClick,
            modifier = modifier
        )
        PMPlateCardStyle.Spotlight -> SpotlightPlateCard(
            rank = rank,
            plateNumber = plateNumber,
            rating = rating,
            commentCount = commentCount,
            searchCount = searchCount,
            cardClick = cardClick,
            isBookmarked = isBookmarked,
            bookmarkClick = bookmarkClick,
            modifier = modifier
        )
        PMPlateCardStyle.Minimal -> MinimalPlateCard(
            rank = rank,
            plateNumber = plateNumber,
            rating = rating,
            commentCount = commentCount,
            cardClick = cardClick,
            isBookmarked = isBookmarked,
            bookmarkClick = bookmarkClick,
            modifier = modifier
        )
    }
}

@Composable
private fun ClassicPlateCard(
    rank: Int,
    plateNumber: String,
    rating: String,
    commentCount: Long,
    searchCount: Long,
    cardClick: () -> Unit,
    isBookmarked: Boolean,
    bookmarkClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMCard(
        modifier = modifier,
        onClick = cardClick,
        padding = PaddingValues(dims.spacing.s12)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RankBadge(rank = rank)

            PMPlateBadge(
                plate = plateNumber,
                size = dims.sizing.plateBadgeMd
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                PlateMetric(
                    icon = Icons.Filled.Star,
                    value = rating,
                    iconTint = colors.iconStar
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlateMetric(
                        icon = Icons.AutoMirrored.Outlined.Chat,
                        value = NumberFormatter.formatCompact(commentCount),
                        iconTint = colors.iconDefault
                    )
                    if (searchCount > 0) {
                        PlateMetric(
                            icon = Icons.Outlined.Search,
                            value = NumberFormatter.formatCompact(searchCount),
                            iconTint = colors.iconDefault
                        )
                    }
                }
            }

            BookmarkButton(isBookmarked = isBookmarked, bookmarkClick = bookmarkClick)
        }
    }
}

@Composable
private fun SpotlightPlateCard(
    rank: Int,
    plateNumber: String,
    rating: String,
    commentCount: Long,
    searchCount: Long,
    cardClick: () -> Unit,
    isBookmarked: Boolean,
    bookmarkClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val shape = RoundedCornerShape(dims.radius.r16)

    Box(
        modifier = modifier
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colors.primary.copy(alpha = 0.16f),
                        colors.surface
                    )
                )
            )
            .border(dims.stroke.st1, colors.primary.copy(alpha = 0.35f), shape)
            .debouncedClickable(onClick = cardClick)
            .padding(dims.spacing.s16)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RankBadge(rank = rank)

            PMPlateBadge(
                plate = plateNumber,
                size = dims.sizing.plateBadgeLg
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                PlateMetric(
                    icon = Icons.Filled.Star,
                    value = rating,
                    iconTint = colors.iconStar,
                    valueColor = colors.primary
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlateMetric(
                        icon = Icons.AutoMirrored.Outlined.Chat,
                        value = NumberFormatter.formatCompact(commentCount),
                        iconTint = colors.iconDefault
                    )
                    if (searchCount > 0) {
                        PlateMetric(
                            icon = Icons.Outlined.Search,
                            value = NumberFormatter.formatCompact(searchCount),
                            iconTint = colors.iconDefault
                        )
                    }
                }
            }

            BookmarkButton(isBookmarked = isBookmarked, bookmarkClick = bookmarkClick)
        }
    }
}

@Composable
private fun MinimalPlateCard(
    rank: Int,
    plateNumber: String,
    rating: String,
    commentCount: Long,
    cardClick: () -> Unit,
    isBookmarked: Boolean,
    bookmarkClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val shape = RoundedCornerShape(dims.radius.r12)

    Row(
        modifier = modifier
            .clip(shape)
            .background(colors.surface)
            .debouncedClickable(onClick = cardClick)
            .padding(horizontal = dims.spacing.s12, vertical = dims.spacing.s8),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PMText(
            text = "#$rank",
            fontSize = dims.fontSize.sm,
            fontWeight = FontWeight.SemiBold,
            color = colors.textTertiary
        )

        PMPlateBadge(
            plate = plateNumber,
            size = dims.sizing.plateBadgeSm
        )

        Spacer(modifier = Modifier.weight(1f))

        PlateMetric(
            icon = Icons.Filled.Star,
            value = rating,
            iconTint = colors.iconStar
        )
        PlateMetric(
            icon = Icons.AutoMirrored.Outlined.Chat,
            value = NumberFormatter.formatCompact(commentCount),
            iconTint = colors.iconDefault
        )

        BookmarkButton(isBookmarked = isBookmarked, bookmarkClick = bookmarkClick)
    }
}

@Composable
private fun RankBadge(rank: Int) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val isFirst = rank == 1

    Box(
        modifier = Modifier
            .size(dims.sizing.rankBadgeSize)
            .clip(RoundedCornerShape(dims.radius.r8))
            .background(if (isFirst) colors.rankFirstBg else colors.rankOtherBg),
        contentAlignment = Alignment.Center
    ) {
        PMText(
            text = rank.toString(),
            color = if (isFirst) colors.rankFirstFg else colors.rankOtherFg,
            fontSize = dims.fontSize.md,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun BookmarkButton(isBookmarked: Boolean, bookmarkClick: (() -> Unit)?) {
    if (bookmarkClick == null) return
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMIconButton(
        imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
        onClick = bookmarkClick,
        size = dims.sizing.iconSm,
        iconColor = if (isBookmarked) colors.primary else colors.iconDefault
    )
}

@Composable
private fun PlateMetric(
    icon: ImageVector,
    value: String,
    iconTint: Color,
    valueColor: Color = MaterialTheme.pmColors.textSecondary
) {
    val dims = MaterialTheme.pmDimensions
    Row(
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PMIcon(
            imageVector = icon,
            size = dims.sizing.iconXs,
            tint = iconTint
        )
        PMText(
            text = value,
            fontSize = dims.fontSize.sm,
            fontWeight = FontWeight.SemiBold,
            color = valueColor
        )
    }
}

@Preview(name = "PMPlateCard", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMPlateCardPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.pmColors.background)
                .padding(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            PMPlateCard(id = "1", rank = 1, plateNumber = "34 EK 0682", rating = "4.8", commentCount = 12, searchCount = 1240, onClick = {}, modifier = Modifier.fillMaxWidth())
            PMPlateCard(id = "2", rank = 2, plateNumber = "06 ABC 123", rating = "4.6", commentCount = 9, searchCount = 910, onClick = {}, modifier = Modifier.fillMaxWidth(), isBookmarked = true, onBookmarkClick = {})
            PMPlateCard(id = "3", rank = 3, plateNumber = "35 T 4421", rating = "4.3", commentCount = 6, searchCount = 0, onClick = {}, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(name = "PMPlateCard Styles", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMPlateCardStylesPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.pmColors.background)
                .padding(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            PMPlateCard(id = "1", rank = 1, plateNumber = "34 EK 0682", rating = "4.8", commentCount = 12, searchCount = 1240, onClick = {}, style = PMPlateCardStyle.Classic, isBookmarked = true, onBookmarkClick = {}, modifier = Modifier.fillMaxWidth())
            PMPlateCard(id = "2", rank = 2, plateNumber = "06 ABC 123", rating = "4.6", commentCount = 9, searchCount = 910, onClick = {}, style = PMPlateCardStyle.Spotlight, onBookmarkClick = {}, modifier = Modifier.fillMaxWidth())
            PMPlateCard(id = "3", rank = 3, plateNumber = "35 T 4421", rating = "4.3", commentCount = 6, searchCount = 0, onClick = {}, style = PMPlateCardStyle.Minimal, onBookmarkClick = {}, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(name = "PMPlateCard Styles Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMPlateCardStylesDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.pmColors.background)
                .padding(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            PMPlateCard(id = "1", rank = 1, plateNumber = "34 EK 0682", rating = "4.8", commentCount = 12, searchCount = 1240, onClick = {}, style = PMPlateCardStyle.Spotlight, isBookmarked = true, onBookmarkClick = {}, modifier = Modifier.fillMaxWidth())
            PMPlateCard(id = "2", rank = 2, plateNumber = "06 ABC 123", rating = "4.6", commentCount = 9, searchCount = 910, onClick = {}, style = PMPlateCardStyle.Minimal, onBookmarkClick = {}, modifier = Modifier.fillMaxWidth())
        }
    }
}
