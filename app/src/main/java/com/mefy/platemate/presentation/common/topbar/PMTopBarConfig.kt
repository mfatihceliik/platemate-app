package com.mefy.platemate.presentation.common.topbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable

sealed interface PMTopBarConfig {
    data object Hidden : PMTopBarConfig

    /**
     * Standart üst bar: başlık + opsiyonel geri butonu + opsiyonel aksiyonlar.
     * [onBackClick] null ise geri butonu render edilmez (tab/ana ekranlar için).
     */
    data class Standard(
        val title: String,
        val onBackClick: (() -> Unit)? = null,
        val actions: (@Composable RowScope.() -> Unit)? = null,
    ) : PMTopBarConfig

    data class Collapsing @OptIn(ExperimentalMaterial3Api::class) constructor(
        val title: String,
        val scrollBehavior: TopAppBarScrollBehavior,
        val onBackClick: (() -> Unit)? = null,
        val actions: (@Composable RowScope.() -> Unit)? = null,
    ) : PMTopBarConfig

    /** Şeffaf üst bar; içeriğin üzerine biner (hero overlay). */
    data class Transparent(
        val onBackClick: (() -> Unit)? = null,
        val actions: (@Composable RowScope.() -> Unit)? = null,
    ) : PMTopBarConfig

    data class Custom(
        val content: @Composable () -> Unit
    ) : PMTopBarConfig

}