package com.mefy.platemate.presentation.features.main.discover.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMStatCard
import com.mefy.platemate.presentation.features.uimodel.DiscoverMetricUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverMetricUiType
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun DiscoverHeroStats(
    modifier: Modifier = Modifier,
    metrics: List<DiscoverMetricUiModel>
) {
    val spacing = PMTheme.spacing

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing.s8)
    ) {
        metrics.forEach { metric ->
            PMStatCard(
                value = metric.valueText,
                label = stringResource(metric.labelResId),
                deltaText = metric.deltaText,
                deltaPositive = metric.deltaPositive,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(name = "DiscoverHeroStats", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun DiscoverHeroStatsPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        DiscoverHeroStats(
            metrics = listOf(
                DiscoverMetricUiModel(
                    type = DiscoverMetricUiType.Search,
                    valueText = "128",
                    labelResId = R.string.discover_metric_search_label,
                    periodResId = R.string.discover_metric_today_period,
                    deltaText = "+24%",
                    deltaPositive = true
                ),
                DiscoverMetricUiModel(
                    type = DiscoverMetricUiType.Comment,
                    valueText = "46",
                    labelResId = R.string.discover_metric_comment_label,
                    periodResId = R.string.discover_metric_today_period,
                    deltaText = "-8%",
                    deltaPositive = false
                ),
                DiscoverMetricUiModel(
                    type = DiscoverMetricUiType.Alert,
                    valueText = "12",
                    labelResId = R.string.discover_metric_active_alert_period,
                    periodResId = R.string.discover_metric_today_period,
                    deltaText = "+5%",
                    deltaPositive = false
                )
            )
        )
    }
}
