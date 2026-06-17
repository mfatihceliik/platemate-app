package com.mefy.platemate.presentation.features.main.review

import androidx.compose.runtime.Immutable

@Immutable
data class ReviewUiState(
    val plateCode: String = "",
    val cityCode: String = "",
    val cityName: String = "",
    val reviewCount: Long = 0,
    val overallRating: Int = 0,
    val subRatings: List<SubRatingUiModel> = defaultSubRatings(),
    val tags: List<ReviewTagUiModel> = defaultTags(),
    val comment: String = "",
    val isAnonymous: Boolean = false,
    val isSubmitting: Boolean = false
) {
    val ratingLabel: String
        get() = when (overallRating) {
            1 -> "Çok kötü · 1 / 5"
            2 -> "Kötü · 2 / 5"
            3 -> "Orta · 3 / 5"
            4 -> "Çok iyi · 4 / 5"
            5 -> "Mükemmel · 5 / 5"
            else -> ""
        }

    val isSubmitEnabled: Boolean
        get() = overallRating > 0 && !isSubmitting
}

@Immutable
data class SubRatingUiModel(
    val key: String,
    val label: String,
    val rating: Int = 0
)

@Immutable
data class ReviewTagUiModel(
    val code: String,
    val label: String,
    val isSelected: Boolean = false
)

private fun defaultSubRatings() = listOf(
    SubRatingUiModel("courtesy", "Nezaket"),
    SubRatingUiModel("traffic_respect", "Trafik Saygısı"),
    SubRatingUiModel("driving_safety", "Sürüş Güvenliği")
)

private fun defaultTags() = listOf(
    ReviewTagUiModel("POLITE", "Nazik"),
    ReviewTagUiModel("GAVE_WAY", "Yol verdi"),
    ReviewTagUiModel("RESPECTFUL", "Saygılı"),
    ReviewTagUiModel("THANKED", "Teşekkür etti"),
    ReviewTagUiModel("PATIENT", "Sabırlı"),
    ReviewTagUiModel("CAREFUL", "Dikkatli"),
    ReviewTagUiModel("SAFE_DRIVING", "Güvenli sürüş"),
    ReviewTagUiModel("SPEEDING", "Hızlı"),
    ReviewTagUiModel("AGGRESSIVE", "Agresif"),
    ReviewTagUiModel("TAILGATING", "Yakın takip"),
    ReviewTagUiModel("NO_SIGNAL", "Sinyal vermedi"),
    ReviewTagUiModel("HONKER", "Kornacı")
)
