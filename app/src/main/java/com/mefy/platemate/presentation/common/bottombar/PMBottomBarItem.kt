package com.mefy.platemate.presentation.common.bottombar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.navigation.TopLevelDestination
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

/**
 * Yüzen bardaki tek sekme: ikon üstte, etiket altta (yalnız seçilide).
 * Seçili ikon arkasında `primaryContainer` hap belirir; tıklamada gri ripple yok.
 */
@Composable
internal fun RowScope.PMBottomBarItem(
    destination: TopLevelDestination,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions
    val label = stringResource(destination.labelRes)
    val interaction = remember { MutableInteractionSource() }

    val pillColor by animateColorAsState(
        targetValue = if (selected) colors.primaryContainer else Color.Transparent,
        animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing),
        label = "pillColor"
    )
    val tint by animateColorAsState(
        targetValue = if (selected) colors.primary else colors.tabInactive,
        animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing),
        label = "tint"
    )

    Box(
        modifier = Modifier
            .weight(1f)
            .selectable(
                selected = selected,
                role = Role.Tab,
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(pillColor)
                    .padding(dims.spacing.s10),
                contentAlignment = Alignment.Center
            ) {
                PMIcon(
                    imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                    contentDescription = label,
                    tint = tint,
                    size = dims.sizing.iconLg
                )
            }
            AnimatedVisibility(
                visible = selected,
                enter = fadeIn(tween(320)) + expandVertically(tween(320, easing = FastOutSlowInEasing)),
                exit = fadeOut(tween(240)) + shrinkVertically(tween(240, easing = FastOutSlowInEasing))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(Modifier.height(dims.spacing.s4))
                    PMText(
                        text = label,
                        style = PMTextStyle.Note,
                        color = colors.primary
                    )
                }
            }
        }
    }
}

@Preview(name = "BottomBarItem Light", showBackground = true)
@Composable
private fun PMBottomBarItemLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        Row(Modifier.background(MaterialTheme.pmColors.surface)) {
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
        Row(Modifier.background(MaterialTheme.pmColors.surface)) {
            PMBottomBarItem(TopLevelDestination.Search, selected = true, onClick = {})
            PMBottomBarItem(TopLevelDestination.Discover, selected = false, onClick = {})
            PMBottomBarItem(TopLevelDestination.Profile, selected = false, onClick = {})
        }
    }
}
