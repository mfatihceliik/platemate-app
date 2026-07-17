package com.mefy.platemate.presentation.components.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.LayoutDirection

/**
 * İki [PaddingValues]'ı kenar-kenar toplar. start/end, RTL doğruluğu için [layoutDirection] ile
 * çözülür. Düz (composable olmayan) fonksiyon → `remember` bloğu içinde çağrılabilir; bu yüzden
 * [layoutDirection] parametre olarak alınır (local okunamaz).
 *
 * PMBaseScreen bunu, dondurulmuş scaffold inset'i ile çağıranın kozmetik gutter'ını birleştirmek
 * için kullanır.
 */
fun PaddingValues.plus(
    other: PaddingValues,
    layoutDirection: LayoutDirection
): PaddingValues = PaddingValues(
    start = calculateStartPadding(layoutDirection) + other.calculateStartPadding(layoutDirection),
    top = calculateTopPadding() + other.calculateTopPadding(),
    end = calculateEndPadding(layoutDirection) + other.calculateEndPadding(layoutDirection),
    bottom = calculateBottomPadding() + other.calculateBottomPadding()
)
