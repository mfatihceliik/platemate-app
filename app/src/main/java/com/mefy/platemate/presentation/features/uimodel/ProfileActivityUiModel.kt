package com.mefy.platemate.presentation.features.uimodel

sealed interface ProfileActivityUiModel {
    val id: String
    val createdAtText: String
    val sortKey: String
}