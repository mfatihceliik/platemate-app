package com.mefy.platemate.presentation.features.admin.socialplatforms.form

sealed interface SocialPlatformFormUiAction {
    data object BackClicked : SocialPlatformFormUiAction
    data object SaveClicked : SocialPlatformFormUiAction
    data class CodeChanged(val value: String) : SocialPlatformFormUiAction
    data class LabelChanged(val locale: String, val value: String) : SocialPlatformFormUiAction
    data class AddLabelLanguage(val locale: String) : SocialPlatformFormUiAction
    data class IconUrlChanged(val value: String) : SocialPlatformFormUiAction
    data class BackgroundColorHexChanged(val value: String) : SocialPlatformFormUiAction
    data class IconTintColorHexChanged(val value: String) : SocialPlatformFormUiAction
    data class SortOrderChanged(val value: String) : SocialPlatformFormUiAction
}
