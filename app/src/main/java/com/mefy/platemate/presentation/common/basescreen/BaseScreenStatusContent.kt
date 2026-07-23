package com.mefy.platemate.presentation.common.basescreen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mefy.platemate.presentation.common.state.ScreenStatus

/** [status]'e göre yükleme/boş/içerik/hata'dan birini tek [Crossfade] ile çizer (durum
 * geçişlerinde titreme olmasın). Hata her zaman [PMErrorState] + [onRetry] ile çizilir. */
@Composable
internal fun BaseScreenStatusContent(
    status: ScreenStatus,
    padding: PaddingValues,
    onRetry: () -> Unit,
    loading: @Composable (PaddingValues) -> Unit,
    empty: @Composable (PaddingValues) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Crossfade(targetState = status, label = "screen_status") { current ->
        when (current) {
            ScreenStatus.Loading -> loading(padding)
            ScreenStatus.Empty -> empty(padding)
            ScreenStatus.Content -> content(padding)
            is ScreenStatus.Error -> PMErrorState(
                message = current.message,
                onRetry = onRetry,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
