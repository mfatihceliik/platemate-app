package com.mefy.platemate.presentation.features.main.profile.settings.sociallinks

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTextField
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.main.profile.settings.sociallinks.components.PlatformRow
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun SocialLinksScreen(
    state: SocialLinksUiState,
    onAction: (SocialLinksUiAction) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_setting_social_links),
            onBackClick = onBackClick
        )
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s8),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
            ) {
                PMText(
                    text = stringResource(R.string.profile_social_description),
                    style = PMTextStyle.Note,
                    color = colors.textSecondary
                )

                // Connected accounts section
                Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
                    PMText(
                        text = stringResource(R.string.profile_social_connected_section),
                        style = PMTextStyle.SectionLabel,
                        modifier = Modifier.padding(start = dims.spacing.s4)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colors.surface, MaterialTheme.shapes.large)
                            .border(dims.stroke.st1, colors.cardBorder, MaterialTheme.shapes.large)
                    ) {
                        SocialLinksViewModel.supportedPlatforms.forEachIndexed { index, platform ->
                            val linkedItem = state.links.firstOrNull {
                                it.platform.equals(platform, ignoreCase = true)
                            }
                            val isConnected = linkedItem != null

                            PlatformRow(
                                platform = platform,
                                isConnected = isConnected,
                                displayValue = linkedItem?.url,
                                onConnectClick = {
                                    onAction(SocialLinksUiAction.NewPlatformSelected(platform))
                                },
                                onRemoveClick = {
                                    linkedItem?.let {
                                        onAction(SocialLinksUiAction.DeleteClicked(it.id))
                                    }
                                }
                            )

                            if (index < SocialLinksViewModel.supportedPlatforms.lastIndex) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = dims.spacing.s12)
                                        .height(dims.stroke.st1)
                                        .background(colors.outlineVariant)
                                )
                            }
                        }
                    }
                }

                // Add new link section
                Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
                    PMText(
                        text = stringResource(R.string.profile_social_add_section),
                        style = PMTextStyle.SectionLabel,
                        modifier = Modifier.padding(start = dims.spacing.s4)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PMTextField(
                            value = state.newUrl,
                            onValueChange = { onAction(SocialLinksUiAction.NewUrlChanged(it)) },
                            placeholder = "https://...",
                            modifier = Modifier.weight(1f)
                        )

                        Box(
                            modifier = Modifier
                                .height(48.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(colors.primary)
                                .debouncedClickable(
                                    enabled = state.newUrl.isNotBlank() && !state.isSubmitting
                                ) {
                                    onAction(SocialLinksUiAction.AddClicked)
                                }
                                .padding(horizontal = dims.spacing.s24),
                            contentAlignment = Alignment.Center
                        ) {
                            PMText(
                                text = stringResource(R.string.profile_social_add_button_short),
                                style = PMTextStyle.Body,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
