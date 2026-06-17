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
import androidx.compose.material3.MaterialTheme
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
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun FeatureIllustration1() {
    val dimensions = MaterialTheme.pmDimensions
    val pmColors = MaterialTheme.pmColors
    val colorScheme = MaterialTheme.colorScheme
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(
                Brush.linearGradient(listOf(pmColors.categoryTealBg, colorScheme.surface)),
                RoundedCornerShape(dimensions.radius.r24)
            )
            .border(dimensions.stroke.st2, colorScheme.primary.copy(alpha = 0.15f), RoundedCornerShape(dimensions.radius.r24))
            .clip(RoundedCornerShape(dimensions.radius.r24))
            .padding(vertical = dimensions.spacing.s32, horizontal = dimensions.spacing.s24),
        contentAlignment = Alignment.Center
    ) {
        // Bg circle
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = dimensions.spacing.s48, y = dimensions.spacing.s48)
                .size(180.dp)
                .background(colorScheme.primary.copy(alpha = 0.07f), CircleShape)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.spacing.s24)
        ) {
            // Large plate card
            Box(
                modifier = Modifier
                    .size(width = 220.dp, height = 86.dp)
                    .shadow(dimensions.spacing.s32, spotColor = colorScheme.primary.copy(alpha = 0.25f))
                    .background(colorScheme.surface, RoundedCornerShape(dimensions.radius.r16))
                    .border(dimensions.stroke.st2, pmColors.cardBorder, RoundedCornerShape(dimensions.radius.r16))
                    .clip(RoundedCornerShape(dimensions.radius.r16)),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.width(dimensions.spacing.s12).fillMaxSize().background(colorScheme.primary).align(Alignment.CenterStart))
                PMText(text = stringResource(R.string.illustration_plate_1), style = PMTextStyle.Display, color = pmColors.primaryDark, modifier = Modifier.padding(start = dimensions.spacing.s12))
            }

            // Stars
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensions.spacing.s8)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.s8)) {
                    repeat(4) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = pmColors.star, modifier = Modifier.size(dimensions.spacing.s32))
                    }
                    Icon(Icons.Filled.Star, contentDescription = null, tint = pmColors.starEmpty, modifier = Modifier.size(dimensions.spacing.s32))
                }
                
                // Chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.s8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.height(28.dp).background(pmColors.categoryTealBg, CircleShape).padding(horizontal = dimensions.spacing.s12), contentAlignment = Alignment.Center) {
                        PMText(text = stringResource(R.string.illustration_careful_driver), style = PMTextStyle.Caption, color = pmColors.categoryTealFg)
                    }
                    Box(modifier = Modifier.height(28.dp).background(pmColors.categoryOrangeBg, CircleShape).padding(horizontal = dimensions.spacing.s12), contentAlignment = Alignment.Center) {
                        PMText(text = stringResource(R.string.illustration_fast), style = PMTextStyle.Caption, color = pmColors.categoryOrangeFg)
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.height(28.dp).background(pmColors.categoryGreenBg, CircleShape).padding(horizontal = dimensions.spacing.s12), contentAlignment = Alignment.Center) {
                        PMText(text = stringResource(R.string.illustration_no_lane_change), style = PMTextStyle.Caption, color = pmColors.categoryGreenFg)
                    }
                }
            }
        }
    }
}

