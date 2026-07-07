package com.mefy.platemate.presentation.features.admin.premiumfeatures

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.messaging.HandleUiMessages
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMIconButton
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PremiumFeaturesRoute(
    viewModel: PremiumFeaturesViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToForm: (featureId: Long?) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.refresh() }

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                PremiumFeaturesUiEffect.NavigateBack -> onNavigateBack()
                is PremiumFeaturesUiEffect.NavigateToForm -> onNavigateToForm(effect.featureId)
            }
        }
    }

    val status = when {
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        state.isLoading -> ScreenStatus.Loading
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.admin_premium_features_title),
            onBackClick = { viewModel.onAction(PremiumFeaturesUiAction.BackClicked) },
            actions = {
                PMIconButton(onClick = { viewModel.onAction(PremiumFeaturesUiAction.AddClicked) }) {
                    PMIcon(imageVector = Icons.Filled.Add, contentDescription = stringResource(R.string.admin_premium_feature_add))
                }
            }
        ),
        status = status,
        keepTopBarWhileLoading = true,
        onRetry = { viewModel.onAction(PremiumFeaturesUiAction.RetryClicked) },
        loading = { p -> PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(p)) }
    ) { contentPadding ->
        PremiumFeaturesScreen(state = state, onAction = viewModel::onAction, contentPadding = contentPadding)
    }
}
