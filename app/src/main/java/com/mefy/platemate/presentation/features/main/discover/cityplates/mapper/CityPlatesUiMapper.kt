package com.mefy.platemate.presentation.features.main.discover.cityplates.mapper

import com.mefy.platemate.domain.model.discovery.CityPlateActivity
import com.mefy.platemate.domain.usecase.search.FormatTurkishPlateInputUseCase
import com.mefy.platemate.presentation.common.formatter.NumberFormatter
import com.mefy.platemate.presentation.features.main.discover.cityplates.CityPlateUiModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityPlatesUiMapper @Inject constructor(
    private val formatTurkishPlateInputUseCase: FormatTurkishPlateInputUseCase
) {
    fun mapPlates(items: List<CityPlateActivity>, startRank: Int): List<CityPlateUiModel> =
        items.mapIndexed { index, activity ->
            CityPlateUiModel(
                id = activity.plateCode,
                rank = startRank + index,
                plateCode = formatTurkishPlateInputUseCase(activity.plateCode),
                ratingText = NumberFormatter.formatRating(activity.ratingAverage),
                reviewCount = activity.reviewCount,
                todayReviewCount = activity.todayReviewCount
            )
        }
}
