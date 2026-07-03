package com.mefy.platemate.presentation.common.bottombar

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.mefy.platemate.presentation.navigation.TopLevelDestination
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions


private const val IndicatorAnimMs = 350

@Composable
fun MainBottomBar(
    selectedDestination: TopLevelDestination,
    onDestinationSelected: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {

    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions
    val pillShape = RoundedCornerShape(dims.radius.rFull)

    val selectedState = remember { mutableStateOf(selectedDestination) }
    var selected by selectedState
    LaunchedEffect(selectedDestination) { selected = selectedDestination }

    val latestOnSelected by rememberUpdatedState(onDestinationSelected)
    val onSelect = remember {
        { destination: TopLevelDestination ->
            if (destination != selectedState.value) {
                selectedState.value = destination
                latestOnSelected(destination)
            }
        }
    }
    val clickHandlers = remember(onSelect) {
        TopLevelDestination.entries.associateWith { d -> { onSelect(d) } }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars),
        contentAlignment = Alignment.BottomCenter
    ) {
        // 1) Yüzen hap bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
                .height(dims.sizing.mainBottomBarContentHeight)
                .shadow(
                    elevation = dims.spacing.s16,
                    shape = pillShape,
                    spotColor = colors.cardShadow,
                    ambientColor = colors.cardShadow
                )
                .clip(pillShape)
                .background(colors.surface)
                .border(dims.stroke.st1, colors.outlineVariant, pillShape)
        ) {
            BoxWithConstraints(Modifier.fillMaxSize()) {
                val slotWidth = maxWidth / TopLevelDestination.entries.size
                val indicatorSize = dims.sizing.iconLg + dims.spacing.s10 * 2
                val targetX = slotWidth * selected.ordinal + (slotWidth - indicatorSize) / 2
                val indicatorX by animateDpAsState(
                    targetValue = targetX,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMediumLow,
                    ),
                    label = "indicatorX"
                )

                val circleAlpha by animateFloatAsState(
                    targetValue = if (selected == TopLevelDestination.Messages) 0f else 1f,
                    animationSpec = tween(durationMillis = IndicatorAnimMs, easing = FastOutSlowInEasing),
                    label = "indicatorAlpha"
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .offset { IntOffset(indicatorX.roundToPx(), 0) }
                        .size(indicatorSize)
                        .graphicsLayer { alpha = circleAlpha }
                        .clip(CircleShape)
                        .background(colors.primaryContainer)
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TopLevelDestination.entries.forEach { destination ->
                        if (destination == TopLevelDestination.Messages) {
                            Spacer(Modifier.weight(1f))
                        } else {
                            PMBottomBarItem(
                                destination = destination,
                                selected = destination == selected,
                                onClick = clickHandlers.getValue(destination)
                            )
                        }
                    }
                }
            }
        }

        CenterFab(
            selected = selected == TopLevelDestination.Messages,
            onClick = clickHandlers.getValue(TopLevelDestination.Messages),
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = -dims.spacing.s8)
        )
    }
}


@Preview(name = "MainBottomBar Light", showBackground = true)
@Composable
private fun MainBottomBarLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        MainBottomBar(
            selectedDestination = TopLevelDestination.Search,
            onDestinationSelected = {}
        )
    }
}

@Preview(name = "MainBottomBar Dark", showBackground = true)
@Composable
private fun MainBottomBarDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        MainBottomBar(
            selectedDestination = TopLevelDestination.Messages,
            onDestinationSelected = {}
        )
    }
}
