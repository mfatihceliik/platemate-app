package com.mefy.platemate.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp

/**
 * App seviyesindeki tek [androidx.compose.material3.Scaffold]'un `innerPadding`'ini
 * ekran ağacına taşır. AppNavHost sağlar, PMBaseScreen tüketir.
 *
 * Genelde sadece alt boşluk (görünür durumdaki MainBottomBar yüksekliği) taşır;
 * status-bar inset'ini PMTopBar, navigation-bar inset'ini MainBottomBar kendi yönetir.
 */
val LocalScaffoldPadding = compositionLocalOf { PaddingValues(0.dp) }
