package com.mefy.platemate.presentation.features.main.settings.editprofile

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.features.uimodel.SocialPlatform

@Immutable
data class EditProfileUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val isSaving: Boolean = false,
    val displayName: String = "",
    val username: String = "",
    val bio: String = "",
    val profilePhotoUrl: String = "",
    val showAvatarDialog: Boolean = false,
    val avatarUrlDraft: String = "",
    val displayNameError: UiText? = null,
    val usernameError: UiText? = null,
    val availablePlatforms: List<SocialPlatform> = emptyList(),
    // Eklenmiş linkler: platform kodu -> url. Aynı platform tekrar eklenirse üzerine yazılır.
    val socialLinks: SnapshotStateMap<String, String> = mutableStateMapOf(),
    // Yeni link ekleme formu.
    val socialUrlInput: String = "",
    val socialLinkError: UiText? = null,
    // Yüklenen ilk değerler (dirty karşılaştırması için). Username düzenlenemez → dahil değil.
    val initialDisplayName: String = "",
    val initialBio: String = "",
    val initialProfilePhotoUrl: String = "",
    val initialSocialLinks: Map<String, String> = emptyMap()
) {

    val bioLength: Int get() = bio.length
    val bioMaxLength: Int get() = 160
    val isAddSocialEnabled: Boolean get() = socialUrlInput.isNotBlank()

    // socialLinks (SnapshotStateMap) okunur → link ekle/sil'de Compose recompose olur.
    private fun currentSocial(): Map<String, String> =
        socialLinks.mapValues { it.value.trim() }.filterValues { it.isNotBlank() }

    val isProfileDirty: Boolean
        get() = displayName.trim() != initialDisplayName ||
            bio.trim() != initialBio ||
            profilePhotoUrl.trim() != initialProfilePhotoUrl

    val isSocialDirty: Boolean get() = currentSocial() != initialSocialLinks
    val isDirty: Boolean get() = isProfileDirty || isSocialDirty
}
