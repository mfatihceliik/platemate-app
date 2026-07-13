package com.mefy.platemate.presentation.features.admin.hub

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText
import java.util.Locale

@Immutable
data class AdminHubUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val query: String = "",
    val items: List<AdminMenuItemUiModel> = emptyList()
) {
    val filteredItems: List<AdminMenuItemUiModel>
        get() {
            val trimmed = query.trim()
            if (trimmed.isEmpty()) return items
            val needle = trimmed.lowercase(SEARCH_LOCALE)
            return items.filter {
                it.title.lowercase(SEARCH_LOCALE).contains(needle) ||
                    it.code.lowercase(SEARCH_LOCALE).contains(needle)
            }
        }

    private companion object {
        // Başlıklar Türkçe gelebilir; İ/i dönüşümü için TR locale ile küçült.
        val SEARCH_LOCALE: Locale = Locale.forLanguageTag("tr")
    }
}

@Immutable
data class AdminMenuItemUiModel(
    val code: String,
    val title: String,
    val iconKey: String,
    val badgeCount: Long?
)
