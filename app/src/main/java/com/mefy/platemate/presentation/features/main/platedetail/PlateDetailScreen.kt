package com.mefy.platemate.presentation.features.main.platedetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.spacedByWithFooter
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMRatingBar
import com.mefy.platemate.presentation.components.PMChip
import com.mefy.platemate.presentation.features.main.platedetail.components.PlateInfoRow
import com.mefy.platemate.presentation.features.main.platedetail.components.ReviewItem
import com.mefy.platemate.presentation.features.main.platedetail.components.ReviewReportBottomSheet
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlateDetailScreen(
    modifier: Modifier = Modifier,
    state: PlateDetailUiState,
    onAction: (PlateDetailUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues()
) {

    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding), contentPadding = PaddingValues(
            horizontal = dims.spacing.s16, vertical = dims.spacing.s16
        ), verticalArrangement = spacedByWithFooter(dims.spacing.s16)
    ) {
        item {
            PlateInfoRow(
                plateCode = state.plateCode,
                cityName = state.cityName,
                ratingAverage = state.ratingAverage,
                reviewCount = state.reviewCount
            )
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
                PMSectionLabel(
                    text = stringResource(R.string.platedetail_tags_title),
                )
            }

            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                    verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
                ) {
                    state.tags.forEach { tag ->
                        PMChip(
                            label = tag.label,
                            count = tag.count
                        )
                    }

                }
            }

            item {
                HorizontalDivider(color = colors.outlineVariant)
            }
        }

        if (state.hasReviews) {
            item {
                PMSectionLabel(
                    text = stringResource(R.string.platedetail_reviews_title)
                )
            }

            items(
                items = state.reviews, key = { it.id }) { review ->
                ReviewItem(
                    review = review,
                    onAvatarClick = { onAction(PlateDetailUiAction.AvatarClicked(review.userId)) },
                    onReportClick = { onAction(PlateDetailUiAction.ReportReviewClicked(review.id)) },
                    onEditClick = { onAction(PlateDetailUiAction.EditReviewClicked(review.id)) })
            }
        }

        if (state.errorMessage == null) {
            item {
                PMButton(
                    text = if (state.isEmpty) stringResource(R.string.platedetail_first_review) else stringResource(
                        R.string.platedetail_review
                    ),
                    onClick = { onAction(PlateDetailUiAction.ReviewClicked) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = dims.spacing.s16)
                )
            }
        }
    }

    state.reviewReport?.let { report ->
        ReviewReportBottomSheet(
            report = report,
            onReasonSelected = { onAction(PlateDetailUiAction.ReportReasonSelected(it)) },
            onDescriptionChange = { onAction(PlateDetailUiAction.ReportDescriptionChanged(it)) },
            onDismiss = { onAction(PlateDetailUiAction.ReportDismissed) },
            onSubmit = { onAction(PlateDetailUiAction.ReportSubmitClicked) })
    }
}

@Preview(name = "PlateDetail With Reviews", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PlateDetailWithReviewsPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateDetailScreen(
            state = previewStateWithReviews(), onAction = {},
        )
    }
}

@Preview(name = "PlateDetail Empty", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PlateDetailEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateDetailScreen(
            state = previewStateEmpty(), onAction = {},
        )
    }
}

@Preview(name = "PlateDetail Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun PlateDetailDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PlateDetailScreen(
            state = previewStateEmpty(), onAction = {},
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
        PlateReviewUiModel(
            1,
            "ahmetk",
            "Ahmet K.",
            null,
            5,
            "2024-12-01",
            "Çok nazik bir sürücü, teşekkür etti.",
            listOf("POLITE", "GAVE_WAY")
        ), PlateReviewUiModel(
            2,
            "zeynept",
            "Zeynep T.",
            null,
            4,
            "2024-11-30",
            "Yol verdi, saygılı davrandı.",
            listOf("GAVE_WAY")
        ), PlateReviewUiModel(
            3,
            "muratd",
            "Murat D.",
            null,
            5,
            "2024-11-28",
            "Trafik kurallarına uyuyor, örnek sürücü.",
            emptyList()
        )
    ),
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
)
