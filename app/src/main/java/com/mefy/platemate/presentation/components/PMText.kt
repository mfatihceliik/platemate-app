package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMText(
    text: String,
    style: PMTextStyle = PMTextStyle.Body,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        modifier = modifier,
        style = style.resolve(MaterialTheme.typography),
        color = color,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign
    )
}

@Preview(name = "PMText Light", showBackground = true, backgroundColor = 0xFFF6FAFB)
@Composable
private fun PMTextLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMTextPreviewContent()
    }
}

@Preview(name = "PMText Dark", showBackground = true, backgroundColor = 0xFF101618)
@Composable
private fun PMTextDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMTextPreviewContent()
    }
}

@Composable
private fun PMTextPreviewContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.pmDimensions.spacing.s16)
    ) {
        PMText(text = "PlateMate", style = PMTextStyle.Display)
        PMText(text = "Driver network and trust", style = PMTextStyle.Headline)
        PMText(text = "Login to continue", style = PMTextStyle.Title)
        PMText(text = "Body text sample", style = PMTextStyle.Body)
        PMText(text = "Label sample", style = PMTextStyle.Label)
        PMText(
            text = "Caption sample with long text that truncates in one line.",
            style = PMTextStyle.Caption,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
