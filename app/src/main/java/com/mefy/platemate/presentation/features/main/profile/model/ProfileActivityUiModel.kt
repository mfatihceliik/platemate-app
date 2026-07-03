package com.mefy.platemate.presentation.features.main.profile.model

import androidx.compose.runtime.Immutable

sealed interface ProfileActivityUiModel {
    val id: String
    val createdAtText: String
    val sortKey: String
}