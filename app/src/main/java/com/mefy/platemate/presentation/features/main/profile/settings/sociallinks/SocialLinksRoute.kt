package com.mefy.platemate.presentation.features.main.profile.settings.sociallinks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.presentation.common.text.resolve
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SocialLinksRoute(
    viewModel: SocialLinksViewModel,
    onBackClick: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is SocialLinksUiEffect.ShowSnackbar -> onShowSnackbar(effect.message.resolve(context))
            }
        }
    }

    SocialLinksScreen(
        state = state,
        onAction = viewModel::onAction,
        onBackClick = onBackClick,
        modifier = modifier
    )
}
