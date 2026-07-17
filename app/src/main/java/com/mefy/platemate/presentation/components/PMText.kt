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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
import androidx.compose.ui.unit.TextUnit
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.resolve
import androidx.compose.ui.platform.LocalLocale

@Composable
fun PMText(
    modifier: Modifier = Modifier,
    text: String,
    style: PMTextStyle = PMTextStyle.Body,
    fontWeight: FontWeight? = null,
    fontSize: TextUnit = MaterialTheme.pmDimensions.fontSize.sm,
    color: Color = MaterialTheme.pmColors.onSurface,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null
) {
    val colors = MaterialTheme.pmColors
    val resolvedColor = if (style == PMTextStyle.SectionLabel && color == colors.onSurface) {
        colors.textPrimary
    } else {
        color
    }
    val resolvedText = if (style == PMTextStyle.SectionLabel) {
        text.uppercase(LocalLocale.current.platformLocale)
    } else {
        text
    }

    Text(
        text = resolvedText,
        modifier = modifier,
        style = style.resolve(MaterialTheme.typography),
        fontWeight = fontWeight,
        fontSize = fontSize,
        color = resolvedColor,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign
    )
}

@Composable
fun PMText(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    style: PMTextStyle = PMTextStyle.Body,
    fontWeight: FontWeight? = null,
    fontSize: TextUnit = MaterialTheme.pmDimensions.fontSize.sm,
    color: Color = MaterialTheme.pmColors.onSurface,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null
) {
    val colors = MaterialTheme.pmColors
    val resolvedColor = if (style == PMTextStyle.SectionLabel && color == colors.onSurface) {
        colors.textPrimary
    } else {
        color
    }
    val resolvedText = if (style == PMTextStyle.SectionLabel) {
        AnnotatedString(
            text = text.text.uppercase(LocalLocale.current.platformLocale),
            spanStyles = text.spanStyles,
            paragraphStyles = text.paragraphStyles
        )
    } else {
        text
    }

    Text(
        modifier = modifier,
        text = resolvedText,
        style = style.resolve(MaterialTheme.typography),
        fontWeight = fontWeight,
        fontSize = fontSize,
        color = resolvedColor,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign
    )
}

@Preview(name = "PMText Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMTextLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMTextPreviewContent()
    }
}

@Preview(name = "PMText Dark", showBackground = true, backgroundColor = 0xFF0F172A)
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
            .background(MaterialTheme.pmColors.background)
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
        PMText(text = "Note · secondary info · 12sp", style = PMTextStyle.Note)
        PMText(text = "Son Aramalar", style = PMTextStyle.SectionLabel)
    }
}
