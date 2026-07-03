package com.mefy.platemate.presentation.features.main.settings.premium

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import com.mefy.platemate.presentation.common.messaging.HandleUiMessages
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PremiumInfoRoute(
    viewModel: PremiumInfoViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)

    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    val status = when {
        state.isLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_premium_title),
            onBackClick = onBackClick
        ),
        status = status,
        onRetry = viewModel::retry,
        loading = { innerPadding -> PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(innerPadding)) },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .background(colors.star, MaterialTheme.shapes.medium)
                        .debouncedClickable { /* TODO: Purchase flow */ },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PMIcon(
                        imageVector = Icons.Filled.Star,
                        tint = Color.White,
                        size = dims.sizing.iconMd,
                    )
                    PMText(
                        text = stringResource(R.string.profile_premium_upgrade),
                        style = PMTextStyle.Body,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(start = dims.spacing.s8)
                    )
                }
            }
        }
    ) { innerPadding ->
        PremiumInfoScreen(
            modifier = modifier,
            state = state,
            innerPadding = innerPadding
        )
    }
}
