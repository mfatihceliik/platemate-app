package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.style.TextOverflow
import com.mefy.platemate.presentation.features.uimodel.PlateReportTagUiModel
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun ReportTypeCard(
    tag: PlateReportTagUiModel,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.pmDimensions.spacing
    val radius = MaterialTheme.pmDimensions.radius
    val colorScheme = MaterialTheme.colorScheme
    val (backgroundColor, contentColor) = resolveReportTagColors(
        colorHex = tag.colorHex,
        fallbackBackground = colorScheme.primaryContainer,
        fallbackContent = colorScheme.onPrimaryContainer
    )

    PMText(
        text = tag.label,
        style = PMTextStyle.Label,
        color = contentColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(radius.r8)
            )
            .padding(horizontal = spacing.s8, vertical = spacing.s4)
    )
}

internal fun resolveReportTagColors(
    colorHex: String,
    fallbackBackground: Color,
    fallbackContent: Color
): Pair<Color, Color> {
    val parsedBackground = parseColorHexOrNull(colorHex)
    val background = parsedBackground ?: fallbackBackground
    val content = if (parsedBackground == null) {
        fallbackContent
    } else {
        if (background.luminance() > 0.55f) Color(0xFF1A1A1A) else Color.White
    }
    return background to content
}

internal fun parseColorHexOrNull(rawHex: String): Color? {
    val normalized = rawHex.trim()
        .removePrefix("#")
        .let { hex ->
            when (hex.length) {
                3 -> hex.map { "$it$it" }.joinToString(separator = "").uppercase()
                4 -> hex.map { "$it$it" }.joinToString(separator = "").uppercase()
                6 -> "FF${hex.uppercase()}"
                8 -> hex.uppercase()
                else -> return null
            }
        }

    val argb = normalized.toLongOrNull(16) ?: return null
    val alpha = ((argb shr 24) and 0xFF).toInt()
    val red = ((argb shr 16) and 0xFF).toInt()
    val green = ((argb shr 8) and 0xFF).toInt()
    val blue = (argb and 0xFF).toInt()
    return Color(red = red, green = green, blue = blue, alpha = alpha)
}
