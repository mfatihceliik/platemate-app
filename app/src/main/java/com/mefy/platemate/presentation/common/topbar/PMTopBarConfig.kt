package com.mefy.platemate.presentation.common.topbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable


sealed interface PMTopBarConfig {
    data object Hidden : PMTopBarConfig
    data class Standard(
        val title: String,
        val onBackClick: (() -> Unit)? = null,
        val alignment: PMTopBarAlignment = PMTopBarAlignment.Center,
        val actions: (@Composable RowScope.() -> Unit)? = null,
    ) : PMTopBarConfig
    data class Custom(
        val content: @Composable () -> Unit
    ) : PMTopBarConfig
    data class Transparent(
        val title: String? = null,
        val onBackClick: (() -> Unit)? = null,
        val actions: (@Composable RowScope.() -> Unit)? = null,
        val hideOnScroll: Boolean = true
    ) : PMTopBarConfig
}

enum class PMTopBarAlignment { Start, Center }