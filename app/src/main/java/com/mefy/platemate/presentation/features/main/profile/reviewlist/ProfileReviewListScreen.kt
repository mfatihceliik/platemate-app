package com.mefy.platemate.presentation.features.main.profile.reviewlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.components.PMEmptyState
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMPlateBadge
import com.mefy.platemate.presentation.components.PMSearchBar
import com.mefy.platemate.presentation.components.PMTabItem
import com.mefy.platemate.presentation.components.PMTabRow
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

private const val LOAD_MORE_THRESHOLD = 4

@Composable
fun ProfileReviewListScreen(
    modifier: Modifier = Modifier,
    state: ProfileReviewListUiState,
    onAction: (ProfileReviewListUiAction) -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
    innerPadding: PaddingValues = PaddingValues()
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    val shouldLoadMore by remember(lazyListState) {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            layoutInfo.totalItemsCount > 0 &&
                lastVisibleIndex >= layoutInfo.totalItemsCount - LOAD_MORE_THRESHOLD
        }
    }

    LaunchedEffect(shouldLoadMore, state.endReached, state.isLoadingMore, state.isLoading) {
        if (shouldLoadMore && !state.endReached && !state.isLoadingMore && !state.isLoading) {
            onAction(ProfileReviewListUiAction.LoadMoreRequested)
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .fillMaxWidth()
            .background(colors.background),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        item(contentType = "tabs") {
            PMTabRow(
                selectedTabIndex = state.selectedTab,
                tabs = listOf(
                    PMTabItem(
                        title = stringResource(R.string.profile_status_approved),
                        icon = Icons.Filled.CheckCircle
                    ),
                    PMTabItem(
                        title = stringResource(R.string.profile_status_pending_review),
                        icon = Icons.Filled.HourglassEmpty
                    ),
                    PMTabItem(
                        title = stringResource(R.string.profile_status_rejected),
                        icon = Icons.Filled.Cancel
                    )
                ),
                onTabSelected = { onAction(ProfileReviewListUiAction.TabChanged(it)) }
            )
        }

        item(contentType = "search") {
            PMSearchBar(
                query = state.searchQuery,
                onQueryChange = { onAction(ProfileReviewListUiAction.SearchQueryChanged(it)) },
                placeholder = stringResource(R.string.profile_review_list_search_hint),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dims.spacing.s16)
            )
        }

        if (state.isLoading) {
            item(contentType = "loading") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dims.spacing.s32),
                    contentAlignment = Alignment.Center
                ) {
                    PMCircularProgressIndicator()
                }
            }
        } else if (state.isEmpty) {
            item(contentType = "empty") {
                PMEmptyState(
                    icon = Icons.Outlined.SearchOff,
                    message = stringResource(R.string.profile_review_list_empty),
                    modifier = Modifier.padding(horizontal = dims.spacing.s16)
                )
            }
        } else {
            items(
                items = state.reviews,
                key = { it.id },
                contentType = { "review_item" }
            ) { review ->
                ProfileReviewListItemCard(
                    review = review,
                    onClick = { onAction(ProfileReviewListUiAction.ReviewClicked(review.plateCode, review.id)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dims.spacing.s16)
                )
            }

            if (state.isLoadingMore) {
                item(contentType = "loading_more") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = dims.spacing.s8),
                        contentAlignment = Alignment.Center
                    ) {
                        PMCircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileReviewListItemCard(
    review: ProfileReviewListItemUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMCard(
        modifier = modifier,
        onClick = onClick,
        padding = PaddingValues(dims.spacing.s12)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s10),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PMPlateBadge(plate = review.plateCode, size = dims.sizing.plateBadgeSm)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s4),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PMIcon(
                        imageVector = Icons.Filled.Star,
                        tint = colors.iconStar,
                        size = dims.sizing.iconSm
                    )
                    PMText(
                        text = review.rating.toString(),
                        style = PMTextStyle.Body,
                        color = colors.textPrimary
                    )
                }
            }
            if (review.comment.isNotBlank()) {
                PMText(
                    text = review.comment,
                    style = PMTextStyle.Body,
                    color = colors.textPrimary,
                    maxLines = 2
                )
            }
            if (review.dateText.isNotBlank()) {
                PMText(
                    text = review.dateText,
                    style = PMTextStyle.Note,
                    color = colors.textTertiary
                )
            }
        }
    }
}

@Preview(name = "ProfileReviewList Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ProfileReviewListScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ProfileReviewListScreen(state = previewState(), onAction = {})
    }
}

@Preview(name = "ProfileReviewList Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ProfileReviewListScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ProfileReviewListScreen(state = previewState(), onAction = {})
    }
}

private fun previewState(): ProfileReviewListUiState = ProfileReviewListUiState(
    isInitialLoading = false,
    reviews = listOf(
        ProfileReviewListItemUiModel(
            id = 1L,
            plateCode = "34 EK 0682",
            comment = "Çok nazik bir sürücü, teşekkür etti.",
            rating = 5,
            dateText = "2024-12-01"
        ),
        ProfileReviewListItemUiModel(
            id = 2L,
            plateCode = "06 ABC 123",
            comment = "Yol verdi, saygılı davrandı.",
            rating = 4,
            dateText = "2024-11-30"
        )
    )
)
