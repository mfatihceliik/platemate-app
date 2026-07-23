package com.mefy.platemate.presentation.features.auth.onboarding.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.mefy.platemate.presentation.components.PMButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.variant.PMButtonVariant
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun FeaturePagerContent(
    onSkipClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val shape = PMTheme.shapes.medium

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.surfaceSecondary)
    ) {
        // Skip Button Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = spacing.s64, end = spacing.s24),
            contentAlignment = Alignment.CenterEnd
        ) {
            PMText(
                text = stringResource(R.string.onboarding_skip),
                style = PMTextStyle.Body,
                color = colors.textLabel,
                modifier = Modifier.clickable(onClick = onSkipClick)
            )
        }

        // Horizontal Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Illustration Area
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(top = spacing.s16, start = spacing.s24, end = spacing.s24),
                    contentAlignment = Alignment.Center
                ) {
                    when (page) {
                        0 -> FeatureIllustration1()
                        1 -> FeatureIllustration2()
                        2 -> FeatureIllustration3()
                    }
                }

                // Text and Nav Area
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = spacing.s24, bottom = spacing.s32, start = spacing.s24, end = spacing.s24),
                    verticalArrangement = Arrangement.spacedBy(spacing.s16)
                ) {
                    val titleRes = when (page) {
                        0 -> R.string.onboarding_f1_title
                        1 -> R.string.onboarding_f2_title
                        else -> R.string.onboarding_f3_title
                    }
                    val descRes = when (page) {
                        0 -> R.string.onboarding_f1_desc
                        1 -> R.string.onboarding_f2_desc
                        else -> R.string.onboarding_f3_desc
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(spacing.s8)) {
                        PMText(
                            text = stringResource(titleRes),
                            style = PMTextStyle.Display,
                            color = colors.textPrimary
                        )
                        PMText(
                            text = stringResource(descRes),
                            style = PMTextStyle.Body,
                            color = colors.textTertiary
                        )
                    }

                    // Progress Dots
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = spacing.s4),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(3) { index ->
                            val isSelected = pagerState.currentPage == index
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = spacing.s4)
                                    .size(width = if (isSelected) 24.dp else 7.dp, height = 7.dp)
                                    .background(
                                        color = if (isSelected) colors.primary else colors.starEmpty,
                                        shape = shape
                                    )
                            )
                        }
                    }

                    // Bottom Nav
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(spacing.s12),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (page > 0) {
                            Box(
                                modifier = Modifier
                                    .size(spacing.s48)
                                    .background(colors.chipBg, shape)
                                    .clickable {
                                        scope.launch {
                                            pagerState.animateScrollToPage(page - 1)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                PMIcon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    tint = colors.textSecondary
                                )
                            }
                        }

                        PMButton(
                            text = stringResource(if (page == 2) R.string.auth_register_title else R.string.onboarding_next),
                            onClick = {
                                if (page < 2) {
                                    scope.launch {
                                        pagerState.animateScrollToPage(page + 1)
                                    }
                                } else {
                                    onRegisterClick()
                                }
                            },
                            modifier = Modifier.weight(1f),
                            variant = PMButtonVariant.Filled
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "FeaturePagerContent Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun FeaturePagerContentLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        FeaturePagerContent(onSkipClick = {}, onRegisterClick = {})
    }
}

@Preview(name = "FeaturePagerContent Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun FeaturePagerContentDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        FeaturePagerContent(onSkipClick = {}, onRegisterClick = {})
    }
}

