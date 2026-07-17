package com.mefy.platemate.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabIndicatorScope
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PMTabRow(
    selectedTabIndex: Int,
    tabs: List<PMTabItem>,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    style: PMTabRowStyle = PMTabRowStyle.Primary,
    scrollable: Boolean = false,
    containerColor: Color = MaterialTheme.pmColors.onPrimary,
    contentColor: Color = MaterialTheme.pmColors.primary,
    unselectedContentColor: Color = MaterialTheme.pmColors.textSecondary,
    indicatorColor: Color = contentColor,
    indicatorHeight: Dp = MaterialTheme.pmDimensions.spacing.s4,
    indicatorShape: Shape = RoundedCornerShape(MaterialTheme.pmDimensions.radius.r4),
    matchIndicatorContentSize: Boolean = false,
    selectedFontWeight: FontWeight = FontWeight.SemiBold,
    unselectedFontWeight: FontWeight = FontWeight.Normal,
    showDivider: Boolean = false
) {

    val indicator: @Composable TabIndicatorScope.() -> Unit = {
        TabRowDefaults.PrimaryIndicator(
            modifier = Modifier.tabIndicatorOffset(
                selectedTabIndex = selectedTabIndex,
                matchContentSize = matchIndicatorContentSize
            ),
            height = indicatorHeight,
            color = indicatorColor,
            shape = indicatorShape
        )
    }

    val divider: @Composable () -> Unit = {
        if (showDivider) {
            HorizontalDivider()
        }
    }

    val tabContent: @Composable () -> Unit = {
        tabs.forEachIndexed { index, tab ->
            PMTab(
                tab = tab,
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                selectedColor = contentColor,
                unselectedColor = unselectedContentColor,
                selectedWeight = selectedFontWeight,
                unselectedWeight = unselectedFontWeight
            )
        }
    }

    when {

        style == PMTabRowStyle.Primary && !scrollable ->
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = modifier,
                containerColor = containerColor,
                contentColor = contentColor,
                indicator = indicator,
                divider = divider,
                tabs = tabContent
            )

        style == PMTabRowStyle.Secondary && !scrollable ->
            SecondaryTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = modifier,
                containerColor = containerColor,
                contentColor = contentColor,
                indicator = indicator,
                divider = divider,
                tabs = tabContent
            )

        style == PMTabRowStyle.Primary && scrollable ->
            PrimaryScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = modifier,
                containerColor = containerColor,
                contentColor = contentColor,
                indicator = indicator,
                divider = divider,
                tabs = tabContent
            )

        else ->
            SecondaryScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = modifier,
                containerColor = containerColor,
                contentColor = contentColor,
                indicator = indicator,
                divider = divider,
                tabs = tabContent
            )
    }
}

@Composable
private fun PMTab(
    tab: PMTabItem,
    selected: Boolean,
    onClick: () -> Unit,
    selectedColor: Color,
    unselectedColor: Color,
    selectedWeight: FontWeight,
    unselectedWeight: FontWeight
) {
    val dims = MaterialTheme.pmDimensions

    val title: @Composable () -> Unit = {
        PMText(
            text = tab.title,
            style = PMTextStyle.Body,
            color = if (selected) selectedColor else unselectedColor,
            fontWeight = if (selected) selectedWeight else unselectedWeight
        )
    }

    val icon = tab.icon
    if (icon != null) {
        LeadingIconTab(
            selected = selected,
            enabled = tab.enabled,
            onClick = onClick,
            icon = { PMIcon(imageVector = icon, size = dims.sizing.iconMd) },
            text = title
        )
    } else {
        Tab(
            selected = selected,
            enabled = tab.enabled,
            onClick = onClick,
            text = title
        )
    }
}



enum class PMTabRowStyle {
    Primary,
    Secondary
}

data class PMTabItem(
    val title: String,
    val icon: ImageVector? = null,
    val enabled: Boolean = true
)

@Preview(name = "PMTabRow Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PMTabRowLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMTabRowPreviewContent()
    }
}

@Preview(name = "PMTabRow Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun PMTabRowDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMTabRowPreviewContent()
    }
}

@Composable
private fun PMTabRowPreviewContent() {
    var selected by remember {
        mutableIntStateOf(0)
    }

    val previewTabs = listOf(
        PMTabItem(
            title = "Arkadaşlar",
            icon = Icons.Filled.Person
        ),
        PMTabItem(
            title = "İstekler (3)",
            icon = Icons.Filled.PersonAdd
        ),
        PMTabItem(
            title = "Engellenenler",
            enabled = false
        )
    )

    PMTabRow(
        selectedTabIndex = selected,
        tabs = previewTabs,
        onTabSelected = {
            selected = it
        }
    )
}
