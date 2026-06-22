package com.mefy.platemate.presentation.features.main.platedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.components.PMRatingBar
import com.mefy.platemate.presentation.features.main.platedetail.components.EmptyPlateState
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.features.main.platedetail.components.PlateDetailShimmerContent
import com.mefy.platemate.presentation.features.main.platedetail.components.PlateInfoRow
import com.mefy.platemate.presentation.features.main.platedetail.components.ReviewItem
import com.mefy.platemate.presentation.features.main.platedetail.components.TagChipWithCount
import com.mefy.platemate.presentation.features.main.settings.components.SectionLabel
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

    val status = when {
        state.isLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage)
        else -> ScreenStatus.Content
    }

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
        status = status,
        onRetry = { onAction(PlateDetailUiAction.RetryClicked) },
        loading = { innerPadding ->
            PlateDetailShimmerContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        },
        bottomBar = {
            if (state.errorMessage == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.surface)
                        .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
                ) {
                    PMButton(
                        text = if (state.isEmpty) stringResource(R.string.platedetail_first_review) else stringResource(
                            R.string.platedetail_review
                        ),
                        onClick = { onAction(PlateDetailUiAction.ReviewClicked) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(
                        horizontal = dims.spacing.s16,
                        vertical = dims.spacing.s16
                    ),
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
                                        starNumber = item.stars, percentage = item.percentage
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
                            SectionLabel(
                                text = stringResource(R.string.platedetail_tags_title),
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
                            SectionLabel(
                                text = stringResource(R.string.platedetail_reviews_title)
                            )
                        }

                        items(
                            items = state.reviews, key = { it.id }) { review ->
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

@Preview(name = "PlateDetail With Reviews", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PlateDetailWithReviewsPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateDetailScreen(
            state = previewStateWithReviews(), onAction = {})
    }
}

@Preview(name = "PlateDetail Empty", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PlateDetailEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateDetailScreen(
            state = previewStateEmpty(), onAction = {})
    }
}

@Preview(name = "PlateDetail Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun PlateDetailDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PlateDetailScreen(
            state = previewStateEmpty(), onAction = {})
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
        PlateReviewUiModel(
            1,
            "ahmetk",
            "Ahmet K.",
            "AK",
            null,
            5,
            "2024-12-01",
            "Çok nazik bir sürücü, teşekkür etti.",
            listOf("POLITE", "GAVE_WAY")
        ),
        PlateReviewUiModel(
            2,
            "zeynept",
            "Zeynep T.",
            "ZT",
            null,
            4,
            "2024-11-30",
            "Yol verdi, saygılı davrandı.",
            listOf("GAVE_WAY")
        ),
        PlateReviewUiModel(
            3,
            "muratd",
            "Murat D.",
            "MD",
            null,
            5,
            "2024-11-28",
            "Trafik kurallarına uyuyor, örnek sürücü.",
            emptyList()
        )
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
