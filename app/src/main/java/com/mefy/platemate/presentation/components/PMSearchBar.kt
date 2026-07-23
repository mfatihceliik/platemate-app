package com.mefy.platemate.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.components.variant.PMTextFieldVariant
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun PMSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSearch: (() -> Unit)? = null,
    isSearchActionEnabled: Boolean = false,
    placeholder: String = stringResource(R.string.search_plate_placeholder),
    isLoading: Boolean = false,
    enabled: Boolean = true,
    supportingText: String? = null,
    errorText: String? = null,
    isError: Boolean = false,
    isSuccess: Boolean = false,
) {
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing
    val radius = PMTheme.radius
    val stroke = PMTheme.stroke
    val colors = PMTheme.colors

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val shouldShowError = isError || errorText != null
    val supportingOrError = errorText ?: supportingText
    val containerShape = RoundedCornerShape(radius.r16)

    // Focus halkası input + dock'lu butonu birlikte sarar; focus success'ten önce gelir.
    val borderColor by animateColorAsState(
        targetValue = when {
            shouldShowError -> colors.error
            isFocused -> colors.primary
            isSuccess -> colors.primary
            else -> Color.Transparent
        },
        label = "searchBorderColor"
    )

    val leadingIconTint by animateColorAsState(
        targetValue = if (isFocused && enabled) colors.primary else colors.textLabel,
        label = "searchLeadingIconTint"
    )

    var textFieldValue by remember { mutableStateOf(TextFieldValue(query, TextRange(query.length))) }

    if (query != textFieldValue.text) {
        val oldContent = textFieldValue.text.filter { it != ' ' }
        val newContent = query.filter { it != ' ' }

        textFieldValue = if (oldContent == newContent) {
            val oldCursor = textFieldValue.selection.start.coerceIn(0, textFieldValue.text.length)
            val contentCharsBefore = textFieldValue.text.take(oldCursor).count { it != ' ' }
            var newCursor = 0
            var seen = 0
            for (ch in query) {
                if (seen >= contentCharsBefore) break
                newCursor++
                if (ch != ' ') seen++
            }
            TextFieldValue(
                text = query,
                selection = TextRange(newCursor.coerceAtMost(query.length))
            )
        } else {
            TextFieldValue(
                text = query,
                selection = TextRange(query.length)
            )
        }
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(sizing.searchBarHeight)
                .clip(containerShape)
                .background(colors.searchFieldBg)
                .border(stroke.st2, borderColor, containerShape),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMTextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    textFieldValue = newValue
                    onQueryChange(newValue.text)
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                placeholder = placeholder,
                variant = PMTextFieldVariant.Search,
                enabled = enabled,
                singleLine = true,
                interactionSource = interactionSource,
                keyboardOptions = KeyboardOptions(
                    imeAction = if (onSearch != null) ImeAction.Search else ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { if (enabled && isSearchActionEnabled) onSearch?.invoke() }
                ),
                leadingIcon = {
                    PMIcon(
                        imageVector = Icons.Outlined.Search,
                        tint = leadingIconTint
                    )
                },
                trailingIcon = {
                    if (isLoading && onSearch == null) {
                        PMCircularProgressIndicator(
                            size = sizing.circleProgressBarXs
                        )
                    } else {
                        AnimatedVisibility(
                            visible = query.isNotEmpty() && enabled && !isLoading,
                            enter = fadeIn() + scaleIn(initialScale = 0.6f),
                            exit = fadeOut() + scaleOut(targetScale = 0.6f)
                        ) {
                            PMIconButton(
                                imageVector = Icons.Rounded.Close,
                                onClick = { onQueryChange("") },
                                iconColor = colors.textLabel
                            )
                        }
                    }
                }
            )

            if (onSearch != null) {
                DockedSearchButton(
                    isActive = isSearchActionEnabled && enabled,
                    isLoading = isLoading,
                    onClick = onSearch
                )
            }
        }

        if (supportingOrError != null) {
            PMSectionLabel(
                text = supportingOrError,
                style = PMTextStyle.Label,
                color = when {
                    shouldShowError -> colors.error
                    isSuccess -> colors.textPrimary
                    else -> colors.textLabel
                },
                modifier = Modifier.padding(
                    start = spacing.s4,
                    top = spacing.s4
                )
            )
        }
    }
}

@Composable
private fun DockedSearchButton(
    isActive: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val sizing = PMTheme.sizing
    val colors = PMTheme.colors

    val backgroundColor by animateColorAsState(
        targetValue = if (isActive && !isLoading) colors.primary else colors.surfaceVariant,
        label = "dockedSearchButtonBg"
    )

    Box(
        modifier = Modifier
            .width(sizing.searchBarHeight)
            .fillMaxHeight()
            .background(backgroundColor)
            .debouncedClickable(enabled = isActive && !isLoading, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            PMCircularProgressIndicator(
                size = sizing.circleProgressBarXs
            )
        } else {
            PMIcon(
                imageVector = Icons.Outlined.Search,
                contentDescription = stringResource(R.string.main_tab_search),
                tint = if (isActive) colors.onPrimary else colors.textLabel
            )
        }
    }
}

@Preview(name = "PMSearchBar States", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMSearchBarPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val spacing = PMTheme.spacing
        val colors = PMTheme.colors
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.background)
                .padding(spacing.s16),
            verticalArrangement = Arrangement.spacedBy(spacing.s12)
        ) {
            var query by remember { mutableStateOf("") }
            PMSearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = {},
                modifier = Modifier.fillMaxWidth()
            )
            PMSearchBar(
                query = "34 EK 0682",
                onQueryChange = {},
                onSearch = {},
                isSearchActionEnabled = true,
                isSuccess = true,
                supportingText = "İstanbul plakası",
                modifier = Modifier.fillMaxWidth()
            )
            PMSearchBar(
                query = "34 EK",
                onQueryChange = {},
                onSearch = {},
                isError = true,
                errorText = "Geçersiz plaka formatı",
                modifier = Modifier.fillMaxWidth()
            )
            PMSearchBar(
                query = "34 EK 0682",
                onQueryChange = {},
                onSearch = {},
                isLoading = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
            PMSearchBar(
                query = "",
                onQueryChange = {},
                placeholder = "Dil ara",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(name = "PMSearchBar Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMSearchBarDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        val spacing = PMTheme.spacing
        val colors = PMTheme.colors
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.background)
                .padding(spacing.s16),
            verticalArrangement = Arrangement.spacedBy(spacing.s12)
        ) {
            PMSearchBar(
                query = "",
                onQueryChange = {},
                onSearch = {},
                modifier = Modifier.fillMaxWidth()
            )
            PMSearchBar(
                query = "06 ABC 123",
                onQueryChange = {},
                onSearch = {},
                isSearchActionEnabled = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
