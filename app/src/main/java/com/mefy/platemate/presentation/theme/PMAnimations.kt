package com.mefy.platemate.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
data class PMAnimations(
    val flash: Int = 150,
    val fast: Int = 250,
    val normal: Int = 350,
    val slow: Int = 500,
    val extraSlow: Int = 700,
    val longDuration1: Int = 1000,
    val longDuration2: Int = 1500,
    val longDuration3: Int = 2000,
    val longDuration4: Int = 3000,
    val longDuration5: Int = 4000,
    val longDuration6: Int = 4500,
    
    // Semantic durations
    val buttonRipple: Int = 200,
    val screenTransition: Int = 250,
    val reticleFade: Int = 500,
    val successDelay: Long = 1000L,
    val debounceSearch: Long = 300L,
    val debounceAction: Long = 400L
)
val LocalAnimations = staticCompositionLocalOf<PMAnimations> {
    error("No Animations Provided")
}
