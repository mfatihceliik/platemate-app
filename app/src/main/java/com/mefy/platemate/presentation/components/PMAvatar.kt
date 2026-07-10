package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.avatar.AvatarPalette
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
import kotlin.math.abs

@Composable
fun PMAvatar(
    displayName: String,
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    size: Dp = MaterialTheme.pmDimensions.sizing.avatarXl,
    isEditable: Boolean = false,
    onClick: (() -> Unit)? = null,
    onEditClick: (() -> Unit)? = null
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val primary = colors.primary

    val colorSets = remember(colors) {
        listOf(
            colors.avatarIndigoBg to colors.avatarIndigoFg,
            colors.avatarTealBg to colors.avatarTealFg,
            colors.avatarGreenBg to colors.avatarGreenFg,
            colors.avatarOrangeBg to colors.avatarOrangeFg
        )
    }

    // Initials are derived here from the full display name so callers never
    // pre-compute them; deterministic color pick also hashes the full name.
    val initials = remember(displayName) { AvatarPalette.initials(displayName) }

    val colorIndex = remember(displayName) {
        abs(displayName.hashCode()) % colorSets.size
    }
    val (bgColor, fgColor) = colorSets[colorIndex]

    val baseModifier = modifier
        .size(size)
        .let {
            if (onClick != null) {
                it.debouncedClickable(onClick = onClick)
            } else {
                it
            }
        }

    Box(
        modifier = baseModifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        // Main Avatar
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(bgColor)
                .border(dims.stroke.st1, fgColor.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // TODO: If imageUrl is not null, load Image with Coil here.
            // For now, show initials since the user only mentioned empty image behavior.
            PMText(
                text = initials,
                style = PMTextStyle.Headline,
                fontWeight = FontWeight.ExtraBold,
                color = fgColor
            )
        }

        // Edit Icon
        if (isEditable) {

            Box(
                modifier = Modifier
                    .size(dims.sizing.iconLg)
                    .shadow(elevation = dims.spacing.s4, shape = CircleShape, spotColor = primary.copy(alpha = 0.45f))
                    .clip(CircleShape)
                    .background(primary)
                    .border(dims.stroke.st1, colors.surface, CircleShape)
                    .let {
                        if (onEditClick != null) it.debouncedClickable(onClick = onEditClick)
                        else it
                    },
                contentAlignment = Alignment.Center
            ) {
                PMIcon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = stringResource(R.string.edit_profile_change_photo),
                    tint = Color.White,
                )
            }
        }
    }
}

@Preview(name = "PMAvatar Regular", showBackground = true)
@Composable
private fun PMAvatarPreview() {
    PlateMateTheme {
        PMAvatar(
            displayName = "Fatih Çelik",
            onClick = {}
        )
    }
}

@Preview(name = "PMAvatar Editable", showBackground = true)
@Composable
private fun PMAvatarEditablePreview() {
    PlateMateTheme {
        PMAvatar(
            displayName = "Ahmet Yılmaz",
            isEditable = true,
            onEditClick = {}
        )
    }
}
