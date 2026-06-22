package com.mefy.platemate.presentation.features.main.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import com.mefy.platemate.presentation.components.PlateCard
import com.mefy.platemate.presentation.components.model.PlateCardDensity
import com.mefy.platemate.presentation.features.main.profile.model.PlateReviewNotificationItem
import com.mefy.platemate.presentation.features.main.profile.model.ProfileReviewStatusUi
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PlateReviewActivityCard(
    item: PlateReviewNotificationItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        PlateCard(
            plateCode = item.plateCode,
            onClick = onClick,
            density = PlateCardDensity.Standard,
            ratingAverage = item.ratingAverage,
            commentCount = item.commentCount
        )
        StatusPill(status = item.reviewStatus, createdAtText = item.createdAtText)
    }
}

@Preview(name = "PlateReviewCard Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PlateReviewActivityCardLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateReviewActivityCardPreviewContent()
    }
}

@Preview(name = "PlateReviewCard Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PlateReviewActivityCardDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PlateReviewActivityCardPreviewContent()
    }
}

@Composable
private fun PlateReviewActivityCardPreviewContent() {
    val dims = MaterialTheme.pmDimensions
    PlateReviewActivityCard(
        item = PlateReviewNotificationItem(
            id = "review_1",
            normalizedPlateCode = "34AB1234",
            plateCode = "34 AB 1234",
            ratingAverage = 4.0,
            commentCount = 1,
            reviewStatus = ProfileReviewStatusUi.APPROVED,
            createdAtText = "2026-05-27",
            sortKey = "2026-05-27T10:00:00Z"
        ),
        onClick = {},
        modifier = Modifier.padding(dims.spacing.s16)
    )
}