@Composable
fun FeatureIllustration2() {
    val dimensions = MaterialTheme.pmDimensions
    val pmColors = MaterialTheme.pmColors
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(
                Brush.linearGradient(listOf(pmColors.categoryOrangeBg, colorScheme.surface)),
                RoundedCornerShape(dimensions.radius.r24)
            )
            .border(dimensions.stroke.st2, pmColors.warning.copy(alpha = 0.18f), RoundedCornerShape(dimensions.radius.r24))
            .clip(RoundedCornerShape(dimensions.radius.r24))
            .padding(vertical = dimensions.spacing.s24, horizontal = dimensions.spacing.s24),
        contentAlignment = Alignment.TopCenter
    ) {
        // Bg circle
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-30).dp, y = (-30).dp)
                .size(140.dp)
                .background(pmColors.warning.copy(alpha = 0.07f), CircleShape)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.spacing.s16),
            modifier = Modifier.fillMaxSize()
        ) {
            // Trend badge
            Row(
                modifier = Modifier
                    .height(dimensions.spacing.s32)
                    .background(pmColors.categoryOrangeBg, CircleShape)
                    .border(dimensions.stroke.st1, pmColors.warning.copy(alpha = 0.25f), CircleShape)
                    .padding(horizontal = dimensions.spacing.s16),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.s8)
            ) {
                Icon(Icons.Filled.Star, contentDescription = null, tint = pmColors.warning, modifier = Modifier.size(dimensions.sizing.categoryIconDot))
                PMText(text = stringResource(R.string.illustration_trend_this_week), style = PMTextStyle.Caption, color = pmColors.categoryOrangeFg)
            }

            // Top 3 List
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimensions.spacing.s8)
            ) {
                TrendItem(rank = "1", rankBg = pmColors.categoryOrangeBg, rankColor = pmColors.warning, city = "34", plate = stringResource(R.string.illustration_plate_1), stats = stringResource(R.string.illustration_stats_1), trend = "+12%", trendBg = pmColors.success)
                TrendItem(rank = "2", rankBg = pmColors.chipBg, rankColor = pmColors.textTertiary, city = "06", plate = stringResource(R.string.illustration_plate_2), stats = stringResource(R.string.illustration_stats_2), trend = "+8%", trendBg = pmColors.success)
                TrendItem(rank = "3", rankBg = pmColors.errorContainer, rankColor = colorScheme.error, city = "35", plate = stringResource(R.string.illustration_plate_3), stats = stringResource(R.string.illustration_stats_3), trend = "-3%", trendBg = colorScheme.error)
            }
        }
    }
}

@Composable
private fun TrendItem(rank: String, rankBg: Color, rankColor: Color, city: String, plate: String, stats: String, trend: String, trendBg: Color) {
    val dimensions = MaterialTheme.pmDimensions
    val pmColors = MaterialTheme.pmColors
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorScheme.surface, RoundedCornerShape(dimensions.radius.r16))
            .padding(vertical = dimensions.spacing.s8, horizontal = dimensions.spacing.s12)
            .shadow(10.dp, spotColor = pmColors.cardShadow),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.s8)
    ) {
        Box(modifier = Modifier.size(30.dp).background(rankBg, RoundedCornerShape(dimensions.radius.r8)), contentAlignment = Alignment.Center) {
            PMText(text = rank, style = PMTextStyle.Title, color = rankColor)
        }
        Box(
            modifier = Modifier
                .size(width = 56.dp, height = 40.dp)
                .background(pmColors.categoryTealBg, RoundedCornerShape(dimensions.radius.r12))
                .border(dimensions.stroke.st1, pmColors.primaryContainerBorder, RoundedCornerShape(dimensions.radius.r12))
                .clip(RoundedCornerShape(dimensions.radius.r12)),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.width(7.dp).fillMaxSize().background(colorScheme.primary).align(Alignment.CenterStart))
            PMText(text = city, style = PMTextStyle.Label, color = pmColors.primaryDark, modifier = Modifier.padding(start = dimensions.spacing.s8))
        }
        Column(modifier = Modifier.weight(1f)) {
            PMText(text = plate, style = PMTextStyle.Label, color = pmColors.textPrimary)
            PMText(text = stats, style = PMTextStyle.Caption, color = pmColors.textTertiary)
        }
        Box(modifier = Modifier.size(width = 38.dp, height = 20.dp).background(trendBg, RoundedCornerShape(dimensions.radius.r8)), contentAlignment = Alignment.Center) {
            PMText(text = trend, style = PMTextStyle.Caption, color = colorScheme.onPrimary)
        }
    }
}

