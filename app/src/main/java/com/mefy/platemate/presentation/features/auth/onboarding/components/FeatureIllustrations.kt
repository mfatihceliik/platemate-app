package com.mefy.platemate.presentation.features.auth.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PMTheme

@Composable
internal fun FeatureIllustration1() {
    val colors = PMTheme.colors
    val radius = PMTheme.radius
    val stroke = PMTheme.stroke
    val spacing = PMTheme.spacing
    val shape = PMTheme.shapes.medium
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(
                Brush.linearGradient(listOf(colors.categoryTealBg, colors.surface)),
                RoundedCornerShape(radius.r24)
            )
            .border(stroke.st2, colors.primary.copy(alpha = 0.15f), RoundedCornerShape(radius.r24))
            .clip(RoundedCornerShape(radius.r24))
            .padding(vertical = spacing.s32, horizontal = spacing.s24),
        contentAlignment = Alignment.Center
    ) {
        // Bg circle
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = spacing.s48, y = spacing.s48)
                .size(180.dp)
                .background(colors.primary.copy(alpha = 0.07f), CircleShape)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.s24)
        ) {
            // Large plate card
            Box(
                modifier = Modifier
                    .size(width = 220.dp, height = 86.dp)
                    .shadow(spacing.s32, spotColor = colors.primary.copy(alpha = 0.25f))
                    .background(colors.surface, RoundedCornerShape(radius.r16))
                    .border(stroke.st2, colors.cardBorder, shape)
                    .clip(RoundedCornerShape(radius.r16)),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.width(spacing.s12).fillMaxSize().background(colors.primary).align(Alignment.CenterStart))
                PMText(text = stringResource(R.string.illustration_plate_1), style = PMTextStyle.Display, color = colors.onPrimaryContainer, modifier = Modifier.padding(start = spacing.s12))
            }

            // Stars
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.s8)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(spacing.s8)) {
                    repeat(4) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = colors.iconStar, modifier = Modifier.size(spacing.s32))
                    }
                    Icon(Icons.Filled.Star, contentDescription = null, tint = colors.starEmpty, modifier = Modifier.size(spacing.s32))
                }
                
                // Chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing.s8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.height(28.dp).background(colors.categoryTealBg, CircleShape).padding(horizontal = spacing.s12), contentAlignment = Alignment.Center) {
                        PMText(text = stringResource(R.string.illustration_careful_driver), style = PMTextStyle.Caption, color = colors.categoryTealFg)
                    }
                    Box(modifier = Modifier.height(28.dp).background(colors.categoryOrangeBg, CircleShape).padding(horizontal = spacing.s12), contentAlignment = Alignment.Center) {
                        PMText(text = stringResource(R.string.illustration_fast), style = PMTextStyle.Caption, color = colors.categoryOrangeFg)
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.height(28.dp).background(colors.categoryGreenBg, CircleShape).padding(horizontal = spacing.s12), contentAlignment = Alignment.Center) {
                        PMText(text = stringResource(R.string.illustration_no_lane_change), style = PMTextStyle.Caption, color = colors.categoryGreenFg)
                    }
                }
            }
        }
    }
}

@Composable
internal fun FeatureIllustration2() {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing
    val radius = PMTheme.radius
    val shape = PMTheme.shapes.medium
    val stroke = PMTheme.stroke

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(
                Brush.linearGradient(listOf(colors.categoryOrangeBg, colors.surface)),
                RoundedCornerShape(radius.r24)
            )
            .border(stroke.st2, colors.warning.copy(alpha = 0.18f), shape)
            .clip(shape)
            .padding(vertical = spacing.s24, horizontal = spacing.s24),
        contentAlignment = Alignment.TopCenter
    ) {
        // Bg circle
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-30).dp, y = (-30).dp)
                .size(140.dp)
                .background(colors.warning.copy(alpha = 0.07f), CircleShape)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.s16),
            modifier = Modifier.fillMaxSize()
        ) {
            // Trend badge
            Row(
                modifier = Modifier
                    .height(spacing.s32)
                    .background(colors.categoryOrangeBg, CircleShape)
                    .border(stroke.st1, colors.warning.copy(alpha = 0.25f), CircleShape)
                    .padding(horizontal = spacing.s16),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.s8)
            ) {
                Icon(Icons.Filled.Star, contentDescription = null, tint = colors.warning, modifier = Modifier.size(sizing.categoryIconDot))
                PMText(text = stringResource(R.string.illustration_trend_this_week), style = PMTextStyle.Caption, color = colors.categoryOrangeFg)
            }

            // Top 3 List
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacing.s8)
            ) {
                TrendItem(rank = "1", rankBg = colors.categoryOrangeBg, rankColor = colors.warning, city = "34", plate = stringResource(R.string.illustration_plate_1), stats = stringResource(R.string.illustration_stats_1), trend = "+12%", trendBg = colors.success)
                TrendItem(rank = "2", rankBg = colors.chipBg, rankColor = colors.textTertiary, city = "06", plate = stringResource(R.string.illustration_plate_2), stats = stringResource(R.string.illustration_stats_2), trend = "+8%", trendBg = colors.success)
                TrendItem(rank = "3", rankBg = colors.errorContainer, rankColor = colors.error, city = "35", plate = stringResource(R.string.illustration_plate_3), stats = stringResource(R.string.illustration_stats_3), trend = "-3%", trendBg = colors.error)
            }
        }
    }
}

