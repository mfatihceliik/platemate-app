package com.mefy.platemate.presentation.features.main.profile.settings.editprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.pmDimensions
import com.mefy.platemate.presentation.theme.pmColors

@Composable
internal fun EditProfileSaveAction(
    isSaving: Boolean,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val primary = colors.primary

    Box(
        modifier = modifier
            .padding(end = dims.spacing.s8)
            .height(dims.spacing.s32)
            .shadow(elevation = dims.stroke.st2, shape = MaterialTheme.shapes.extraSmall, spotColor = primary.copy(alpha = 0.5f))
            .clip(MaterialTheme.shapes.extraSmall)
            .background(primary)
            .debouncedClickable(enabled = !isSaving, onClick = onSaveClick)
            .padding(horizontal = dims.spacing.s12),
        contentAlignment = Alignment.Center
    ) {
        if (isSaving) {
            CircularProgressIndicator(
                modifier = Modifier.size(dims.spacing.s16),
                color = Color.White,
                strokeWidth = dims.stroke.st2
            )
        } else {
            PMText(
                text = stringResource(R.string.edit_profile_save),
                style = PMTextStyle.Body,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Preview(name = "EditProfileSaveAction Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun EditProfileSaveActionLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        Row {
            EditProfileSaveAction(isSaving = false, onSaveClick = {})
            EditProfileSaveAction(isSaving = true, onSaveClick = {})
        }
    }
}

@Preview(name = "EditProfileSaveAction Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun EditProfileSaveActionDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        Row {
            EditProfileSaveAction(isSaving = false, onSaveClick = {})
            EditProfileSaveAction(isSaving = true, onSaveClick = {})
        }
    }
}
