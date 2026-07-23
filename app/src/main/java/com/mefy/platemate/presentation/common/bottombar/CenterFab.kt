package com.mefy.platemate.presentation.common.bottombar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.navigation.TopLevelDestination
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun CenterFab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors
    val label = stringResource(TopLevelDestination.Messages.labelRes)
    val interaction = remember { MutableInteractionSource() }

    val fill by animateColorAsState(
        targetValue = if (selected) colors.primaryContainer else colors.chipBg,
        animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing),
        label = "fabFill"
    )
    val tint by animateColorAsState(
        targetValue = if (selected) colors.primary else colors.tabInactive,
        animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing),
        label = "fabTint"
    )

    Box(
        modifier = modifier
            .size(spacing.s48)
            .clip(CircleShape)
            .background(fill)
            .selectable(
                selected = selected,
                role = Role.Tab,
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        PMIcon(
            imageVector = if (selected) TopLevelDestination.Messages.selectedIcon
            else TopLevelDestination.Messages.unselectedIcon,
            contentDescription = label,
            tint = tint,
        )
    }
}

@Preview(name = "CenterFab Selected Light", showBackground = true)
@Composable
private fun CenterFabSelectedPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val spacing = PMTheme.spacing
        Box(Modifier.padding(spacing.s24), contentAlignment = Alignment.Center) {
            CenterFab(selected = true, onClick = {})
        }
    }
}

@Preview(name = "CenterFab Unselected Dark", showBackground = true)
@Composable
private fun CenterFabUnselectedDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        val spacing = PMTheme.spacing
        Box(Modifier.padding(spacing.s24), contentAlignment = Alignment.Center) {
            CenterFab(selected = false, onClick = {})
        }
    }
}