@Composable
internal fun TrendItem(rank: String, rankBg: Color, rankColor: Color, city: String, plate: String, stats: String, trend: String, trendBg: Color) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val radius = PMTheme.radius
    val sizing = PMTheme.sizing
    val shape = PMTheme.shapes.medium
    val stroke = PMTheme.stroke

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface, RoundedCornerShape(radius.r16))
            .padding(vertical = spacing.s8, horizontal = spacing.s12)
            .shadow(10.dp, spotColor = colors.cardShadow),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.s8)
    ) {
        Box(modifier = Modifier.size(30.dp).background(rankBg, RoundedCornerShape(radius.r8)), contentAlignment = Alignment.Center) {
            PMText(text = rank, style = PMTextStyle.Title, color = rankColor)
        }
        Box(
            modifier = Modifier
                .size(width = 56.dp, height = 40.dp)
                .background(colors.categoryTealBg, shape)
                .border(stroke.st1, colors.primaryContainerBorder, shape)
                .clip(shape),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.width(7.dp).fillMaxSize().background(colors.primary).align(Alignment.CenterStart))
            PMText(text = city, style = PMTextStyle.Label, color = colors.onPrimaryContainer, modifier = Modifier.padding(start = spacing.s8))
        }
        Column(modifier = Modifier.weight(1f)) {
            PMText(text = plate, style = PMTextStyle.Label, color = colors.textPrimary)
            PMText(text = stats, style = PMTextStyle.Caption, color = colors.textTertiary)
        }
        Box(modifier = Modifier.size(width = 38.dp, height = 20.dp).background(trendBg, shape), contentAlignment = Alignment.Center) {
            PMText(text = trend, style = PMTextStyle.Caption, color = colors.onPrimary)
        }
    }
}

@Composable
internal fun FeatureIllustration3() {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val radius = PMTheme.radius
    val sizing = PMTheme.sizing
    val shape = PMTheme.shapes.medium
    val stroke = PMTheme.stroke

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(
                Brush.linearGradient(listOf(colors.categoryGreenBg, colors.surface)),
                shape
            )
            .border(stroke.st2, colors.success.copy(alpha = 0.18f), RoundedCornerShape(radius.r24))
            .clip(RoundedCornerShape(radius.r24))
            .padding(vertical = spacing.s24, horizontal = spacing.s24),
        contentAlignment = Alignment.TopCenter
    ) {
        // Bg circle
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-30).dp, y = 30.dp)
                .size(140.dp)
                .background(colors.success.copy(alpha = 0.07f), CircleShape)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.s16),
            modifier = Modifier.fillMaxSize()
        ) {
            // Avatar stack
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(52.dp).background(colors.categoryTealBg, CircleShape).border(stroke.st3, colors.surface, CircleShape).shadow(8.dp, spotColor = colors.cardShadow), contentAlignment = Alignment.Center) {
                    PMText(text = "AY", style = PMTextStyle.Title, color = colors.onPrimaryContainer)
                }
                Box(modifier = Modifier.offset(x = (-14).dp).size(52.dp).background(colors.categoryOrangeBg, CircleShape).border(stroke.st3, colors.surface, CircleShape).shadow(8.dp, spotColor = colors.cardShadow), contentAlignment = Alignment.Center) {
                    PMText(text = "MK", style = PMTextStyle.Title, color = colors.categoryOrangeFg)
                }
                Box(modifier = Modifier.offset(x = (-28).dp).size(52.dp).background(colors.categoryIndigoBg, CircleShape).border(stroke.st3, colors.surface, CircleShape).shadow(8.dp, spotColor = colors.cardShadow), contentAlignment = Alignment.Center) {
                    PMText(text = "ZE", style = PMTextStyle.Title, color = colors.categoryIndigoFg)
                }
                Box(modifier = Modifier.offset(x = (-42).dp).size(52.dp).background(colors.chipBg, CircleShape).border(stroke.st3, colors.surface, CircleShape).shadow(8.dp, spotColor = colors.cardShadow), contentAlignment = Alignment.Center) {
                    PMText(text = "+8K", style = PMTextStyle.Label, color = colors.textTertiary)
                }
            }

            // Stats grid
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = spacing.s4), horizontalArrangement = Arrangement.spacedBy(spacing.s8)) {
                StatBox(value = stringResource(R.string.illustration_users_count), label = stringResource(R.string.illustration_users_label), color = colors.primary, modifier = Modifier.weight(1f))
                StatBox(value = stringResource(R.string.illustration_reviews_count), label = stringResource(R.string.illustration_reviews_label), color = colors.warning, modifier = Modifier.weight(1f))
                StatBox(value = stringResource(R.string.illustration_plates_count), label = stringResource(R.string.illustration_plates_label), color = colors.success, modifier = Modifier.weight(1f))
            }

            // Recent comment
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.surface, shape)
                    .padding(vertical = spacing.s12, horizontal = spacing.s16)
                    .shadow(8.dp, spotColor = colors.cardShadow),
                horizontalArrangement = Arrangement.spacedBy(spacing.s8)
            ) {
                Box(modifier = Modifier.size(34.dp).background(colors.categoryTealBg, CircleShape), contentAlignment = Alignment.Center) {
                    PMText(text = "AY", style = PMTextStyle.Caption, color = colors.onPrimaryContainer)
                }
                Column(verticalArrangement = Arrangement.spacedBy(spacing.s4)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(spacing.s8), verticalAlignment = Alignment.CenterVertically) {
                        PMText(text = stringResource(R.string.illustration_comment_name), style = PMTextStyle.Label, color = colors.textPrimary)
                        PMText(text = "★★★★★", style = PMTextStyle.Caption, color = colors.iconStar)
                    }
                    PMText(text = stringResource(R.string.illustration_comment_text), style = PMTextStyle.Caption, color = colors.textSecondary)
                }
            }
        }
    }
}
