package com.mefy.platemate.presentation.features.main.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMMessageItem
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun MessagesScreen(
    state: MessagesUiState,
    onAction: (MessagesUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.main_tab_messages)
        ),
        containerColor = colors.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.isLoading) {
                MessagesShimmerContent(modifier = Modifier.fillMaxSize())
            } else if (state.conversations.isEmpty()) {
                MessagesEmptyState(modifier = Modifier.fillMaxSize())
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = state.conversations,
                        key = { it.roomId }
                    ) { conversation ->
                        PMMessageItem(
                            initials = conversation.initials,
                            name = conversation.name,
                            preview = conversation.preview,
                            time = conversation.time,
                            isUnread = conversation.isUnread,
                            avatarBg = conversation.avatarBg,
                            avatarFg = conversation.avatarFg,
                            onClick = { onAction(MessagesUiAction.ConversationClicked(conversation.roomId)) }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(start = dims.spacing.s16 + dims.sizing.avatarMedium + dims.spacing.s12),
                            color = colors.outlineVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MessagesEmptyState(
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Column(
        modifier = modifier.padding(dims.spacing.s32),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(dims.sizing.plateBadgeMedium)
                .clip(CircleShape)
                .background(colors.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            PMIcon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = null,
                tint = colors.textLabel,
                modifier = Modifier.size(dims.sizing.iconXl)
            )
        }

        PMText(
            text = stringResource(R.string.messages_empty_title),
            fontSize = dims.fontSize.md,
            color = colors.textPrimary,
            modifier = Modifier.padding(top = dims.spacing.s12)
        )
        PMText(
            text = stringResource(R.string.messages_empty_subtitle),
            fontSize = dims.fontSize.md,
            color = colors.textTertiary,
            modifier = Modifier.padding(top = dims.spacing.s4)
        )
    }
}

@Composable
private fun MessagesShimmerContent(
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val colorScheme = MaterialTheme.pmColors

    val shimmerTheme = remember(colorScheme) {
        defaultShimmerTheme.copy(
            shaderColors = listOf(
                colors.skeleton.copy(alpha = 0.55f),
                colors.surface.copy(alpha = 0.95f),
                colors.skeletonSecondary.copy(alpha = 0.45f)
            ),
            shaderColorStops = listOf(0f, 0.5f, 1f)
        )
    }
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View, theme = shimmerTheme)

    Column(
        modifier = modifier.padding(horizontal = dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
    ) {
        repeat(6) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(dims.sizing.avatarMedium)
                        .shimmer(shimmer)
                        .background(colors.skeleton, CircleShape)
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(dims.spacing.s16)
                            .shimmer(shimmer)
                            .background(colors.skeleton, RoundedCornerShape(dims.radius.r8))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(dims.spacing.s16)
                            .shimmer(shimmer)
                            .background(colors.skeleton.copy(alpha = 0.5f), RoundedCornerShape(dims.radius.r8))
                    )
                }
                Box(
                    modifier = Modifier
                        .size(width = 35.dp, height = dims.spacing.s12)
                        .shimmer(shimmer)
                        .background(colors.skeleton, RoundedCornerShape(dims.radius.r8))
                )
            }
        }
    }
}

@Preview(name = "Messages Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun MessagesLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        MessagesScreen(
            state = MessagesUiState(
                isLoading = false,
                conversations = previewConversations()
            ),
            onAction = {}
        )
    }
}

@Preview(name = "Messages Empty", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun MessagesEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        MessagesScreen(
            state = MessagesUiState(isLoading = false),
            onAction = {}
        )
    }
}

@Preview(name = "Messages Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun MessagesDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        MessagesScreen(
            state = MessagesUiState(
                isLoading = false,
                conversations = previewConversations()
            ),
            onAction = {}
        )
    }
}

private fun previewConversations() = listOf(
    MessageConversationUiModel(
        roomId = 1,
        initials = "AY",
        name = "Ahmet Y.",
        preview = "Teşekkürler, çok yardımcı oldun!",
        time = "09:24",
        isUnread = true,
        avatarBg = Color(0xFFEEF2FF),
        avatarFg = Color(0xFF4F46E5)
    ),
    MessageConversationUiModel(
        roomId = 2,
        initials = "ZK",
        name = "Zeynep K.",
        preview = "Plakayı gördüm, gerçekten nazik biri",
        time = "Dün",
        isUnread = false,
        avatarBg = Color(0xFFECFEFF),
        avatarFg = Color(0xFF0891B2)
    ),
    MessageConversationUiModel(
        roomId = 3,
        initials = "MC",
        name = "Mehmet C.",
        preview = "Evet, o plakanın sahibiyim ben",
        time = "Pzt",
        isUnread = false,
        avatarBg = Color(0xFFF0FDF4),
        avatarFg = Color(0xFF15803D)
    )
)
