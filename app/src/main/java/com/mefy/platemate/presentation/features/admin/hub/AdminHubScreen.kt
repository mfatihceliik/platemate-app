package com.mefy.platemate.presentation.features.admin.hub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMRowItem
import com.mefy.platemate.presentation.components.PMSearchBar
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.pmRowPositionOf
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun AdminHubScreen(
    state: AdminHubUiState,
    onAction: (AdminHubUiAction) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val filtered = state.filteredItems

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        PMSearchBar(
            query = state.query,
            onQueryChange = { onAction(AdminHubUiAction.QueryChanged(it)) },
            placeholder = stringResource(R.string.admin_menu_search_hint),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dims.spacing.s12)
        )

        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = dims.spacing.s48),
                contentAlignment = Alignment.Center
            ) {
                PMText(
                    text = stringResource(R.string.admin_menu_empty),
                    style = PMTextStyle.Body,
                    color = colors.textLabel
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = dims.spacing.s24)
            ) {
                itemsIndexed(
                    items = filtered,
                    key = { _, item -> item.code }
                ) { index, item ->
                    PMRowItem(
                        title = item.title,
                        leadingIcon = iconFor(item.iconKey),
                        leadingIconTint = colors.primary,
                        leadingContainerColor = colors.primaryContainer,
                        position = pmRowPositionOf(index, filtered.size),
                        trailing = item.badgeCount
                            ?.takeIf { it > 0 }
                            ?.let { count -> { MenuBadge(count = count) } },
                        onClick = { onAction(AdminHubUiAction.ItemClicked(item.code)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MenuBadge(count: Long, modifier: Modifier = Modifier) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Box(
        modifier = modifier
            .sizeIn(minWidth = dims.spacing.s24, minHeight = dims.spacing.s24)
            .background(colors.errorContainer, CircleShape)
            .padding(horizontal = dims.spacing.s8),
        contentAlignment = Alignment.Center
    ) {
        PMText(
            text = count.toString(),
            style = PMTextStyle.Caption,
            fontWeight = FontWeight.Bold,
            color = colors.error
        )
    }
}

private fun iconFor(iconKey: String): ImageVector = when (iconKey) {
    "comment" -> Icons.AutoMirrored.Filled.Comment
    "flag" -> Icons.Filled.Flag
    "rate_review" -> Icons.Filled.RateReview
    "visibility_off" -> Icons.Filled.VisibilityOff
    "label" -> Icons.AutoMirrored.Filled.Label
    "report" -> Icons.Filled.Flag
    "share" -> Icons.Filled.Share
    "workspace_premium" -> Icons.Filled.WorkspacePremium
    "star" -> Icons.Filled.Star
    "palette" -> Icons.Filled.Palette
    "tune" -> Icons.Filled.Tune
    else -> Icons.Filled.Tune
}

private val adminHubPreviewState = AdminHubUiState(
    isLoading = false,
    items = listOf(
        AdminMenuItemUiModel(code = "PENDING_COMMENTS", title = "Bekleyen Yorumlar", iconKey = "comment", badgeCount = 12L),
        AdminMenuItemUiModel(code = "COMMENT_REPORTS", title = "Yorum Şikayetleri", iconKey = "flag", badgeCount = 3L),
        AdminMenuItemUiModel(code = "HIDDEN_PLATES", title = "Gizlenen Plakalar", iconKey = "visibility_off", badgeCount = 0L),
        AdminMenuItemUiModel(code = "PLATE_REPORT_TYPES", title = "İhbar Tipleri", iconKey = "label", badgeCount = null),
        AdminMenuItemUiModel(code = "APP_SETTINGS", title = "Uygulama Ayarları", iconKey = "tune", badgeCount = null)
    )
)

@Preview(name = "AdminHub Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun AdminHubScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        AdminHubScreen(state = adminHubPreviewState, onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}

@Preview(name = "AdminHub Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun AdminHubScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        AdminHubScreen(state = adminHubPreviewState, onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}

@Preview(name = "AdminHub Empty Search", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun AdminHubScreenEmptySearchPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        AdminHubScreen(
            state = adminHubPreviewState.copy(query = "bulunamayacak"),
            onAction = {},
            contentPadding = PaddingValues(0.dp)
        )
    }
}
