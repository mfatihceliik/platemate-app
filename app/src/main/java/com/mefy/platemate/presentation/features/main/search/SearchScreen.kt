package com.mefy.platemate.presentation.features.main.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.text.resolve
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTextStyle
import com.mefy.platemate.presentation.components.PlateCard
import com.mefy.platemate.presentation.features.main.search.model.SearchRecentUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateReportTagUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    state: SearchUiState,
    onAction: (SearchUiAction) -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    val colorScheme = MaterialTheme.colorScheme
    val spacing = MaterialTheme.pmDimensions.spacing
    val radius = MaterialTheme.pmDimensions.radius
    val stroke = MaterialTheme.pmDimensions.stroke

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .background(colorScheme.background),
        contentPadding = PaddingValues(horizontal = spacing.s16, vertical = spacing.s16),
        verticalArrangement = Arrangement.spacedBy(spacing.s16)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(spacing.s4)
                ) {
                    PMText(
                        text = stringResource(R.string.search_header_title),
                        style = PMTextStyle.Headline,
                        color = colorScheme.onBackground
                    )
                    PMText(
                        text = stringResource(R.string.search_header_subtitle),
                        style = PMTextStyle.Body,
                        color = colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(radius.r10))
                        .background(colorScheme.primaryContainer)
                        .border(
                            width = stroke.st1,
                            color = colorScheme.outline,
                            shape = RoundedCornerShape(radius.r10)
                        )
                        .padding(horizontal = spacing.s10, vertical = spacing.s8)
                ) {
                    PMText(
                        text = state.countryCode,
                        style = PMTextStyle.Label,
                        color = colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        item {
            PlateInput(
                plateInput = state.plateInput,
                countryCode = state.countryCode,
                isPlateValid = state.isPlateValid,
                isSearchEnabled = state.isSearchEnabled,
                onInputChanged = { onAction(SearchUiAction.PlateInputChanged(it)) },
                onSearchClick = { onAction(SearchUiAction.SearchClicked) }
            )

            val hasInvalidPlate = state.plateInput.isNotBlank() && !state.isPlateValid
            if (hasInvalidPlate) {
                PMText(
                    text = stringResource(R.string.search_plate_invalid_format),
                    style = PMTextStyle.Body,
                    color = colorScheme.error,
                    modifier = Modifier.padding(top = spacing.s8, start = spacing.s4)
                )
            } else if (state.formMessage != null) {
                PMText(
                    text = state.formMessage.resolve(),
                    style = PMTextStyle.Body,
                    color = colorScheme.error,
                    modifier = Modifier.padding(top = spacing.s8, start = spacing.s4)
                )
            }
            
            if (state.detectedCityName != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = spacing.s8, start = spacing.s4),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing.s6)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = colorScheme.onSurfaceVariant,
                        modifier = Modifier.width(spacing.s16)
                    )
                    PMText(
                        text = stringResource(R.string.search_detected_city_plate, state.detectedCityName),
                        style = PMTextStyle.Body,
                        color = colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing.s6),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccessTime,
                        contentDescription = null,
                        tint = colorScheme.primary
                    )
                    PMText(
                        text = stringResource(R.string.search_recent_title),
                        style = PMTextStyle.Title,
                        color = colorScheme.onBackground
                    )
                }
                PMText(
                    text = stringResource(R.string.search_recent_clear),
                    style = PMTextStyle.Label,
                    color = colorScheme.primary,
                    modifier = Modifier.clickable {
                        onAction(SearchUiAction.ClearRecentClicked)
                    }
                )
            }
        }

        if (state.recentSearches.isEmpty()) {
            item {
                PMText(
                    text = stringResource(R.string.search_recent_empty),
                    style = PMTextStyle.Body,
                    color = colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(
                items = state.recentSearches,
                key = { item -> item.normalizedPlateCode },
                contentType = { "recent_search_item" }
            ) { item ->
                RecentSearchItem(
                    item = item,
                    onClick = { onAction(SearchUiAction.RecentItemClicked(item.plateCode)) },
                    onBookmarkClick = {
                        onAction(SearchUiAction.RecentBookmarkClicked(item.normalizedPlateCode))
                    }
                )
            }
        }

        item {
            PMCard(
                modifier = Modifier.fillMaxWidth(),
                padding = PaddingValues(horizontal = spacing.s14, vertical = spacing.s12)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing.s8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = colorScheme.primary
                    )
                    PMText(
                        text = stringResource(R.string.search_safety_banner),
                        style = PMTextStyle.Body,
                        color = colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun PlateInput(
    plateInput: String,
    countryCode: String,
    isPlateValid: Boolean,
    isSearchEnabled: Boolean,
    onInputChanged: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val spacing = MaterialTheme.pmDimensions.spacing
    val radius = MaterialTheme.pmDimensions.radius
    val stroke = MaterialTheme.pmDimensions.stroke

    var textFieldValue by remember { mutableStateOf(TextFieldValue(plateInput)) }

    LaunchedEffect(plateInput) {
        if (textFieldValue.text != plateInput) {
            textFieldValue = TextFieldValue(
                text = plateInput,
                selection = TextRange(plateInput.length)
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(radius.r14))
            .background(colorScheme.surfaceVariant)
            .border(
                width = stroke.st1,
                color = if (plateInput.isNotBlank() && !isPlateValid) {
                    colorScheme.error
                } else {
                    colorScheme.primary
                },
                shape = RoundedCornerShape(radius.r14)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(colorScheme.primaryContainer)
                .padding(horizontal = spacing.s16, vertical = spacing.s16)
        ) {
            PMText(
                text = countryCode,
                style = PMTextStyle.Title,
                color = colorScheme.onPrimaryContainer
            )
        }

        VerticalDivider(
            modifier = Modifier
                .width(stroke.st1)
                .heightIn(min = spacing.s40),
            color = colorScheme.outlineVariant.copy(alpha = 0.5f)
        )

        BasicTextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                textFieldValue = newValue
                onInputChanged(newValue.text)
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.headlineMedium.copy(color = colorScheme.onSurface),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (isSearchEnabled) {
                        onSearchClick()
                    }
                }
            ),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = spacing.s16, vertical = spacing.s16),
            decorationBox = { innerTextField ->
                if (plateInput.isBlank()) {
                    PMText(
                        text = stringResource(R.string.search_plate_placeholder),
                        style = PMTextStyle.Headline,
                        color = colorScheme.onSurfaceVariant
                    )
                }
                innerTextField()
            }
        )

        VerticalDivider(
            modifier = Modifier
                .width(stroke.st1)
                .heightIn(min = spacing.s40),
            color = colorScheme.outlineVariant.copy(alpha = 0.5f)
        )

        FilledIconButton(
            onClick = onSearchClick,
            enabled = isSearchEnabled,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary,
                disabledContainerColor = colorScheme.surface,
                disabledContentColor = colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(horizontal = spacing.s8, vertical = spacing.s8)
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(R.string.search_submit)
            )
        }
    }
}

