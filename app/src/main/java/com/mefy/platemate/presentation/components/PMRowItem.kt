package com.mefy.platemate.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

enum class PMRowPosition {
    Single,
    Top,
    Middle,
    Bottom
}
fun pmRowPositionOf(index: Int, count: Int): PMRowPosition = when {
    count <= 1 -> PMRowPosition.Single
    index == 0 -> PMRowPosition.Top
    index == count - 1 -> PMRowPosition.Bottom
    else -> PMRowPosition.Middle
}
@Composable
fun PMRowItem(
    modifier: Modifier = Modifier,
    title: String = "",
    subtitle: String? = null,
    leadingIcon: ImageVector? = null,
    leadingIconPainter: Painter? = null,
    leadingIconTint: Color? = null,
    leadingContainerColor: Color? = null,
    trailingText: String? = null,
    showChevron: Boolean? = null,
    enabled: Boolean = true,
    position: PMRowPosition = PMRowPosition.Single,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    content: (@Composable () -> Unit)? = null
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions
    val resolvedChevron = showChevron ?: (onClick != null)

    val r = dims.radius.r12
    val shape = when (position) {
        PMRowPosition.Single -> RoundedCornerShape(r)
        PMRowPosition.Top -> RoundedCornerShape(topStart = r, topEnd = r)
        PMRowPosition.Middle -> RoundedCornerShape(dims.spacing.s0)
        PMRowPosition.Bottom -> RoundedCornerShape(bottomStart = r, bottomEnd = r)
    }

    val clickModifier = if (onClick != null) {
        Modifier.debouncedClickable(enabled = enabled, onClick = onClick)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.surface)
            .border(BorderStroke(dims.stroke.st1, colors.outlineVariant), shape)
            .then(clickModifier)
            .padding(
                horizontal = dims.spacing.s16, vertical = dims.spacing.s12
            )
    ) {
        PMRowItemBody(
            title = title,
            subtitle = subtitle,
            leadingIcon = leadingIcon,
            leadingIconPainter = leadingIconPainter,
            leadingIconTint = leadingIconTint,
            leadingContainerColor = leadingContainerColor,
            trailingText = trailingText,
            resolvedChevron = resolvedChevron,
            enabled = enabled,
            trailing = trailing,
            content = content
        )
    }
}

@Composable
private fun PMRowItemBody(
    title: String,
    subtitle: String?,
    leadingIcon: ImageVector?,
    leadingIconPainter: Painter?,
    leadingIconTint: Color?,
    leadingContainerColor: Color?,
    trailingText: String?,
    resolvedChevron: Boolean,
    enabled: Boolean,
    trailing: (@Composable () -> Unit)?,
    content: (@Composable () -> Unit)?
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions
    val titleColor = if (enabled) colors.textPrimary else colors.disabled

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.6f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ── Leading + texts ──────────────────────────────
        Row(
            modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIconPainter != null) {
                PMIconContainer(
                    painter = leadingIconPainter,
                    tint = leadingIconTint ?: Color.Unspecified,
                    containerColor = leadingContainerColor ?: colors.primaryContainer
                )
                Spacer(modifier = Modifier.width(dims.spacing.s12))
            } else if (leadingIcon != null) {
                PMIconContainer(
                    imageVector = leadingIcon,
                    tint = leadingIconTint,
                    containerColor = leadingContainerColor ?: colors.primaryContainer
                )
                Spacer(modifier = Modifier.width(dims.spacing.s12))
            }

            if (content != null) {
                content()
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
                ) {
                    PMText(
                        text = title,
                        style = PMTextStyle.Body,
                        fontWeight = FontWeight.SemiBold,
                        color = titleColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (subtitle != null) {
                        PMText(
                            text = subtitle,
                            style = PMTextStyle.Caption,
                            color = colors.textTertiary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // ── Trailing ─────────────────────────────────────
        if (trailing != null) {
            Spacer(modifier = Modifier.width(dims.spacing.s12))
            trailing()
        } else if (trailingText != null || resolvedChevron) {
            Spacer(modifier = Modifier.width(dims.spacing.s12))
            TrailingDefault(
                text = trailingText, showChevron = resolvedChevron
            )
        }
    }
}

@Composable
private fun TrailingDefault(
    text: String?,
    showChevron: Boolean,
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s4)
    ) {
        if (text != null) {
            PMText(
                text = text,
                style = PMTextStyle.Body,
                color = colors.textTertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (showChevron) {
            PMIcon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                size = dims.sizing.iconLg,
                tint = colors.textLabel
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────

@Preview(name = "PMRowItem Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMRowItemLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMRowItemPreviewContent()
    }
}

@Preview(name = "PMRowItem Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMRowItemDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMRowItemPreviewContent()
    }
}

private data class PreviewRow(
    val title: String,
    val subtitle: String?,
    val icon: ImageVector,
    val container: Boolean,
)

@Composable
private fun PMRowItemPreviewContent() {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    val group = listOf(
        PreviewRow("Account", "Manage your profile", Icons.Filled.Person, true),
        PreviewRow("Notifications", "Push and email alerts", Icons.Filled.Notifications, true),
        PreviewRow("Saved plates", null, Icons.Filled.Bookmark, true),
        PreviewRow("Privacy & security", "Password, sessions", Icons.Filled.Lock, true),
    )

    // No inter-item spacing: grouped rows must be flush so the border-overlap reads as one
    // card. Gaps between the group and standalone rows are inserted explicitly.
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(dims.spacing.s16)
    ) {
        // Grouped card — one PMRowItem per data item, positioned by index.
        itemsIndexed(group) { index, row ->
            PMRowItem(
                title = row.title,
                subtitle = row.subtitle,
                leadingIcon = row.icon,
                leadingContainerColor = if (row.container) colors.primaryContainer else null,
                position = pmRowPositionOf(index, group.size),
                onClick = {})
        }

        item { Spacer(modifier = Modifier.height(dims.spacing.s16)) }

        // Standalone (Single) variants.
        item {
            PMRowItem(
                title = "Language",
                leadingIcon = Icons.Filled.Person,
                trailingText = "English",
                onClick = {})
        }
        item { Spacer(modifier = Modifier.height(dims.spacing.s12)) }
        item {
            PMRowItem(
                title = "Push notifications",
                subtitle = "Receive updates instantly",
                trailing = { PMSwitch(checked = true, onCheckedChange = {}) })
        }
        item { Spacer(modifier = Modifier.height(dims.spacing.s12)) }
        item {
            PMRowItem(
                title = "Disabled row",
                subtitle = "Not interactive",
                leadingIcon = Icons.Filled.Lock,
                leadingContainerColor = colors.surfaceVariant,
                enabled = false,
                onClick = {})
        }
    }
}
