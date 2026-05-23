package com.mefy.platemate.presentation.common.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.mefy.platemate.presentation.common.text.UiText
import kotlinx.coroutines.flow.Flow

@Composable
fun CollectCommonUiEvents(
    events: Flow<CommonUiEvent>,
    onShowSnackbar: (UiText) -> Unit,
    onShowDialog: (CommonDialogModel) -> Unit
) {
    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                is CommonUiEvent.ShowSnackbar -> onShowSnackbar(event.message)
                is CommonUiEvent.ShowDialog -> onShowDialog(event.dialog)
            }
        }
    }
}
