package com.mefy.platemate.presentation.features.main.search

sealed interface SearchUiEffect {
    data class NavigateToSearchDetail(val id: String) : SearchUiEffect
}
