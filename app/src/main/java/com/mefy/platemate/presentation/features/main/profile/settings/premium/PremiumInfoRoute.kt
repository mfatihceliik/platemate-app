package com.mefy.platemate.presentation.features.main.profile.settings.premium

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
    PremiumInfoScreen(
        state = state,
        onBackClick = onBackClick,
        modifier = modifier
    )
}
