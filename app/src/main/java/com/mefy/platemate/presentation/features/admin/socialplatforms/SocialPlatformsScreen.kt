package com.mefy.platemate.presentation.features.admin.socialplatforms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.socialplatforms.components.SocialPlatformRow
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun SocialPlatformsScreen(
    state: SocialPlatformsUiState,
    onAction: (SocialPlatformsUiAction) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    if (state.items.isEmpty()) {
        Box(modifier.fillMaxSize().padding(contentPadding), contentAlignment = Alignment.Center) {
            PMText(text = stringResource(R.string.admin_social_platforms_empty), style = PMTextStyle.Body, color = colors.textLabel)
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(contentPadding),
            contentPadding = PaddingValues(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            items(items = state.items, key = { it.id }) { item ->
                SocialPlatformRow(
                    item = item,
                    onClick = { onAction(SocialPlatformsUiAction.EditClicked(item.id)) },
                    onToggle = { onAction(SocialPlatformsUiAction.ActiveToggled(item.id, item.active)) }
                )
            }
        }
    }
}

private val socialPlatformsPreviewState = SocialPlatformsUiState(
    isLoading = false,
    items = listOf(
        SocialPlatformListItem(id = 1L, code = "INSTAGRAM", label = "Instagram", sortOrder = 1, active = true),
        SocialPlatformListItem(id = 2L, code = "SNAPCHAT", label = "Snapchat", sortOrder = 6, active = false)
    )
)

@Preview(name = "SocialPlatforms Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun SocialPlatformsScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        SocialPlatformsScreen(state = socialPlatformsPreviewState, onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}

@Preview(name = "SocialPlatforms Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun SocialPlatformsScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        SocialPlatformsScreen(state = socialPlatformsPreviewState, onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}

@Preview(name = "SocialPlatforms Empty", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun SocialPlatformsScreenEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        SocialPlatformsScreen(state = SocialPlatformsUiState(isLoading = false), onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}
