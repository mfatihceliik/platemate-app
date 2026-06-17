package com.mefy.platemate.presentation.features.main.profile.settings.editprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTextField
import com.mefy.platemate.presentation.components.PMTopBarConfig
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.main.profile.settings.editprofile.components.AvatarEditSection
import com.mefy.platemate.presentation.features.main.profile.settings.editprofile.components.DangerZoneSection
import com.mefy.platemate.presentation.features.main.profile.settings.editprofile.components.EditProfileSaveAction
import com.mefy.platemate.presentation.features.main.profile.settings.editprofile.components.FieldLabel
import com.mefy.platemate.presentation.features.main.profile.settings.editprofile.components.SocialLinkEditRow
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun EditProfileScreen(
    state: EditProfileUiState,
    onAction: (EditProfileUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.WithActions(
            title = stringResource(R.string.edit_profile_title),
            onBackClick = { onAction(EditProfileUiAction.BackClicked) },
            actions = {
                EditProfileSaveAction(
                    isSaving = state.isSaving,
                    onSaveClick = { onAction(EditProfileUiAction.SaveClicked) }
                )
            }
        ),
        containerColor = colors.surfaceSecondary
    ) { innerPadding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(bottom = dims.spacing.s32)
            ) {
                item(key = "avatar") {
                    val initials = state.displayName
                        .split(" ")
                        .take(2)
                        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                        .joinToString("")
                        .ifEmpty { "?" }

                    AvatarEditSection(
                        initials = initials,
                        onAvatarClick = { onAction(EditProfileUiAction.AvatarEditClicked) }
                    )
                }

                item(key = "form") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colors.surfaceSecondary)
                            .padding(dims.spacing.s16),
                        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
                    ) {
                        FieldLabel(stringResource(R.string.edit_profile_field_display_name))
                        PMTextField(
                            value = state.displayName,
                            onValueChange = { onAction(EditProfileUiAction.DisplayNameChanged(it)) },
                            isError = state.displayNameError != null,
                            errorText = state.displayNameError,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            modifier = Modifier.fillMaxWidth()
                        )

                        FieldLabel(stringResource(R.string.edit_profile_field_username))
                        Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)) {
                            PMTextField(
                                value = state.username,
                                onValueChange = { onAction(EditProfileUiAction.UsernameChanged(it)) },
                                isError = state.usernameError != null,
                                errorText = state.usernameError,
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

                        FieldLabel(stringResource(R.string.edit_profile_field_bio))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.small)
                                .background(MaterialTheme.colorScheme.surface)
                                .border(dims.stroke.st1, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small)
                                .padding(dims.spacing.s12)
                        ) {
                            Column {
                                BasicTextField(
                                    value = state.bio,
                                    onValueChange = { onAction(EditProfileUiAction.BioChanged(it)) },
                                    minLines = 3,
                                    maxLines = 5,
                                    textStyle = TextStyle(color = colors.textPrimary, fontSize = dims.fontSize.lg),
                                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(top = dims.spacing.s8),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    PMText(
                                        text = "${state.bioLength}/${state.bioMaxLength}",
                                        style = PMTextStyle.Note,
                                        color = colors.textLabel
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = dims.spacing.s4),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
                        ) {
                            FieldLabel(stringResource(R.string.edit_profile_field_social))
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        SocialLinkEditRow(
                            platform = "INSTAGRAM",
                            value = state.instagramUrl,
                            onValueChange = { onAction(EditProfileUiAction.InstagramChanged(it)) }
                        )
                        SocialLinkEditRow(
                            platform = "X",
                            value = state.twitterUrl,
                            onValueChange = { onAction(EditProfileUiAction.TwitterChanged(it)) }
                        )
                        SocialLinkEditRow(
                            platform = "FACEBOOK",
                            value = state.linkedInUrl,
                            onValueChange = { onAction(EditProfileUiAction.LinkedInChanged(it)) }
                        )

                        DangerZoneSection(
                            onDeleteAccountClick = { onAction(EditProfileUiAction.DeleteAccountClicked) },
                            modifier = Modifier.padding(top = dims.spacing.s4)
                        )
                    }
                }
            }
        }
    }
}

private val previewState = EditProfileUiState(
    isLoading = false,
    isSaving = false,
    displayName = "Ahmet Yılmaz",
    username = "ahmetyilmaz",
    bio = "İstanbul sürücüsü. Saygılı ve temkinli araç kullanırım.",
    instagramUrl = "https://instagram.com/ahmet",
    twitterUrl = "",
    linkedInUrl = ""
)

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
