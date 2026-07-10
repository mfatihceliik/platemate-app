package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.variant.PMTextFieldVariant
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

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
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    
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

    PMTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            textFieldValue = newValue
            onQueryChange(newValue.text)
        },
        modifier = modifier,
        placeholder = placeholder,
        variant = PMTextFieldVariant.Search,
        enabled = enabled,
        singleLine = true,
        supportingText = supportingText,
        errorText = errorText,
        isError = isError,
        isSuccess = isSuccess,
        keyboardOptions = KeyboardOptions(
            imeAction = if (onSearch != null) ImeAction.Search else ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onSearch = { if (enabled && isSearchActionEnabled) onSearch?.invoke() }
        ),
        leadingIcon = {
            PMIcon(
                imageVector = Icons.Outlined.Search,
                tint = if(enabled) colors.primary else colors.textLabel
            )
        },
        trailingIcon = {
            if (isLoading) {
                PMCircularProgressIndicator(
                    size = dims.sizing.circleProgressBarXs
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
                ) {
                    if (query.isNotEmpty() && enabled) {
                        PMIconButton(
                            imageVector = Icons.Rounded.Close,
                            onClick = { onQueryChange("") },
                            iconColor = colors.textLabel
                        )
                    }
                    if (isSearchActionEnabled && enabled && onSearch != null) {
                        PMIconButton(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = stringResource(R.string.main_tab_search),
                            onClick = onSearch,
                        )
                    }
                }
            }
        }
    )
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
                isSearchActionEnabled = true,
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
                isSearchActionEnabled = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
