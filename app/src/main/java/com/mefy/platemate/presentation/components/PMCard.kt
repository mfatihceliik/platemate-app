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
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    padding: PaddingValues? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(MaterialTheme.pmDimensions.radius.r8)
    val colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    val border = BorderStroke(MaterialTheme.pmDimensions.stroke.st1, MaterialTheme.colorScheme.outlineVariant)
    val resolvedPadding = padding ?: PaddingValues(MaterialTheme.pmDimensions.spacing.s16)

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
            onClick = onClick,
            enabled = enabled,
            shape = shape,
            colors = colors,
            border = border
        ) {
            Column(modifier = Modifier.padding(resolvedPadding), content = content)
        }
    }
}

@Preview(name = "PMCard Light", showBackground = true, backgroundColor = 0xFFF6FAFB)
@Composable
private fun PMCardLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMCardPreviewContent()
    }
}

@Preview(name = "PMCard Dark", showBackground = true, backgroundColor = 0xFF101618)
@Composable
private fun PMCardDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMCardPreviewContent()
    }
}

@Composable
private fun PMCardPreviewContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.pmDimensions.spacing.s16)
    ) {
        PMCard(modifier = Modifier.fillMaxWidth()) {
            PMText(text = "Static card", style = PMTextStyle.Title)
            PMText(
                text = "Consistent container for reusable content blocks.",
                style = PMTextStyle.Body,
                modifier = Modifier.padding(top = MaterialTheme.pmDimensions.spacing.s6)
            )
        }
        PMCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.pmDimensions.spacing.s12),
            onClick = {}
        ) {
            PMText(text = "Clickable card", style = PMTextStyle.Title)
            PMText(
                text = "Tap behavior supported through optional onClick.",
                style = PMTextStyle.Body,
                modifier = Modifier.padding(top = MaterialTheme.pmDimensions.spacing.s6)
            )
        }
    }
}