@Composable
private fun RecentSearchItem(
    item: SearchRecentUiModel,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val spacing = MaterialTheme.pmDimensions.spacing

    PlateCard(
        plateCode = item.plateCode,
        onClick = onClick,
        cityName = item.cityName,
        ratingAverage = item.ratingAverage,
        reportTags = item.reportTags,
        rightBottomContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.s12),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBookmarkClick,
                    modifier = Modifier.testTag("recent_bookmark_${item.normalizedPlateCode}")
                ) {
                    Icon(
                        imageVector = if (item.isBookmarked) {
                            Icons.Filled.Bookmark
                        } else {
                            Icons.Filled.BookmarkBorder
                        },
                        contentDescription = null,
                        tint = if (item.isBookmarked) {
                            colorScheme.primary
                        } else {
                            colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.width(spacing.s16)
                    )
                }
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(spacing.s16)
                )
            }
        }
    )
}

@Preview(name = "Search Screen Light", showBackground = true, backgroundColor = 0xFFF3F6FF)
@Composable
private fun SearchScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        SearchScreen(
            state = SearchUiState(
                plateInput = "34 ABC 123",
                isPlateValid = true,
                detectedCityName = "İstanbul",
                recentSearches = listOf(
                    SearchRecentUiModel(
                        normalizedPlateCode = "34ABC123",
                        plateCode = "34 ABC 123",
                        cityName = "İstanbul",
                        reportTags = previewReportTags(),
                        ratingAverage = 2.3,
                        commentCount = 1,
                        isBookmarked = true
                    ),
                    SearchRecentUiModel(
                        normalizedPlateCode = "34XYZ123",
                        plateCode = "34 XYZ 123",
                        cityName = "İstanbul",
                        reportTags = previewReportTags(),
                        ratingAverage = 3.3,
                        commentCount = 3,
                        isBookmarked = false
                    )
                )
            ),
            onAction = {}
        )
    }
}

@Preview(name = "Search Screen Dark", showBackground = true, backgroundColor = 0xFF07153A)
@Composable
private fun SearchScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        SearchScreen(
            state = SearchUiState(
                recentSearches = listOf(
                    SearchRecentUiModel(
                        normalizedPlateCode = "34ABC123",
                        plateCode = "34 ABC 123",
                        cityName = "İstanbul",
                        reportTags = previewReportTags(),
                        ratingAverage = 2.3,
                        commentCount = 1,
                        isBookmarked = true
                    ),
                    SearchRecentUiModel(
                        normalizedPlateCode = "34XYZ123",
                        plateCode = "34 XYZ 123",
                        cityName = "İstanbul",
                        reportTags = previewReportTags(),
                        ratingAverage = 3.3,
                        commentCount = 3,
                        isBookmarked = false
                    )
                )
            ),
            onAction = {}
        )
    }
}

private fun previewReportTags(): List<PlateReportTagUiModel> = listOf(
    PlateReportTagUiModel(
        code = "CUTS",
        label = "Cuts lanes",
        severity = "HIGH",
        colorHex = "#FF6A3D"
    ),
    PlateReportTagUiModel(
        code = "SPEEDING",
        label = "Speeding",
        severity = "MEDIUM",
        colorHex = "#FFB300"
    )
)

