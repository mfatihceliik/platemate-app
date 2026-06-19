package com.mefy.platemate.presentation.features.main.platedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMPlateBadge
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.components.PMRatingBar
import com.mefy.platemate.presentation.components.PMRatingStars
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.model.PlateBadgeSize
import com.mefy.platemate.presentation.features.main.platedetail.components.PlateDetailShimmerContent
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlateDetailScreen(
    state: PlateDetailUiState,
    onAction: (PlateDetailUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.platedetail_title),
            onBackClick = { onAction(PlateDetailUiAction.BackClicked) },
            actions = {
                PMIconButton(
                    onClick = { onAction(PlateDetailUiAction.BookmarkClicked) },
                ) {
                    PMIcon(
                        imageVector = if (state.isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                        size = dims.sizing.iconHuge,
                        contentDescription = stringResource(R.string.platedetail_bookmark),
                    )
                }
            }
        ),
        containerColor = colors.surface,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.surface)
                    .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
            ) {
                PMButton(
                    text = if (state.isEmpty) stringResource(R.string.platedetail_first_review) else stringResource(R.string.platedetail_review),
                    onClick = { onAction(PlateDetailUiAction.ReviewClicked) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { innerPadding ->
        if (state.isLoading) {
            PlateDetailShimmerContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = dims.spacing.s16, vertical = dims.spacing.s16),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
            ) {
                item {
                    PlateInfoRow(state = state)
                }

                item {
                    HorizontalDivider(color = colors.outlineVariant)
                }

                if (state.hasRatingBreakdown) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
                            state.ratingBreakdown.forEach { item ->
                                PMRatingBar(
                                    starNumber = item.stars,
                                    percentage = item.percentage
                                )
                            }
                        }
                    }

                    item {
                        HorizontalDivider(color = colors.outlineVariant)
                    }
                }

                if (state.hasTags) {
                    item {
                        PMText(
                            text = stringResource(R.string.platedetail_tags_title),
                            style = PMTextStyle.SectionLabel
                        )
                    }

                    item {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
                        ) {
                            state.tags.forEach { tag ->
                                TagChipWithCount(label = tag.label, count = tag.count)
                            }
                        }
                    }

                    item {
                        HorizontalDivider(color = colors.outlineVariant)
                    }
                }

                if (state.hasReviews) {
                    item {
                        PMText(
                            text = stringResource(R.string.platedetail_reviews_title),
                            style = PMTextStyle.SectionLabel
                        )
                    }

                    items(
                        items = state.reviews,
                        key = { it.id }
                    ) { review ->
                        ReviewItem(review = review)
                    }
                }

                if (state.isEmpty) {
                    item {
                        EmptyPlateState()
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyPlateState() {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dims.spacing.s16),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
    ) {
        Box(
            modifier = Modifier
                .size(dims.sizing.plateBadgeLarge)
                .clip(CircleShape)
                .background(colors.searchFieldBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.RateReview,
                contentDescription = null,
                tint = colors.textLabel,
                modifier = Modifier.size(dims.sizing.rankBadgeSize)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            PMText(
                text = stringResource(R.string.platedetail_empty_reviews_title),
                fontSize = dims.fontSize.md,
                color = colors.textPrimary,
                textAlign = TextAlign.Center
            )
            PMText(
                text = stringResource(R.string.platedetail_empty_reviews_subtitle),
                fontSize = dims.fontSize.sm,
                color = colors.textTertiary,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(dims.spacing.s8))

        EmptyInfoRow(
            icon = Icons.Filled.Star,
            text = stringResource(R.string.platedetail_rate_driving)
        )
        EmptyInfoRow(
            icon = Icons.Outlined.Sell,
            text = stringResource(R.string.platedetail_tag_driver)
        )
        EmptyInfoRow(
            icon = Icons.Outlined.RateReview,
            text = stringResource(R.string.platedetail_share_comment)
        )
    }
}

@Composable
private fun EmptyInfoRow(icon: ImageVector, text: String) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Row(
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s10),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = dims.spacing.s24)
    ) {
        PMIcon(
            imageVector = icon,
        )
        PMText(
            text = text,
            color = colors.textSecondary
        )
    }
}

