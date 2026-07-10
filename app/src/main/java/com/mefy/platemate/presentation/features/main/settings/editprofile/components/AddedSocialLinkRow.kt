package com.mefy.platemate.presentation.features.main.settings.editprofile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun AddedSocialLinkRow(
    url: String,
    iconUrl: String?,
    iconTint: Color,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMCard(
        modifier = modifier.fillMaxWidth(),
        padding = PaddingValues(horizontal = dims.spacing.s12, vertical = dims.spacing.s8)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            PMIcon(
                painter = rememberAsyncImagePainter(
                    model = iconUrl,
                    error = painterResource(R.drawable.ic_link),
                    placeholder = painterResource(R.drawable.ic_link)
                ),
                contentDescription = null,
                tint = iconTint,
                //containerColor = containerColor
            )
            PMText(
                text = url,
                style = PMTextStyle.Caption,
                color = colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            PMIconButton(
                onClick = onRemove,
                imageVector = Icons.Filled.Close,
                size = dims.sizing.iconSm,
                contentDescription = stringResource(R.string.profile_social_delete_button)
            )
        }
    }
}