package com.mefy.platemate.presentation.features.main.settings.editprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.R
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.main.settings.sociallinks.components.getPlatformInfo
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun SocialLinkEditRow(
    platform: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val info = getPlatformInfo(platform)
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val fieldShape = MaterialTheme.shapes.small

    val borderColor = if (isFocused) colors.primary else colors.outline

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(fieldShape)
            .background(
                if (isFocused) colors.primary.copy(alpha = 0.04f)
                else colors.surface
            )
            .border(dims.stroke.st1, borderColor, fieldShape)
            .padding(horizontal = dims.spacing.s12, vertical = dims.spacing.s12),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s10),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(dims.spacing.s32)
                .clip(MaterialTheme.shapes.extraSmall)
                .background(info.bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(info.iconRes),
                contentDescription = info.displayName,
                tint = info.iconTint,
                modifier = Modifier.size(dims.spacing.s16)
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            if (value.isEmpty()) {
                PMText(
                    text = stringResource(R.string.edit_profile_social_add_hint, info.displayName),
                    style = PMTextStyle.Body,
                    color = colors.textLabel
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(color = colors.textPrimary, fontSize = dims.fontSize.md),
                cursorBrush = SolidColor(colors.primary),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                interactionSource = interactionSource
            )
        }

        if (value.isNotEmpty()) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null,
                tint = colors.disabled,
                modifier = Modifier.size(dims.spacing.s12)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(dims.spacing.s24)
                    .clip(CircleShape)
                    .background(colors.surfaceVariant)
                    .border(dims.stroke.st1, colors.outline, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null,
                    tint = colors.textLabel,
                    modifier = Modifier.size(dims.spacing.s12)
                )
            }
        }
    }
}

@Preview(name = "SocialLinkEditRow Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun SocialLinkEditRowLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions

        Column(
            modifier = Modifier.padding(dims.spacing.s12),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            SocialLinkEditRow(platform = "INSTAGRAM", value = "https://instagram.com/ahmet", onValueChange = {})
            SocialLinkEditRow(platform = "X", value = "", onValueChange = {})
            SocialLinkEditRow(platform = "FACEBOOK", value = "", onValueChange = {})
        }
    }
}

@Preview(name = "SocialLinkEditRow Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun SocialLinkEditRowDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions

        Column(
            modifier = Modifier.padding(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            SocialLinkEditRow(platform = "INSTAGRAM", value = "https://instagram.com/ahmet", onValueChange = {})
            SocialLinkEditRow(platform = "X", value = "", onValueChange = {})
            SocialLinkEditRow(platform = "FACEBOOK", value = "", onValueChange = {})
        }
    }
}
