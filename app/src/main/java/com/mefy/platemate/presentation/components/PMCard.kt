package com.mefy.platemate.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.model.PMCardVariant
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClick
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMCard(
    modifier: Modifier = Modifier,
    variant: PMCardVariant = PMCardVariant.Standard,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    debounceClick: Boolean = true,
    debounceMillis: Long = 600L,
    padding: PaddingValues? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val pmColors = MaterialTheme.pmColors
    val shape = when (variant) {
        PMCardVariant.Standard -> RoundedCornerShape(dims.radius.r12)
        PMCardVariant.Large -> RoundedCornerShape(dims.radius.r16)
    }
    val colors = CardDefaults.cardColors(containerColor = pmColors.surface)
    val border = BorderStroke(dims.stroke.st1, pmColors.outlineVariant)
    val resolvedPadding = padding ?: PaddingValues(dims.spacing.s16)

    val safeOnClick = if (onClick != null && debounceClick) {
        debouncedClick(debounceMillis = debounceMillis, onClick = onClick)
    } else {
        onClick
    }

    if (onClick == null) {
        Card(
            modifier = modifier,
            shape = shape,
            colors = colors,
            border = border
        ) {
            Column(modifier = Modifier.padding(resolvedPadding), content = content)
        }
    } else {
        Card(
            modifier = modifier,
            onClick = safeOnClick!!,
            enabled = enabled,
            shape = shape,
            colors = colors,
            border = border
        ) {
            Column(modifier = Modifier.padding(resolvedPadding), content = content)
        }
    }
}

@Preview(name = "PMCard Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMCardLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMCardPreviewContent()
    }
}

@Preview(name = "PMCard Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMCardDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMCardPreviewContent()
    }
}

@Composable
private fun PMCardPreviewContent() {

    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(dims.spacing.s16)
    ) {
        PMCard(modifier = Modifier.fillMaxWidth()) {
            PMText(text = "Standard card (12dp)", style = PMTextStyle.Title)
            PMText(
                text = "Consistent container for reusable content blocks.",
                style = PMTextStyle.Body,
                modifier = Modifier.padding(top = MaterialTheme.pmDimensions.spacing.s8)
            )
        }
        PMCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.pmDimensions.spacing.s12),
            variant = PMCardVariant.Large,
            onClick = {}
        ) {
            PMText(text = "Large card (16dp)", style = PMTextStyle.Title)
            PMText(
                text = "Tap behavior supported through optional onClick.",
                style = PMTextStyle.Body,
                modifier = Modifier.padding(top = MaterialTheme.pmDimensions.spacing.s8)
            )
        }
    }
}
