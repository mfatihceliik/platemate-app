package com.mefy.platemate.presentation.common.bottombar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.navigation.TopLevelDestination
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun RowScope.PMBottomBarItem(
    destination: TopLevelDestination,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = PMTheme.colors
    val label = stringResource(destination.labelRes)
    val interaction = remember { MutableInteractionSource() }

    val tint by animateColorAsState(
        targetValue = if (selected) colors.primary else colors.tabInactive,
        animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing),
        label = "tint"
    )

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .selectable(
                selected = selected,
                role = Role.Tab,
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            )
            .align(Alignment.CenterVertically),
        contentAlignment = Alignment.Center
    ) {
        PMIcon(
            imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
            contentDescription = label,
            tint = tint,
        )
        // Etiket: alt overlay — ikonu ittirmez, dairenin hemen altına yerleşir.
        // align BottomCenter burada (BoxScope) hesaplanır; AnimatedVisibility'nin
        // scope'suz (standalone) overload'ı için ayrı composable'a taşındı.

        /*LabelOverlay(
            visible = selected,
            label = label,
            color = colors.primary,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )*/
    }
}

@Preview(name = "BottomBarItem Light", showBackground = true)
@Composable
private fun PMBottomBarItemLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val colors = PMTheme.colors
        Row(Modifier.background(colors.surface)) {
            PMBottomBarItem(TopLevelDestination.Search, selected = true, onClick = {})
            PMBottomBarItem(TopLevelDestination.Discover, selected = false, onClick = {})
            PMBottomBarItem(TopLevelDestination.Profile, selected = false, onClick = {})
        }
    }
}

@Preview(name = "BottomBarItem Dark", showBackground = true)
@Composable
private fun PMBottomBarItemDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        val colors = PMTheme.colors
        Row(Modifier.background(colors.surface)) {
            PMBottomBarItem(TopLevelDestination.Search, selected = true, onClick = {})
            PMBottomBarItem(TopLevelDestination.Discover, selected = false, onClick = {})
            PMBottomBarItem(TopLevelDestination.Profile, selected = false, onClick = {})
        }
    }
}
