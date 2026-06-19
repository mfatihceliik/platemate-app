package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

/**
 * A reusable, list-friendly row element wrapped in a [PMCard].
 *
 * Anatomy (left → right):
 * ```
 * ┌─────────────────────────────────────────────┐
 * │ [icon]  Title                  [trailing] ›  │
 * │         Subtitle                             │
 * └─────────────────────────────────────────────┘
 * ```
 *
 * Designed to be the single consistent row used across the whole app. Render N
 * of them inside a [LazyColumn] (one per data item) for settings lists, menus,
 * saved-plate lists, etc.
 *
 * @param title primary label (single line, ellipsized).
 * @param subtitle optional secondary line below the title.
 * @param leadingIcon optional icon shown at the start.
 * @param leadingIconTint icon color; defaults to [PMColors.primary].
 * @param leadingContainerColor when set, the icon is drawn inside a rounded,
 *   tinted square container (settings-row style). When null the icon is plain.
 * @param trailingText optional value text shown before the chevron (e.g. "English").
 * @param showChevron force-show/hide the trailing chevron. When null, the chevron
 *   is shown automatically if [onClick] is provided.
 * @param enabled visual + interaction enabled state.
 * @param onClick optional debounced click handler for the whole row.
 * @param trailing optional fully-custom trailing slot (e.g. a [PMSwitch]).
 *   Takes precedence over [trailingText] / chevron.
 * @param showCard when true (default) the row is wrapped in its own [PMCard]
 *   (standalone use). Set false to render a flat row for grouping multiple rows
 *   inside a single shared [PMCard] / [PMRowGroup] (settings-list style).
 */
@Composable
fun PMRowItem(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    leadingIcon: ImageVector? = null,
    leadingIconTint: Color? = null,
    leadingContainerColor: Color? = null,
    trailingText: String? = null,
    showChevron: Boolean? = null,
    enabled: Boolean = true,
    showCard: Boolean = true,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    val dims = MaterialTheme.pmDimensions
    val resolvedChevron = showChevron ?: (onClick != null)

    val body: @Composable () -> Unit = {
        PMRowItemBody(
            title = title,
            subtitle = subtitle,
            leadingIcon = leadingIcon,
            leadingIconTint = leadingIconTint,
            leadingContainerColor = leadingContainerColor,
            trailingText = trailingText,
            resolvedChevron = resolvedChevron,
            enabled = enabled,
            trailing = trailing
        )
    }

    if (showCard) {
        PMCard(
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
            enabled = enabled,
            padding = PaddingValues(
                horizontal = dims.spacing.s16,
                vertical = dims.spacing.s12
            ),
            content = { body() }
        )
    } else {
        val clickModifier = if (onClick != null) {
            Modifier.debouncedClickable(enabled = enabled, onClick = onClick)
        } else {
            Modifier
        }
        Box(
            modifier = modifier
                .fillMaxWidth()
                .then(clickModifier)
                .padding(
                    horizontal = dims.spacing.s16,
                    vertical = dims.spacing.s12
                )
        ) {
            body()
        }
    }
}

@Composable
private fun PMRowItemBody(
    title: String,
    subtitle: String?,
    leadingIcon: ImageVector?,
    leadingIconTint: Color?,
    leadingContainerColor: Color?,
    trailingText: String?,
    resolvedChevron: Boolean,
    enabled: Boolean,
    trailing: (@Composable () -> Unit)?,
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
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                LeadingIcon(
                    icon = leadingIcon,
                    tint = leadingIconTint ?: colors.primary,
                    containerColor = leadingContainerColor
                )
                Spacer(modifier = Modifier.width(dims.spacing.s12))
            }

            Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)) {
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

        // ── Trailing ─────────────────────────────────────
        if (trailing != null) {
            Spacer(modifier = Modifier.width(dims.spacing.s12))
            trailing()
        } else if (trailingText != null || resolvedChevron) {
            Spacer(modifier = Modifier.width(dims.spacing.s12))
            TrailingDefault(
                text = trailingText,
                showChevron = resolvedChevron
            )
        }
    }
}

/**
 * Groups multiple flat [PMRowItem]s (use `showCard = false`) inside a single
 * [PMCard], inserting a thin divider between consecutive rows. Rows are flush
 * (no vertical gap) — the divider is the only visual separator.
 *
 * ```
 * PMRowGroup {
 *     PMRowItem(title = "Row 1", showCard = false, onClick = {})
 *     PMRowItem(title = "Row 2", showCard = false, onClick = {})
 * }
 * ```
 */
@Composable
fun PMRowGroup(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    PMCard(
        modifier = modifier.fillMaxWidth(),
        padding = PaddingValues(0.dp),
        content = content
    )
}

@Composable
private fun LeadingIcon(
    icon: ImageVector,
    tint: Color,
    containerColor: Color?,
) {
    val dims = MaterialTheme.pmDimensions

    if (containerColor != null) {
        Box(
            modifier = Modifier
                .size(dims.sizing.settingsRowIcon)
                .clip(RoundedCornerShape(dims.radius.r10))
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            PMIcon(
                imageVector = icon,
                tint = tint
            )
        }
    } else {
        PMIcon(
            imageVector = icon,
            size = dims.sizing.iconLg,
            tint = tint
        )
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

    val rows = listOf(
        PreviewRow("Account", "Manage your profile", Icons.Filled.Person, true),
        PreviewRow("Notifications", "Push and email alerts", Icons.Filled.Notifications, true),
        PreviewRow("Saved plates", null, Icons.Filled.Bookmark, true),
        PreviewRow("Privacy & security", "Password, sessions", Icons.Filled.Lock, true),
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        // Auto-rendered list — one row per data item.
        items(rows) { row ->
            PMRowItem(
                title = row.title,
                subtitle = row.subtitle,
                leadingIcon = row.icon,
                leadingContainerColor = if (row.container) colors.primaryContainer else null,
                onClick = {}
            )
        }

        // Variants showcase.
        item {
            PMRowItem(
                title = "Language",
                leadingIcon = Icons.Filled.Person,
                trailingText = "English",
                onClick = {}
            )
        }
        item {
            PMRowItem(
                title = "Push notifications",
                subtitle = "Receive updates instantly",
                trailing = { PMSwitch(checked = true, onCheckedChange = {}) }
            )
        }
        item {
            PMRowItem(
                title = "Plain row, no icon",
                onClick = {}
            )
        }
        item {
            PMRowItem(
                title = "Disabled row",
                subtitle = "Not interactive",
                leadingIcon = Icons.Filled.Lock,
                leadingContainerColor = colors.surfaceVariant,
                enabled = false,
                onClick = {}
            )
        }
    }
}
