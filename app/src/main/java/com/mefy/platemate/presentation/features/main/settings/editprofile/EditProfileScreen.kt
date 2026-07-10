package com.mefy.platemate.presentation.features.main.settings.editprofile

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mefy.platemate.presentation.common.text.resolve
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.spacedByWithFooter
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.components.PMCommentField
import com.mefy.platemate.presentation.components.PMPopup
import com.mefy.platemate.presentation.components.PMTextField
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.features.main.settings.editprofile.components.AddSocialLinkForm
import com.mefy.platemate.presentation.features.main.settings.editprofile.components.AddedSocialLinkRow
import com.mefy.platemate.presentation.features.main.settings.editprofile.components.AvatarEditSection
import com.mefy.platemate.presentation.features.uimodel.SocialPlatform
import com.mefy.platemate.presentation.features.uimodel.SocialPlatformFallbackTint
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
    val onSocialUrlChange = remember(onAction) { { v: String -> onAction(EditProfileUiAction.SocialUrlInputChanged(v)) } }
    val onAddSocialLink = remember(onAction) { { onAction(EditProfileUiAction.AddSocialLinkClicked) } }
    val onAvatarUrlConfirmed = remember(onAction) { { onAction(EditProfileUiAction.AvatarUrlConfirmed) } }
    val onAvatarDialogDismissed = remember(onAction) { { onAction(EditProfileUiAction.AvatarDialogDismissed) } }
    val onAvatarUrlChanged = remember(onAction) { { v: String -> onAction(EditProfileUiAction.AvatarUrlChanged(v)) } }

    if (state.showAvatarDialog) {
        PMPopup(
            title = stringResource(R.string.edit_profile_avatar_dialog_title),
            icon = Icons.Filled.Link,
            iconTint = colors.primary,
            iconContainerColor = colors.primaryContainer,
            primaryText = stringResource(R.string.common_save),
            onPrimaryClick = onAvatarUrlConfirmed,
            secondaryText = stringResource(R.string.common_cancel),
            onSecondaryClick = onAvatarDialogDismissed,
            onDismissRequest = onAvatarDialogDismissed
        ) {
            PMTextField(
                value = state.avatarUrlDraft,
                onValueChange = onAvatarUrlChanged,
                placeholder = stringResource(R.string.edit_profile_avatar_dialog_hint),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    // SnapshotStateMap okuması burada yapılır: link eklenince/silinince liste recompose olur.
    val addedSocialLinks = state.socialLinks.entries.toList()

    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            // Edge-to-edge'de adjustResize no-op; klavye açılınca liste viewport'u kısalsın ki
            // odaklanan PMTextField (bringIntoView ile) klavyenin üstüne kaysın, arkasında kalmasın.
            .imePadding(),
        contentPadding = PaddingValues(
            horizontal = dims.spacing.s16,
            vertical = dims.spacing.s8
        ),
        verticalArrangement = spacedByWithFooter(dims.spacing.s8)
    ) {

        item {
            AvatarEditSection(
                displayName = state.displayName,
                onAvatarClick = onAvatarEdit
            )
        }

        item {
            PMTextField(
                value = state.displayName,
                label = stringResource(R.string.edit_profile_field_display_name),
                onValueChange = onDisplayNameChange,
                isError = state.displayNameError != null,
                errorText = state.displayNameError?.resolve(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            val supportingText = if(state.usernameError == null && state.username.isNotEmpty())
                stringResource(R.string.edit_profile_username_hint, state.username) else ""

            PMTextField(
                value = state.username,
                label = stringResource(R.string.edit_profile_field_username),
                supportingText = supportingText,
                onValueChange = onUsernameChange,
                enabled = false,
                readOnly = true,
                isError = state.usernameError != null,
                errorText = state.usernameError?.resolve(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            PMSectionLabel(
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
            PMSectionLabel(
                text = stringResource(R.string.edit_profile_field_social)
            )
            HorizontalDivider(
                color = colors.outline,
            )
        }

        item {
            AddSocialLinkForm(
                url = state.socialUrlInput,
                errorText = state.socialLinkError?.resolve(),
                isAddEnabled = state.isAddSocialEnabled,
                onUrlChange = onSocialUrlChange,
                onAdd = onAddSocialLink
            )
        }

        if (addedSocialLinks.isNotEmpty()) {
            item {
                PMSectionLabel(text = stringResource(R.string.edit_profile_social_added))
            }
            items(
                items = addedSocialLinks,
                key = { it.key }
            ) { (code, url) ->
                val platform = state.availablePlatforms.firstOrNull { it.code.equals(code, ignoreCase = true) }
                AddedSocialLinkRow(
                    url = url,
                    iconUrl = platform?.iconUrl,
                    iconTint = platform?.iconTint ?: SocialPlatformFallbackTint,
                    //containerColor = platform?.backgroundColor ?: SocialPlatformFallbackBg,
                    onRemove = { onAction(EditProfileUiAction.RemoveSocialLinkClicked(code)) }
                )
            }
        }

        item {
            PMButton(
                text = stringResource(R.string.edit_profile_save),
                onClick = onSave,
                enabled = state.isDirty && !state.isSaving,
                loading = state.isSaving,
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
        SocialPlatform(1, "INSTAGRAM", "Instagram", null, "https://www.instagram.com/", Color(0xFFFDF2F8), Color(0xFFDB2777)),
        SocialPlatform(2, "X", "X", null, "https://x.com/", Color(0xFFF1F5F9), Color(0xFF0F172A))
    )
).apply {
    socialLinks["INSTAGRAM"] = "https://www.instagram.com/mfatihceliik"
}

@Preview(name = "EditProfileScreen Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun EditProfileScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        EditProfileScreen(state = previewState, onAction = {})
    }
}

@Preview(name = "EditProfileScreen Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun EditProfileScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        EditProfileScreen(state = previewState, onAction = {})
    }
}
