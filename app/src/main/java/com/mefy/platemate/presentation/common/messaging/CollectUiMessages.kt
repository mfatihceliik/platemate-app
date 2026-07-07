package com.mefy.platemate.presentation.common.messaging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.mefy.platemate.presentation.common.banner.BannerSeverity
import com.mefy.platemate.presentation.common.text.UiText
import kotlinx.coroutines.flow.Flow

@Composable
fun CollectUiMessages(
    messages: Flow<UiMessage>,
    onShowSnackbar: (UiText, BannerSeverity) -> Unit
) {
    LaunchedEffect(messages) {
        messages.collect { message ->
            when (message) {
                is UiMessage.ShowSnackbar -> onShowSnackbar(message.message, message.severity)
            }
        }
    }
}
