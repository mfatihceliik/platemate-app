package com.mefy.platemate.presentation.features.main

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.navigation.TopLevelDestination
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun MainBottomBar(
    selectedDestination: TopLevelDestination,
    onDestinationSelected: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val colors = MaterialTheme.pmColors
    val dimensions = MaterialTheme.pmDimensions

    NavigationBar(
        modifier = modifier,
        containerColor = colorScheme.surface,
        tonalElevation = dimensions.spacing.s4
    ) {
        TopLevelDestination.entries.forEach { destination ->
            val isSelected = destination == selectedDestination
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        onDestinationSelected(destination)
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) {
                            destination.selectedIcon
                        } else {
                            destination.unselectedIcon
                        },
                        contentDescription = stringResource(destination.labelRes)
                    )
                },
                label = {
                    PMText(
                        text = stringResource(destination.labelRes),
                        style = PMTextStyle.Caption
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colors.tabActive,
                    selectedTextColor = colors.tabActive,
                    indicatorColor = colorScheme.surface,
                    unselectedIconColor = colors.tabInactive,
                    unselectedTextColor = colors.tabInactive
                )
            )
        }
    }
}
