package com.mefy.platemate.presentation.features.main.settings.premium

import com.mefy.platemate.presentation.common.messaging.HandleUiMessages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PremiumInfoRoute(
    viewModel: PremiumInfoViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)
    PremiumInfoScreen(
        state = state,
        onBackClick = onBackClick,
        onRetry = viewModel::retry,
        modifier = modifier
    )
}
