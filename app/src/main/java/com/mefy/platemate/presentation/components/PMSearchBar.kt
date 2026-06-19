package com.mefy.platemate.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSearch: (() -> Unit)? = null,
    placeholder: String = stringResource(R.string.search_plate_placeholder),
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val searchBarShape = RoundedCornerShape(dims.radius.r12)
    val buttonEnabled = query.isNotBlank() && !isLoading && enabled && onSearch != null

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
            TextFieldValue(text = query, selection = TextRange(newCursor.coerceAtMost(query.length)))
        } else {
            TextFieldValue(text = query, selection = TextRange(query.length))
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .height(dims.sizing.searchBarHeight)
                .clip(searchBarShape)
                .background(colors.searchFieldBg)
                .padding(horizontal = dims.spacing.s16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            PMIcon(
                imageVector = Icons.Outlined.Search,
            )

            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    PMText(
                        text = placeholder,
                        color = colors.textLabel,
                        fontSize = dims.fontSize.lg,
                        fontWeight = FontWeight.Normal
                    )
                }
                BasicTextField(
                    value = textFieldValue,
                    onValueChange = { newValue ->
                        textFieldValue = newValue
                        onQueryChange(newValue.text)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = colors.textPrimary,
                        letterSpacing = 0.3.sp
                    ),
                    cursorBrush = SolidColor(colors.primary),
                    keyboardOptions = KeyboardOptions(
                        imeAction = if (onSearch != null) ImeAction.Search else ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onSearch = { if (buttonEnabled) onSearch?.invoke() })
                )
            }

            if (isLoading) {
                val infiniteTransition = rememberInfiniteTransition(label = "search_spin")
                val rotation by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing)),
                    label = "rotation"
                )
                PMIcon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier
                        .rotate(rotation)
                )
            }
        }

        if (onSearch != null) {
            Box(
                modifier = Modifier
                    .height(dims.sizing.searchBarHeight)
                    .clip(searchBarShape)
                    .background(
                        if (buttonEnabled) colors.primary
                        else colors.disabled
                    )
                    .debouncedClickable(enabled = buttonEnabled, onClick = onSearch)
                    .padding(horizontal = dims.spacing.s24),
                contentAlignment = Alignment.Center
            ) {
                PMText(
                    text = stringResource(R.string.search_submit),
                    color = colors.onPrimary,
                    fontSize = dims.fontSize.lg,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(name = "PMSearchBar States", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMSearchBarPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions
        val colors = MaterialTheme.pmColors
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.background)
                .padding(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
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
                modifier = Modifier.fillMaxWidth()
            )
            PMSearchBar(
                query = "34 EK 0682",
                onQueryChange = {},
                onSearch = {},
                isLoading = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(name = "PMSearchBar Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMSearchBarDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions
        val colors = MaterialTheme.pmColors
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.background)
                .padding(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
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
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
