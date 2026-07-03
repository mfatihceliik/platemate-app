package com.mefy.platemate.presentation.features.main.settings.editprofile

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mefy.platemate.presentation.common.text.resolve
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import coil.compose.rememberAsyncImagePainter
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.spacedByWithFooter
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.components.PMCommentField
import com.mefy.platemate.presentation.components.PMRowItem
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTextField
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.main.settings.components.SectionLabel
import com.mefy.platemate.presentation.features.main.settings.editprofile.components.AvatarEditSection
import com.mefy.platemate.presentation.features.main.settings.editprofile.components.AvatarUrlDialog
import com.mefy.platemate.presentation.features.main.settings.editprofile.model.SocialPlatform
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun EditProfileScreen(
    modifier: Modifier = Modifier,
    state: EditProfileUiState,
    onAction: (EditProfileUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues()
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors


    // Stable, hoisted callbacks: fields/buttons skip recomposition while their data is unchanged.
    val onSave = remember(onAction) { { onAction(EditProfileUiAction.SaveClicked) } }
    val onAvatarEdit = remember(onAction) { { onAction(EditProfileUiAction.AvatarEditClicked) } }
    val onDeleteAccount = remember(onAction) { { onAction(EditProfileUiAction.DeleteAccountClicked) } }
    val onDisplayNameChange = remember(onAction) { { v: String -> onAction(EditProfileUiAction.DisplayNameChanged(v)) } }
    val onUsernameChange = remember(onAction) { { v: String -> onAction(EditProfileUiAction.UsernameChanged(v)) } }
    val onBioChange = remember(onAction) { { v: String -> onAction(EditProfileUiAction.BioChanged(v)) } }

    if (state.showAvatarDialog) {
        AvatarUrlDialog(
            url = state.avatarUrlDraft,
            onUrlChange = { onAction(EditProfileUiAction.AvatarUrlChanged(it)) },
            onConfirm = { onAction(EditProfileUiAction.AvatarUrlConfirmed) },
            onDismiss = { onAction(EditProfileUiAction.AvatarDialogDismissed) }
        )
    }

    val initials = remember(state.displayName) {
        state.displayName
            .split(" ")
            .take(2)
            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
            .joinToString("")
            .ifEmpty { "?" }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = dims.spacing.s16,
            vertical = dims.spacing.s8
        ),
        verticalArrangement = spacedByWithFooter(dims.spacing.s8)
    ) {

        item {
            AvatarEditSection(
                initials = initials,
                onAvatarClick = onAvatarEdit
            )
        }

        item {
            SectionLabel(
                text = stringResource(R.string.edit_profile_field_display_name)
            )
        }

        item {
            PMTextField(
                value = state.displayName,
                onValueChange = onDisplayNameChange,
                isError = state.displayNameError != null,
                errorText = state.displayNameError?.resolve(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            SectionLabel(
                text = stringResource(R.string.edit_profile_field_username)
            )
        }

        item {
            PMTextField(
                value = state.username,
                onValueChange = onUsernameChange,
                readOnly = true,
                isError = state.usernameError != null,
                errorText = state.usernameError?.resolve(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )
            if (state.usernameError == null && state.username.isNotEmpty()) {
                PMText(
                    text = stringResource(R.string.edit_profile_username_hint, state.username),
                    style = PMTextStyle.Note,
                    color = colors.textLabel,
                    modifier = Modifier.padding(start = dims.spacing.s4)
                )
            }
        }

        item {
            SectionLabel(
                text = stringResource(R.string.edit_profile_field_bio)
            )
        }

        item {
            PMCommentField(
                value = state.bio,
                onValueChange = onBioChange,
                maxLength = state.bioMaxLength,
                placeholder = stringResource(R.string.edit_profile_bio_placeholder),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            SectionLabel(
                text = stringResource(R.string.edit_profile_field_social)
            )
            HorizontalDivider(
                color = colors.outline,
            )
        }

        items(
            items = state.availablePlatforms,
            key = { it.id }
        ) { platform ->
            val interactionSource = remember {
                MutableInteractionSource()
            }
            //val focused by interactionSource.collectIsFocusedAsState()
            PMRowItem(
                leadingIconPainter = rememberAsyncImagePainter(
                    model = platform.iconUrl,
                    error = painterResource(R.drawable.ic_link),
                    placeholder = painterResource(R.drawable.ic_link)
                ),
                leadingIconTint = platform.iconTint,
                leadingContainerColor = platform.backgroundColor
            ) {
                PMTextField(
                    value = state.socialLinks[platform.code].orEmpty(),
                    onValueChange = {
                        onAction(
                            EditProfileUiAction.SocialLinkChanged(
                                platform.code,
                                it
                            )
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                )
            }
        }

        item {
            PMButton(
                text = stringResource(R.string.edit_profile_save),
                onClick = onSave,
                enabled = state.isSaving,
                modifier = Modifier.fillMaxWidth().padding(vertical = dims.spacing.s16)
            )
        }
    }
}



private val previewState = EditProfileUiState(
    isLoading = false,
    isSaving = false,
    displayName = "Ahmet Yılmaz",
    username = "ahmetyilmaz",
    bio = "İstanbul sürücüsüyüm. Saygılı ve temkinli araç kullanırım.",
    availablePlatforms = listOf(
        SocialPlatform(1, "INSTAGRAM", "Instagram", null, androidx.compose.ui.graphics.Color(0xFFFDF2F8), androidx.compose.ui.graphics.Color(0xFFDB2777)),
        SocialPlatform(2, "X", "X", null, androidx.compose.ui.graphics.Color(0xFFF1F5F9), androidx.compose.ui.graphics.Color(0xFF0F172A))
    )
)

@Preview(name = "EditProfileScreen Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun EditProfileScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        EditProfileScreen(state = previewState, onAction = {},)
    }
}

@Preview(name = "EditProfileScreen Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun EditProfileScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        EditProfileScreen(state = previewState, onAction = {},)
    }
}
