package com.mefy.platemate.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.min

fun spacedByWithFooter(space: Dp = 0.dp) = object : Arrangement.Vertical {

    override val spacing = space

    override fun Density.arrange(
        totalSize: Int,
        sizes: IntArray,
        outPositions: IntArray,
    ) {
        if (sizes.isEmpty()) return
        val spacePx = space.roundToPx()

        var occupied = 0
        var lastSpace = 0

        sizes.forEachIndexed { index, size ->

            if (index == sizes.lastIndex) {
                outPositions[index] = totalSize - size
            } else {
                outPositions[index] = min(occupied, totalSize - size)
            }
            lastSpace = min(spacePx, totalSize - outPositions[index] - size)
            occupied = outPositions[index] + size + lastSpace
        }
        occupied -= lastSpace
    }
}

fun spacedByWithFooter(
    itemSpacing: Dp = 0.dp,
    footerTopPadding: Dp = 16.dp, // Butonun üstündeki elemanla olması gereken minimum boşluk
    footerBottomPadding: Dp = 8.dp
) = object : Arrangement.Vertical {

    override val spacing: Dp = itemSpacing

    override fun Density.arrange(
        totalSize: Int,
        sizes: IntArray,
        outPositions: IntArray,
    ) {
        if (sizes.isEmpty()) return

        val spacePx = itemSpacing.roundToPx()
        val topPaddingPx = footerTopPadding.roundToPx()
        val bottomPaddingPx = footerBottomPadding.roundToPx()

        var currentOffset = 0

        sizes.forEachIndexed { index, size ->
            if (index == sizes.lastIndex) {
                val footerPosition = totalSize - size - bottomPaddingPx

                // Kritik Değişiklik: Eğer içerik uzayıp butona dayanırsa,
                // üstteki eleman ile buton arasında en az 'topPaddingPx' kadar boşluk kalmasını zorunlu kılıyoruz.
                outPositions[index] = max(currentOffset + topPaddingPx, footerPosition)
            } else {
                outPositions[index] = currentOffset
                currentOffset += size + spacePx
            }
        }
    }
}


/*val verticalArrangement  = remember {
    object : Arrangement.Vertical {
        override fun Density.arrange(
            totalSize: Int,
            sizes: IntArray,
            outPositions: IntArray
        ) {
            var currentOffset = 0
            sizes.forEachIndexed { index, size ->
                if (index == sizes.lastIndex) {
                    outPositions[index] = totalSize - size
                } else {
                    outPositions[index] = currentOffset
                    currentOffset += size
                }
            }
        }
    }
}*/