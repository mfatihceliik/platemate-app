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
import com.mefy.platemate.presentation.components.PMTextStyle
import com.mefy.platemate.presentation.navigation.TopLevelDestination
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun MainBottomBar(
    selectedDestination: TopLevelDestination,
    onDestinationSelected: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val dimensions = MaterialTheme.pmDimensions

    NavigationBar(
        modifier = modifier,
        containerColor = colorScheme.surface,
        tonalElevation = dimensions.spacing.s2
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
                    selectedIconColor = colorScheme.primary,
                    selectedTextColor = colorScheme.primary,
                    indicatorColor = colorScheme.primaryContainer,
                    unselectedIconColor = colorScheme.onSurfaceVariant,
                    unselectedTextColor = colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
