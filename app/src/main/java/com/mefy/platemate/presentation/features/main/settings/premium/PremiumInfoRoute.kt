package com.mefy.platemate.presentation.features.main.settings.premium

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.common.basescreen.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator

@Composable
fun PremiumInfoRoute(
    modifier: Modifier = Modifier,
    viewModel: PremiumInfoViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val status = when {
        state.isLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_premium_title)
        ),
        status = status,
        onRetry = viewModel::retry,
        loading = { innerPadding -> PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(innerPadding)) },
    ) { innerPadding ->
        PremiumInfoScreen(
            modifier = modifier,
            state = state,
            innerPadding = innerPadding
        )
    }
}
