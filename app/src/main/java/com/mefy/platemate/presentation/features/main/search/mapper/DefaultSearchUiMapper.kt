package com.mefy.platemate.presentation.features.main.search.mapper

import com.mefy.platemate.domain.model.plate.PlateSearchResult
import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.domain.model.search.RecentSearch
import com.mefy.platemate.domain.model.search.SavedPlate
import com.mefy.platemate.presentation.features.main.search.model.SearchRecentUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateReportTagUiModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultSearchUiMapper @Inject constructor() : SearchUiMapper {

    override fun detectCityFromPlate(input: String): String? {
        if (input.isBlank()) return null

        val digitsMatch = "^\\d{1,2}".toRegex().find(input)
        val digits = digitsMatch?.value ?: return null
        val code = digits.padStart(2, '0')
        return CITY_MAP[code]
    }

    override fun resolveCityName(cityName: String?, normalizedPlate: String): String? =
        normalizeCityName(cityName) ?: detectCityFromPlate(normalizedPlate)

    override fun mapRecentSearch(
        result: PlateSearchResult,
        normalizedPlate: String,
        formattedPlate: String,
        detectedCityName: String?
    ): RecentSearch = RecentSearch(
        normalizedPlateCode = normalizedPlate,
        formattedPlateCode = formattedPlate,
        cityName = detectedCityName,
        reportTypes = result.recentReportTypes,
        ratingAverage = result.ratingAverage,
        commentCount = result.totalReviewCount
    )

    override fun mapRecentSearchItem(item: RecentSearch, isBookmarked: Boolean): SearchRecentUiModel =
        SearchRecentUiModel(
            normalizedPlateCode = item.normalizedPlateCode,
            plateCode = item.formattedPlateCode,
            cityName = item.cityName,
            reportTags = mapReportTags(item.reportTypes),
            ratingAverage = item.ratingAverage,
            commentCount = item.commentCount,
            isBookmarked = isBookmarked
        )

    override fun mapRecentSearches(
        items: List<RecentSearch>,
        bookmarkedCodes: Set<String>
    ): List<SearchRecentUiModel> = items.map { item ->
        mapRecentSearchItem(item, isBookmarked = bookmarkedCodes.contains(item.normalizedPlateCode))
    }

    override fun mapSavedPlate(item: RecentSearch): SavedPlate = SavedPlate(
        normalizedPlateCode = item.normalizedPlateCode,
        formattedPlateCode = item.formattedPlateCode,
        cityName = item.cityName,
        ratingAverage = item.ratingAverage,
        commentCount = item.commentCount,
        reportTypes = item.reportTypes,
        savedAt = System.currentTimeMillis()
    )

    private fun mapReportTags(reportTypes: List<ReportType>): List<PlateReportTagUiModel> =
        reportTypes.map { reportType ->
            PlateReportTagUiModel(
                code = reportType.code,
                label = reportType.label,
                severity = reportType.severity,
                colorHex = reportType.colorHex
            )
        }

    private fun normalizeCityName(cityName: String?): String? {
        val value = cityName?.trim()?.takeIf { it.isNotBlank() } ?: return null
        if (!value.isLikelyMojibake()) return value

        val repaired = runCatching {
            String(value.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
        }.getOrElse { return value }

        return if (repaired.contains('\uFFFD')) value else repaired
    }

    private fun String.isLikelyMojibake(): Boolean =
        contains('\u00c3') || contains('\u00c4') || contains('\u00c5') || contains('\u00e2')

    private companion object {
        val CITY_MAP = mapOf(
            "01" to "Adana", "02" to "Ad\u0131yaman", "03" to "Afyonkarahisar", "04" to "A\u011fr\u0131", "05" to "Amasya",
            "06" to "Ankara", "07" to "Antalya", "08" to "Artvin", "09" to "Ayd\u0131n", "10" to "Bal\u0131kesir",
            "11" to "Bilecik", "12" to "Bing\u00f6l", "13" to "Bitlis", "14" to "Bolu", "15" to "Burdur",
            "16" to "Bursa", "17" to "\u00c7anakkale", "18" to "\u00c7ank\u0131r\u0131", "19" to "\u00c7orum", "20" to "Denizli",
            "21" to "Diyarbak\u0131r", "22" to "Edirne", "23" to "Elaz\u0131\u011f", "24" to "Erzincan", "25" to "Erzurum",
            "26" to "Eski\u015fehir", "27" to "Gaziantep", "28" to "Giresun", "29" to "G\u00fcm\u00fc\u015fhane", "30" to "Hakkari",
            "31" to "Hatay", "32" to "Isparta", "33" to "Mersin", "34" to "\u0130stanbul", "35" to "\u0130zmir",
            "36" to "Kars", "37" to "Kastamonu", "38" to "Kayseri", "39" to "K\u0131rklareli", "40" to "K\u0131r\u015fehir",
            "41" to "Kocaeli", "42" to "Konya", "43" to "K\u00fctahya", "44" to "Malatya", "45" to "Manisa",
            "46" to "Kahramanmara\u015f", "47" to "Mardin", "48" to "Mu\u011fla", "49" to "Mu\u015f", "50" to "Nev\u015fehir",
            "51" to "Ni\u011fde", "52" to "Ordu", "53" to "Rize", "54" to "Sakarya", "55" to "Samsun",
            "56" to "Siirt", "57" to "Sinop", "58" to "Sivas", "59" to "Tekirda\u011f", "60" to "Tokat",
            "61" to "Trabzon", "62" to "Tunceli", "63" to "\u015eanl\u0131urfa", "64" to "U\u015fak", "65" to "Van",
            "66" to "Yozgat", "67" to "Zonguldak", "68" to "Aksaray", "69" to "Bayburt", "70" to "Karaman",
            "71" to "K\u0131r\u0131kkale", "72" to "Batman", "73" to "\u015e\u0131rnak", "74" to "Bart\u0131n", "75" to "Ardahan",
            "76" to "I\u011fd\u0131r", "77" to "Yalova", "78" to "Karab\u00fck", "79" to "Kilis", "80" to "Osmaniye",
            "81" to "D\u00fczce"
        )
    }
}
