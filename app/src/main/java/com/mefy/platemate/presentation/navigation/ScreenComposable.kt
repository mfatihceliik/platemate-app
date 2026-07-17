package com.mefy.platemate.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mefy.platemate.presentation.common.messaging.HandleUiMessages
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel

/** [screenComposable] içerik lambdasının alıcısı; rota argümanı gereken ekranlar için giriş verir. */
class ScreenScope(val backStackEntry: NavBackStackEntry)

/**
 * Tip-güvenli bir [composable] hedefi kaydeder ve ekran ViewModel'inin
 * [BaseViewModel.uiMessages] akışını kök banner/dialog sink'ine TEK yerde bağlar; böylece rotalar
 * artık `HandleUiMessages`'i kendileri çağırmaz.
 *
 * [viewModel] VM'i üretir/alır (düz, parent-scoped veya argümana bağlı ediniml destekler).
 * [content] VM'i `it` olarak alır; rota argümanları gerektiğinde `backStackEntry.toRoute<D>()`.
 *
 * Not: yalnız [D] `reified` olmalı ([composable] için); [VM] [BaseViewModel] ile sınırlı sade tip
 * parametresi. Helper hedefe-özel geçiş animasyonu iletmez (geçişler [AppNavHost]'ta global) — özel
 * geçiş isteyen ekran düz [composable] kullanır.
 */
inline fun <reified D : Any, VM : BaseViewModel> NavGraphBuilder.screenComposable(
    crossinline viewModel: @Composable (NavBackStackEntry) -> VM,
    crossinline content: @Composable ScreenScope.(VM) -> Unit,
) {
    composable<D> { backStackEntry ->
        val vm = viewModel(backStackEntry)
        HandleUiMessages(vm.uiMessages)
        ScreenScope(backStackEntry).content(vm)
    }
}
