package com.mefy.platemate.presentation.features.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun AuthInfoBanner(
    text: String,
    links: List<String> = emptyList(),
    modifier: Modifier = Modifier
) {
    val dimensions = MaterialTheme.pmDimensions
    val spacing = dimensions.spacing
    var isChecked by remember { mutableStateOf(true) }

    val annotatedString = buildAnnotatedString {
        var currentIndex = 0
        val textLower = text.lowercase()
        
        // Find all links and their positions
        val linkPositions = links.mapNotNull { link ->
            val index = textLower.indexOf(link.lowercase())
            if (index >= 0) Triple(index, index + link.length, link) else null
        }.sortedBy { it.first }

        if (linkPositions.isEmpty()) {
            append(text)
        } else {
            for (pos in linkPositions) {
                // Append text before link
                if (pos.first > currentIndex) {
                    append(text.substring(currentIndex, pos.first))
                }
                // Append highlighted link
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append(text.substring(pos.first, pos.second))
                }
                currentIndex = pos.second
            }
            // Append remaining text
            if (currentIndex < text.length) {
                append(text.substring(currentIndex))
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { isChecked = !isChecked }
            .padding(vertical = spacing.s8),
        horizontalArrangement = Arrangement.spacedBy(spacing.s12),
        verticalAlignment = Alignment.Top
    ) {
        // Checkbox box
        Box(
            modifier = Modifier
                .padding(top = spacing.s4) // slight alignment tweak
                .size(spacing.s24)
                .background(
                    color = if (isChecked) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = RoundedCornerShape(dimensions.radius.r8)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isChecked) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(spacing.s16)
                )
            }
        }
        
        PMText(
            text = annotatedString,
            style = PMTextStyle.Caption,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
    }
}