@Composable
private fun PlateInfoRow(state: PlateDetailUiState) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PMPlateBadge(
            cityCode = state.cityCode,
            size = PlateBadgeSize.Large
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
        ) {
            PMText(
                text = state.plateCode,
                fontSize = dims.fontSize.xl,
                fontWeight = FontWeight.ExtraBold,
                color = colors.textPrimary
            )
            PMText(
                text = state.cityName,
                color = colors.textTertiary
            )
        }

        if (state.reviewCount > 0) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                PMText(
                    text = String.format("%.1f", state.ratingAverage),
                    fontSize = dims.fontSize.lg,
                    color = colors.textPrimary
                )
                PMRatingStars(
                    rating = state.ratingAverage.toInt(),
                )
                PMText(
                    text = stringResource(R.string.platedetail_review_count, state.reviewCount),
                    color = colors.textLabel
                )
            }
        } else {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                PMText(
                    text = "—",
                    fontSize = dims.fontSize.lg,
                    color = colors.textLabel
                )
                PMRatingStars(
                    rating = 0,
                )
                PMText(
                    text = stringResource(R.string.platedetail_no_rating),
                    color = colors.textLabel
                )
            }
        }
    }
}

@Composable
private fun TagChipWithCount(label: String, count: Int) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Box(
        modifier = Modifier
            .height(dims.sizing.chipHeight)
            .clip(RoundedCornerShape(dims.radius.rFull))
            .background(colors.surfaceVariant)
            .padding(horizontal = dims.spacing.s16),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMText(
                text = label,
                fontSize = dims.fontSize.md,
                fontWeight = FontWeight.SemiBold,
                color = colors.textSecondary
            )
            PMText(
                text = count.toString(),
                color = colors.textLabel
            )
        }
    }
}

@Composable
private fun ReviewItem(review: PlateReviewUiModel) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dims.spacing.s4),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(dims.sizing.avatarSmall)
                .clip(CircleShape)
                .background(colors.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            PMText(
                text = review.initials,
                fontSize = dims.fontSize.md,
                fontWeight = FontWeight.Bold,
                color = colors.onPrimaryContainer
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PMText(
                    text = review.username,
                    fontSize = dims.fontSize.md,
                    color = colors.textPrimary
                )
                PMText(
                    text = review.timeAgo,
                    color = colors.textLabel
                )
            }

            PMRatingStars(
                rating = review.rating,
                starSize = dims.sizing.iconSm
            )

            if (review.comment.isNotBlank()) {
                PMText(
                    text = review.comment,
                    color = colors.textSecondary
                )
            }
        }
    }
}

@Preview(name = "PlateDetail With Reviews", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PlateDetailWithReviewsPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateDetailScreen(
            state = previewStateWithReviews(),
            onAction = {}
        )
    }
}

@Preview(name = "PlateDetail Empty", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PlateDetailEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateDetailScreen(
            state = previewStateEmpty(),
            onAction = {}
        )
    }
}

@Preview(name = "PlateDetail Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun PlateDetailDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PlateDetailScreen(
            state = previewStateEmpty(),
            onAction = {}
        )
    }
}

private fun previewStateWithReviews() = PlateDetailUiState(
    isLoading = false,
    plateCode = "34 EK 0682",
    cityCode = "34",
    cityName = "İstanbul",
    ratingAverage = 4.6,
    reviewCount = 127,
    ratingBreakdown = listOf(
        RatingBreakdownItem(5, 0.65f),
        RatingBreakdownItem(4, 0.20f),
        RatingBreakdownItem(3, 0.08f),
        RatingBreakdownItem(2, 0.04f),
        RatingBreakdownItem(1, 0.03f)
    ),
    tags = listOf(
        PlateTagUiModel("POLITE", "Nazik", 42),
        PlateTagUiModel("GAVE_WAY", "Yol verdi", 31),
        PlateTagUiModel("RESPECTFUL", "Saygılı", 28),
        PlateTagUiModel("PATIENT", "Sabırlı", 19)
    ),
    reviews = listOf(
        PlateReviewUiModel(1, "Ahmet K.", "AK", 5, "2024-12-01", "Çok nazik bir sürücü, teşekkür etti."),
        PlateReviewUiModel(2, "Zeynep T.", "ZT", 4, "2024-11-30", "Yol verdi, saygılı davrandı."),
        PlateReviewUiModel(3, "Murat D.", "MD", 5, "2024-11-28", "Trafik kurallarına uyuyor, örnek sürücü.")
    ),
    isBookmarked = false
)

private fun previewStateEmpty() = PlateDetailUiState(
    isLoading = false,
    plateCode = "06 ABC 123",
    cityCode = "06",
    cityName = "Ankara",
    ratingAverage = 0.0,
    reviewCount = 0,
    ratingBreakdown = emptyList(),
    tags = emptyList(),
    reviews = emptyList(),
    isBookmarked = false
)
