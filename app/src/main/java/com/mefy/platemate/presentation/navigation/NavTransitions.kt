package com.mefy.platemate.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry

/**
 * Uygulama geneli ekran geçişleri. Tek [androidx.navigation.compose.NavHost]'a
 * varsayılan olarak bağlanır; tüm graph/composable'lara (iç içe dahil) uygulanır.
 *
 * Stil: Material "shared axis X" — küçük mesafe yatay kayma + fade. Tam-genişlik
 * slide yerine kısa mesafe, böylece ağır ilk-kompozisyon karesinde bir kare düşse
 * bile titreme daha az fark edilir ve geçiş daha yumuşak hissedilir.
 *
 * - İleri/geri (push/pop) yığını → küçük kayma + fade.
 * - Top-level sekmeler arası geçiş → kısa mesafe yön-duyarlı kayma + fade
 *   (shared-axis X). Tam-genişlik yerine kısa mesafe: giren ekranın ağır ilk
 *   kompozisyon karesiyle çakışsa bile bütçeyi aşmaz → takılmaz. Navigasyon delay'siz
 *   tetiklenir; alt bardaki gösterge de aynı anda akıcı kayar.
 */
/** Ekran geçiş süresi; [AppShell]'deki bar giriş/çıkış animasyonu da buna senkron çalışır. */
internal const val NAV_TRANSITION_DURATION_MS = 250
private val easing = FastOutSlowInEasing
private fun slideSpec() = tween<IntOffset>(NAV_TRANSITION_DURATION_MS, easing = easing)
private fun fadeSpec() = tween<Float>(NAV_TRANSITION_DURATION_MS, easing = easing)

/** Sekme geçişi yönü: +1 ileri (sağa), -1 geri (sola), 0 sekme geçişi değil. */
private fun AnimatedContentTransitionScope<NavBackStackEntry>.tabDirection(): Int {
    val from = initialState.destination.toTopLevelDestinationOrNull()
    val to = targetState.destination.toTopLevelDestinationOrNull()
    if (from == null || to == null || from == to) return 0
    return if (to.ordinal > from.ordinal) 1 else -1
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.appEnter(): EnterTransition {
    val dir = tabDirection()
    return if (dir != 0) {
        slideInHorizontally(slideSpec()) { (it * dir) / 6 } + fadeIn(fadeSpec())
    } else {
        slideInHorizontally(slideSpec()) { it / 5 } + fadeIn(fadeSpec())
    }
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.appExit(): ExitTransition {
    val dir = tabDirection()
    return if (dir != 0) {
        slideOutHorizontally(slideSpec()) { -(it * dir) / 6 } + fadeOut(fadeSpec())
    } else {
        slideOutHorizontally(slideSpec()) { -it / 8 } + fadeOut(fadeSpec())
    }
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.appPopEnter(): EnterTransition {
    val dir = tabDirection()
    return if (dir != 0) {
        slideInHorizontally(slideSpec()) { (it * dir) / 6 } + fadeIn(fadeSpec())
    } else {
        slideInHorizontally(slideSpec()) { -it / 8 } + fadeIn(fadeSpec())
    }
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.appPopExit(): ExitTransition {
    val dir = tabDirection()
    return if (dir != 0) {
        slideOutHorizontally(slideSpec()) { -(it * dir) / 6 } + fadeOut(fadeSpec())
    } else {
        slideOutHorizontally(slideSpec()) { it / 5 } + fadeOut(fadeSpec())
    }
}
