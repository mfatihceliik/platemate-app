package com.mefy.platemate.presentation.features.main.discover.components

import android.graphics.Color.parseColor
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMChip
import com.mefy.platemate.presentation.features.uimodel.DiscoverReportTypeCountUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun DiscoverTopReportTypes(
    reportTypes: List<DiscoverReportTypeCountUiModel>,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val fallbackAccent = MaterialTheme.pmColors.primary

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        items(count = reportTypes.size, key = { reportTypes[it].code }) { index ->
            val reportType = reportTypes[index]
            PMChip(
                label = reportType.label,
                count = reportType.count,
                accentColor = parseHexColor(reportType.colorHex, fallbackAccent),
                dense = true
            )
        }
    }
}

private fun parseHexColor(hex: String, fallback: Color): Color =
    runCatching { Color(parseColor(hex)) }.getOrDefault(fallback)

@Preview(name = "DiscoverTopReportTypes", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun DiscoverTopReportTypesPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        DiscoverTopReportTypes(
            reportTypes = listOf(
                DiscoverReportTypeCountUiModel(code = "DANGEROUS_DRIVING", label = "Tehlikeli Sürüş", colorHex = "#E53935", count = 14),
                DiscoverReportTypeCountUiModel(code = "PARKING", label = "Hatalı Park", colorHex = "#FFB300", count = 9),
                DiscoverReportTypeCountUiModel(code = "COURTESY", label = "Nezaketsizlik", colorHex = "#8E24AA", count = 5)
            )
        )
    }
}
