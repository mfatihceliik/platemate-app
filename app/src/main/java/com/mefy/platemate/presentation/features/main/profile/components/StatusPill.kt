package com.mefy.platemate.presentation.features.main.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.features.main.profile.model.ProfileReviewStatusUi
import com.mefy.platemate.presentation.features.main.profile.model.StatusPillStyle
import com.mefy.platemate.presentation.theme.PMColors
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

/**
 * Review durumunu (onaylandı/beklemede/reddedildi …) arka plan + metin rengine eşler.
 * Saf fonksiyon: hem rozet hem ileride başka yüzeyler aynı eşlemeyi paylaşır.
 */
internal fun reviewStatusStyle(
    status: ProfileReviewStatusUi,
    colors: PMColors
): StatusPillStyle = when (status) {
    ProfileReviewStatusUi.APPROVED -> StatusPillStyle(
        label = R.string.profile_review_status_approved,
        background = colors.categoryGreenBg,
        foreground = colors.categoryGreenFg
    )
    ProfileReviewStatusUi.PENDING_REVIEW -> StatusPillStyle(
        label = R.string.profile_review_status_pending_review,
        background = colors.categoryOrangeBg,
        foreground = colors.categoryOrangeFg
    )
    ProfileReviewStatusUi.REJECTED -> StatusPillStyle(
        label = R.string.profile_review_status_rejected,
        background = colors.errorContainer,
        foreground = colors.onErrorContainer
    )
    ProfileReviewStatusUi.REMOVED_BY_USER -> StatusPillStyle(
        label = R.string.profile_review_status_removed_by_user,
        background = colors.categoryIndigoBg,
        foreground = colors.categoryIndigoFg
    )
    ProfileReviewStatusUi.REMOVED_BY_MODERATOR -> StatusPillStyle(
        label = R.string.profile_review_status_removed_by_moderator,
        background = colors.categoryIndigoBg,
        foreground = colors.categoryIndigoFg
    )
    ProfileReviewStatusUi.REMOVED_BY_LEGAL_REQUEST -> StatusPillStyle(
        label = R.string.profile_review_status_removed_by_legal_request,
        background = colors.categoryIndigoBg,
        foreground = colors.categoryIndigoFg
    )
    ProfileReviewStatusUi.UNKNOWN -> StatusPillStyle(
        label = R.string.profile_review_status_unknown,
        background = colors.surfaceVariant,
        foreground = colors.textSecondary
    )
}

/** Yalnız çip çizen compact durum rozeti (tarih içermez). Aktivite kartlarının içinde kullanılır. */
@Composable
internal fun ReviewStatusBadge(
    status: ProfileReviewStatusUi,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.pmColors
    val style = reviewStatusStyle(status, colors)
    StatusBadge(
        text = stringResource(style.label),
        background = style.background,
        foreground = style.foreground,
        modifier = modifier
    )
}

/** Genel tonal rozet (durum kodu, etiket vb. için). */
@Composable
internal fun StatusBadge(
    text: String,
    background: Color,
    foreground: Color,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    Box(
        modifier = modifier
            .background(background, RoundedCornerShape(dims.radius.rFull))
            .padding(dims.spacing.s4)
    ) {
        PMText(
            text = text,
            fontSize = dims.fontSize.sm,
            fontWeight = FontWeight.Medium,
            color = foreground
        )
    }
}

@Preview(name = "StatusBadge Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun StatusBadgeLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        StatusBadgePreviewContent()
    }
}

@Preview(name = "StatusBadge Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun StatusBadgeDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        StatusBadgePreviewContent()
    }
}

@Composable
private fun StatusBadgePreviewContent() {
    val dims = MaterialTheme.pmDimensions
    Column(
        modifier = Modifier.padding(dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        ReviewStatusBadge(status = ProfileReviewStatusUi.APPROVED)
        ReviewStatusBadge(status = ProfileReviewStatusUi.PENDING_REVIEW)
        ReviewStatusBadge(status = ProfileReviewStatusUi.REJECTED)
    }
}
