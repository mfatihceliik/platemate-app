package com.mefy.platemate.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PMTabRow(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    tabs: List<PMTabItem>,
    onTabSelected: (Int) -> Unit,
    selectedTabBackgroundColor: Color = PMTheme.colors.primary.copy(alpha = .4f),
    containerColor: Color = PMTheme.colors.surfaceVariant,
    contentColor: Color = PMTheme.colors.primary,
    unselectedContentColor: Color = PMTheme.colors.textSecondary,
    indicatorColor: Color = contentColor,
    indicatorHeight: Dp = PMTheme.sizing.tabRowIndicatorHeight,
    indicatorWidthFraction: Float = 0.35f,
    indicatorShape: Shape = PMTheme.shapes.extraSmall,
    selectedFontWeight: FontWeight = FontWeight.SemiBold,
    unselectedFontWeight: FontWeight = FontWeight.Normal,
    showDivider: Boolean = false
) {

    val radius = PMTheme.radius
    val shape = RoundedCornerShape(
        topStart = radius.r10,
        topEnd = radius.r10,
    )
    val density = LocalDensity.current
    val tabPositions = remember {
        mutableStateListOf<TabPositionInfo>()
    }
    val currentTab = tabPositions.getOrNull(selectedTabIndex)
    val indicatorWidth = (currentTab?.width ?: 0.dp) * indicatorWidthFraction
    val targetOffset = if (currentTab != null) {
        currentTab.left + (currentTab.width - indicatorWidth) / 2
    } else {
        0.dp
    }
    val animatedOffset by animateDpAsState(
        targetValue = targetOffset,
        label = "PMTabIndicator"
    )

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(containerColor)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEachIndexed { index, tab ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .onGloballyPositioned { coordinates ->
                                val info = with(density) {
                                    TabPositionInfo(
                                        left = coordinates.positionInParent().x.toDp(),
                                        width = coordinates.size.width.toDp()
                                    )
                                }
                                if (index < tabPositions.size) {
                                    if (tabPositions[index] != info) tabPositions[index] = info
                                } else {
                                    tabPositions.add(info)
                                }
                            }
                            .background(
                                if (selectedTabIndex == index) selectedTabBackgroundColor else Color.Transparent
                            )
                    ) {
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
            }
            if (currentTab != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = animatedOffset)
                        .width(indicatorWidth)
                        .height(indicatorHeight)
                        .clip(indicatorShape)
                        .background(indicatorColor)
                )
            }
        }
        if (showDivider) {
            HorizontalDivider()
        }

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
    val sizing = PMTheme.sizing
    val title: @Composable () -> Unit = {
        PMText(
            text = tab.title,
            style = PMTextStyle.Body,
            color = if (selected) selectedColor else unselectedColor,
            fontWeight = if (selected) selectedWeight else unselectedWeight,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    val icon = tab.icon
    if (icon != null) {
        LeadingIconTab(
            selected = selected,
            enabled = tab.enabled,
            onClick = onClick,
            icon = {
                PMIcon(
                    imageVector = icon,
                    size = sizing.iconMd,
                    tint = if (selected) selectedColor else unselectedColor
                )
            },
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


private data class TabPositionInfo(
    val left: Dp,
    val width: Dp
)

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
