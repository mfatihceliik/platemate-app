package com.mefy.platemate.presentation.common.banner

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
class InAppBannerController {

    var current by mutableStateOf<InAppBannerUiModel?>(null)
        private set

    var resetKey by mutableIntStateOf(0)
        private set

    fun show(banner: InAppBannerUiModel) {
        current = banner
        resetKey++
    }

    fun dismiss() {
        current = null
    }
}
