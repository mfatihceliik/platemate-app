package com.mefy.platemate.presentation.common.topbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

/** Başlık hizalaması: ortada (default) veya solda. */
enum class PMTopBarAlignment { Start, Center }

sealed interface PMTopBarConfig {
    data object Hidden : PMTopBarConfig

    /**
     * Standart üst bar: başlık + opsiyonel geri butonu + opsiyonel aksiyonlar.
     * [onBackClick] null ise geri butonu render edilmez (tab/ana ekranlar için).
     * [alignment] başlığın yatay hizasını belirler (default ortada).
     */
    data class Standard(
        val title: String,
        val onBackClick: (() -> Unit)? = null,
        val alignment: PMTopBarAlignment = PMTopBarAlignment.Center,
        val actions: (@Composable RowScope.() -> Unit)? = null,
    ) : PMTopBarConfig

    /** Tam serbest üst bar; ekran kendi composable'ını verir. */
    data class Custom(
        val content: @Composable () -> Unit
    ) : PMTopBarConfig
}
