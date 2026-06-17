package com.mefy.platemate.presentation.features.main.review

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMPlateBadge
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMRatingStars
import com.mefy.platemate.presentation.components.PMTagChip
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PlateBadgeSize
import com.mefy.platemate.presentation.components.model.PMTextStyle
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
        topBarConfig = PMTopBarConfig.Simple(
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = dims.spacing.s8)
                ) {
                    Checkbox(
                        checked = state.isAnonymous,
                        onCheckedChange = { onAction(ReviewUiAction.AnonymousToggled(it)) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = colors.textLabel
                        )
                    )
                    Text(
                        text = stringResource(R.string.review_anonymous_checkbox),
                        fontSize = dims.fontSize.sm,
                        color = colors.textSecondary
                    )
                }

                PMButton(
                    text = stringResource(R.string.review_submit_button),
                    onClick = { onAction(ReviewUiAction.SubmitClicked) },
                    enabled = state.isSubmitEnabled,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { innerPadding ->
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
                PMText(text = stringResource(R.string.review_section_overall), style = PMTextStyle.SectionLabel)
            }

            item {
                OverallRatingSection(
                    rating = state.overallRating,
                    label = state.ratingLabel,
                    onRatingChange = { onAction(ReviewUiAction.OverallRatingChanged(it)) }
                )
            }

            item {
                PMText(text = stringResource(R.string.review_section_detailed), style = PMTextStyle.SectionLabel)
            }

            item {
                SubRatingsCard(
                    subRatings = state.subRatings,
                    onSubRatingChange = { key, rating ->
                        onAction(ReviewUiAction.SubRatingChanged(key, rating))
                    }
                )
            }

            item {
                PMText(text = stringResource(R.string.review_section_tags), style = PMTextStyle.SectionLabel)
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
                PMText(text = stringResource(R.string.review_section_experience), style = PMTextStyle.SectionLabel)
            }

            item {
                CommentSection(
                    comment = state.comment,
                    onCommentChange = { onAction(ReviewUiAction.CommentChanged(it)) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(dims.spacing.s8))
            }
        }
    }
}

@Composable
private fun PlateInfoCard(state: ReviewUiState) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dims.radius.r16))
            .background(colors.surfaceSecondary)
            .border(dims.stroke.st1, colors.cardBorder, RoundedCornerShape(dims.radius.r16))
            .padding(dims.spacing.s16),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PMPlateBadge(
            cityCode = state.cityCode,
            size = PlateBadgeSize.Review
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
        ) {
            Text(
                text = state.plateCode,
                fontSize = dims.fontSize.xl,
                fontWeight = FontWeight.ExtraBold,
                color = colors.textPrimary
            )
            Text(
                text = if (state.cityName.isNotBlank()) {
                    stringResource(R.string.review_city_review_count, state.cityName, state.reviewCount)
                } else {
                    stringResource(R.string.review_count_format, state.reviewCount)
                },
                fontSize = dims.fontSize.sm,
                color = colors.textTertiary
            )
        }
    }
}

@Composable
private fun OverallRatingSection(
    rating: Int,
    label: String,
    onRatingChange: (Int) -> Unit
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s8),
        modifier = Modifier.fillMaxWidth()
    ) {
        PMRatingStars(
            rating = rating,
            starSize = dims.spacing.s32,
            gap = dims.spacing.s8,
            interactive = true,
            onRatingChange = onRatingChange
        )
        if (label.isNotBlank()) {
            Text(
                text = label,
                fontSize = dims.fontSize.md,
                fontWeight = FontWeight.SemiBold,
                color = colors.textSecondary
            )
        }
    }
}

@Composable
private fun SubRatingsCard(
    subRatings: List<SubRatingUiModel>,
    onSubRatingChange: (String, Int) -> Unit
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dims.radius.r16))
            .background(MaterialTheme.colorScheme.surface)
            .border(dims.stroke.st1, colors.cardBorder, RoundedCornerShape(dims.radius.r16))
            .padding(dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        subRatings.forEach { sub ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sub.label,
                    fontSize = dims.fontSize.md,
                    fontWeight = FontWeight.Medium,
                    color = colors.textSecondary
                )
                PMRatingStars(
                    rating = sub.rating,
                    starSize = dims.spacing.s16,
                    gap = dims.spacing.s4,
                    interactive = true,
                    onRatingChange = { onSubRatingChange(sub.key, it) }
                )
            }
        }
    }
}

@Composable
private fun CommentSection(
    comment: String,
    onCommentChange: (String) -> Unit
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        OutlinedTextField(
            value = comment,
            onValueChange = onCommentChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = {
                Text(
                    text = stringResource(R.string.review_comment_placeholder),
                    color = colors.textTertiary
                )
            },
            shape = RoundedCornerShape(dims.radius.r16),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = colors.cardBorder,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = colors.textPrimary)
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "${comment.length}/240",
                fontSize = 11.sp,
                color = colors.textLabel,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
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
    subRatings = listOf(
        SubRatingUiModel("courtesy", "Nezaket", 5),
        SubRatingUiModel("traffic_respect", "Trafik Saygısı", 4),
        SubRatingUiModel("driving_safety", "Sürüş Güvenliği", 4)
    ),
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
