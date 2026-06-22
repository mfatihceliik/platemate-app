package com.mefy.platemate.presentation.features.main.settings.premium

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMLoading
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

private val AmberLight = Color(0xFFFEF3C7)
private val LightCyan = Color(0xFFA5F3FC)

@Composable
fun PremiumInfoScreen(
    state: PremiumInfoUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    val status = when {
        state.isLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage)
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_premium_title),
            onBackClick = onBackClick
        ),
        containerColor = colors.surface,
        status = status,
        onRetry = onRetry,
        loading = { innerPadding -> PMLoading(modifier = Modifier.padding(innerPadding)) },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .background(colors.star, MaterialTheme.shapes.medium)
                        .debouncedClickable { /* TODO: Purchase flow */ },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(dims.sizing.iconMd)
                    )
                    PMText(
                        text = stringResource(R.string.profile_premium_upgrade),
                        style = PMTextStyle.Body,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(start = dims.spacing.s8)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
        ) {
            // Hero section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s10)
            ) {
                Box(
                    modifier = Modifier
                        .size(dims.sizing.avatarHero)
                        .clip(RoundedCornerShape(dims.radius.r18))
                        .background(AmberLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = colors.star,
                        modifier = Modifier.size(dims.sizing.avatarIconInner)
                    )
                }
                PMText(
                    text = stringResource(R.string.profile_premium_headline),
                    style = PMTextStyle.Headline,
                    fontWeight = FontWeight.ExtraBold,
                    color = colors.textPrimary
                )
                PMText(
                    text = stringResource(R.string.profile_premium_subtitle),
                    style = PMTextStyle.Note,
                    color = colors.textSecondary,
                    textAlign = TextAlign.Center
                )
            }

            // Feature list
            Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s10)) {
                val features = listOf(
                    R.string.profile_premium_feature_1,
                    R.string.profile_premium_feature_2,
                    R.string.profile_premium_feature_3,
                    R.string.profile_premium_feature_4,
                    R.string.profile_premium_feature_5,
                    R.string.profile_premium_feature_6
                )
                features.forEach { featureRes ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s10)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(dims.sizing.iconXl)
                                .clip(CircleShape)
                                .background(AmberLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = colors.star,
                                modifier = Modifier.size(dims.sizing.iconLg)
                            )
                        }
                        PMText(
                            text = stringResource(featureRes),
                            style = PMTextStyle.Body,
                            fontWeight = FontWeight.Medium,
                            color = colors.textSecondary
                        )
                    }
                }
            }

            // Pricing cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s10)
            ) {
                // Monthly card
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(colors.surface, MaterialTheme.shapes.large)
                        .border(dims.stroke.st1, colors.cardBorder, MaterialTheme.shapes.large)
                        .padding(dims.spacing.s12),
                    verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
                ) {
                    PMText(
                        text = stringResource(R.string.profile_premium_monthly),
                        style = PMTextStyle.Note,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.textSecondary
                    )
                    PMText(
                        text = stringResource(R.string.profile_premium_monthly_price),
                        style = PMTextStyle.Headline,
                        fontWeight = FontWeight.ExtraBold,
                        color = colors.textPrimary
                    )
                }

                // Yearly card (highlighted)
                Box(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colors.primaryDark, MaterialTheme.shapes.large)
                            .padding(dims.spacing.s12),
                        verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
                    ) {
                        PMText(
                            text = stringResource(R.string.profile_premium_yearly),
                            style = PMTextStyle.Note,
                            fontWeight = FontWeight.SemiBold,
                            color = LightCyan
                        )
                        PMText(
                            text = stringResource(R.string.profile_premium_yearly_price),
                            style = PMTextStyle.Headline,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                    PMText(
                        text = stringResource(R.string.profile_premium_discount),
                        style = PMTextStyle.Note,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = dims.spacing.s12)
                            .clip(MaterialTheme.shapes.extraLarge)
                            .background(colors.star)
                            .padding(horizontal = dims.spacing.s8, vertical = dims.spacing.s4)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PremiumPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PremiumInfoScreen(
            state = PremiumInfoUiState(isLoading = false),
            onBackClick = {},
            onRetry = {}
        )
    }
}