@Composable
fun FeatureIllustration3() {
    val dimensions = MaterialTheme.pmDimensions
    val pmColors = MaterialTheme.pmColors
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(
                Brush.linearGradient(listOf(pmColors.categoryGreenBg, colorScheme.surface)),
                RoundedCornerShape(dimensions.radius.r24)
            )
            .border(dimensions.stroke.st2, pmColors.success.copy(alpha = 0.18f), RoundedCornerShape(dimensions.radius.r24))
            .clip(RoundedCornerShape(dimensions.radius.r24))
            .padding(vertical = dimensions.spacing.s24, horizontal = dimensions.spacing.s24),
        contentAlignment = Alignment.TopCenter
    ) {
        // Bg circle
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-30).dp, y = 30.dp)
                .size(140.dp)
                .background(pmColors.success.copy(alpha = 0.07f), CircleShape)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.spacing.s16),
            modifier = Modifier.fillMaxSize()
        ) {
            // Avatar stack
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(52.dp).background(pmColors.categoryTealBg, CircleShape).border(dimensions.stroke.st3, colorScheme.surface, CircleShape).shadow(8.dp, spotColor = pmColors.cardShadow), contentAlignment = Alignment.Center) {
                    PMText(text = "AY", style = PMTextStyle.Title, color = pmColors.primaryDark)
                }
                Box(modifier = Modifier.offset(x = (-14).dp).size(52.dp).background(pmColors.categoryOrangeBg, CircleShape).border(dimensions.stroke.st3, colorScheme.surface, CircleShape).shadow(8.dp, spotColor = pmColors.cardShadow), contentAlignment = Alignment.Center) {
                    PMText(text = "MK", style = PMTextStyle.Title, color = pmColors.categoryOrangeFg)
                }
                Box(modifier = Modifier.offset(x = (-28).dp).size(52.dp).background(pmColors.categoryIndigoBg, CircleShape).border(dimensions.stroke.st3, colorScheme.surface, CircleShape).shadow(8.dp, spotColor = pmColors.cardShadow), contentAlignment = Alignment.Center) {
                    PMText(text = "ZE", style = PMTextStyle.Title, color = pmColors.categoryIndigoFg)
                }
                Box(modifier = Modifier.offset(x = (-42).dp).size(52.dp).background(pmColors.chipBg, CircleShape).border(dimensions.stroke.st3, colorScheme.surface, CircleShape).shadow(8.dp, spotColor = pmColors.cardShadow), contentAlignment = Alignment.Center) {
                    PMText(text = "+8K", style = PMTextStyle.Label, color = pmColors.textTertiary)
                }
            }

            // Stats grid
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = dimensions.spacing.s4), horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.s8)) {
                StatBox(value = stringResource(R.string.illustration_users_count), label = stringResource(R.string.illustration_users_label), color = colorScheme.primary, modifier = Modifier.weight(1f))
                StatBox(value = stringResource(R.string.illustration_reviews_count), label = stringResource(R.string.illustration_reviews_label), color = pmColors.warning, modifier = Modifier.weight(1f))
                StatBox(value = stringResource(R.string.illustration_plates_count), label = stringResource(R.string.illustration_plates_label), color = pmColors.success, modifier = Modifier.weight(1f))
            }

            // Recent comment
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.surface, RoundedCornerShape(dimensions.radius.r16))
                    .padding(vertical = dimensions.spacing.s12, horizontal = dimensions.spacing.s16)
                    .shadow(8.dp, spotColor = pmColors.cardShadow),
                horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.s8)
            ) {
                Box(modifier = Modifier.size(34.dp).background(pmColors.categoryTealBg, CircleShape), contentAlignment = Alignment.Center) {
                    PMText(text = "AY", style = PMTextStyle.Caption, color = pmColors.primaryDark)
                }
                Column(verticalArrangement = Arrangement.spacedBy(dimensions.spacing.s4)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.s8), verticalAlignment = Alignment.CenterVertically) {
                        PMText(text = stringResource(R.string.illustration_comment_name), style = PMTextStyle.Label, color = pmColors.textPrimary)
                        PMText(text = "★★★★★", style = PMTextStyle.Caption, color = pmColors.star)
                    }
                    PMText(text = stringResource(R.string.illustration_comment_text), style = PMTextStyle.Caption, color = pmColors.textSecondary)
                }
            }
        }
    }
}
