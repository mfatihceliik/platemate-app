package com.mefy.platemate.presentation.features.admin.commentreasons

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
import com.mefy.platemate.presentation.features.admin.commentreasons.components.CommentReasonRow
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun CommentReasonsScreen(
    state: CommentReasonsUiState,
    onAction: (CommentReasonsUiAction) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    if (state.items.isEmpty()) {
        Box(modifier.fillMaxSize().padding(contentPadding), contentAlignment = Alignment.Center) {
            PMText(text = stringResource(R.string.admin_comment_reasons_empty), style = PMTextStyle.Body, color = colors.textLabel)
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(contentPadding),
            contentPadding = PaddingValues(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            items(items = state.items, key = { it.id }) { item ->
                CommentReasonRow(
                    item = item,
                    onClick = { onAction(CommentReasonsUiAction.EditClicked(item.id)) },
                    onToggle = { onAction(CommentReasonsUiAction.ActiveToggled(item.id, item.active)) }
                )
            }
        }
    }
}

private val commentReasonsPreviewState = CommentReasonsUiState(
    isLoading = false,
    items = listOf(
        CommentReasonListItem(id = 6L, code = "SPAM", label = "Spam", requiresDescription = false, active = true),
        CommentReasonListItem(id = 7L, code = "OTHER", label = "Diğer", requiresDescription = true, active = true),
        CommentReasonListItem(id = 8L, code = "FAKE_PLATE", label = "Sahte plaka", requiresDescription = false, active = false)
    )
)

@Preview(name = "CommentReasons Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun CommentReasonsScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        CommentReasonsScreen(state = commentReasonsPreviewState, onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}

@Preview(name = "CommentReasons Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun CommentReasonsScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        CommentReasonsScreen(state = commentReasonsPreviewState, onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}

@Preview(name = "CommentReasons Empty", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun CommentReasonsScreenEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        CommentReasonsScreen(state = CommentReasonsUiState(isLoading = false), onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}
