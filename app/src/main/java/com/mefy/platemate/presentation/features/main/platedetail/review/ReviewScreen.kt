package com.mefy.platemate.presentation.features.main.platedetail.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCommentField
import com.mefy.platemate.presentation.components.PMTagChip
import com.mefy.platemate.presentation.features.main.platedetail.review.ReviewUiState.Companion.REVIEW_COMMENT_MAX_LENGTH
import com.mefy.platemate.presentation.features.main.platedetail.review.components.OverallRatingSection
import com.mefy.platemate.presentation.features.main.platedetail.review.components.PlateInfoCard
import com.mefy.platemate.presentation.features.main.platedetail.review.components.ReviewResultPopup
import com.mefy.platemate.presentation.features.main.platedetail.review.components.ReviewShimmerContent
import com.mefy.platemate.presentation.features.main.settings.components.SectionLabel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReviewScreen(
    state: ReviewUiState,
    onAction: (ReviewUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.review_title),
            onBackClick = { onAction(ReviewUiAction.BackClicked) }
        ),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
            ) {
                PMButton(
                    text = stringResource(R.string.review_submit_button),
                    onClick = { onAction(ReviewUiAction.SubmitClicked) },
                    enabled = state.isSubmitEnabled,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { innerPadding ->
        if (state.isLoading) {
            ReviewShimmerContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
            return@PMBaseScreen
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = dims.spacing.s16, vertical = dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
        ) {
            item {
                PlateInfoCard(state = state)
            }

            item {
                SectionLabel(
                    text = stringResource(R.string.review_section_overall)
                )
            }

            item {
                OverallRatingSection(
                    rating = state.overallRating,
                    label = getRatingLabel(state.overallRating),
                    onRatingChange = { onAction(ReviewUiAction.OverallRatingChanged(it)) }
                )
            }

            item {
                SectionLabel(
                    text = stringResource(R.string.review_section_tags)
                )
            }

            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                    verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
                ) {
                    state.tags.forEach { tag ->
                        PMTagChip(
                            text = tag.label,
                            isSelected = tag.isSelected,
                            onClick = { onAction(ReviewUiAction.TagToggled(tag.code)) }
                        )
                    }
                }
            }

            item {
                SectionLabel(
                    text = stringResource(R.string.review_section_experience)
                )
            }

            item {
                PMCommentField(
                    value = state.comment,
                    onValueChange = { onAction(ReviewUiAction.CommentChanged(it)) },
                    maxLength = REVIEW_COMMENT_MAX_LENGTH,
                    placeholder = stringResource(R.string.review_comment_placeholder),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    state.submitResult?.let { result ->
        ReviewResultPopup(result = result, onAction = onAction)
    }
}

@Preview(name = "Review Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ReviewLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ReviewScreen(
            state = previewState(),
            onAction = {}
        )
    }
}

@Preview(name = "Review Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ReviewDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ReviewScreen(
            state = previewState(),
            onAction = {}
        )
    }
}

private fun previewState() = ReviewUiState(
    plateCode = "34 EK 0682",
    cityCode = "34",
    cityName = "İstanbul",
    reviewCount = 127,
    overallRating = 4,
    isLoading = false,
    tags = listOf(
        ReviewTagUiModel("POLITE", "Nazik", true),
        ReviewTagUiModel("GAVE_WAY", "Yol verdi", true),
        ReviewTagUiModel("RESPECTFUL", "Saygılı", true),
        ReviewTagUiModel("THANKED", "Teşekkür etti"),
        ReviewTagUiModel("PATIENT", "Sabırlı"),
        ReviewTagUiModel("CAREFUL", "Dikkatli"),
        ReviewTagUiModel("SAFE_DRIVING", "Güvenli sürüş"),
        ReviewTagUiModel("SPEEDING", "Hızlı"),
        ReviewTagUiModel("AGGRESSIVE", "Agresif"),
        ReviewTagUiModel("TAILGATING", "Yakın takip"),
        ReviewTagUiModel("NO_SIGNAL", "Sinyal vermedi"),
        ReviewTagUiModel("HONKER", "Kornacı")
    ),
    comment = "Çok nazik bir sürücü.",
    isAnonymous = false
)

@Composable
private fun getRatingLabel(rating: Int): String {
    if (rating == 0) return ""
    val textRes = when (rating) {
        1 -> R.string.review_rating_1
        2 -> R.string.review_rating_2
        3 -> R.string.review_rating_3
        4 -> R.string.review_rating_4
        5 -> R.string.review_rating_5
        else -> return ""
    }
    return "${stringResource(textRes)} · $rating / 5"
}
