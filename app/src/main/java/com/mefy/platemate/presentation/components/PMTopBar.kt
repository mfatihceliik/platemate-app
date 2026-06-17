package com.mefy.platemate.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmColors

sealed interface PMTopBarConfig {
    data object Hidden : PMTopBarConfig

    data class Simple(
        val title: String,
        val onBackClick: () -> Unit
    ) : PMTopBarConfig

    data class WithActions(
        val title: String,
        val onBackClick: () -> Unit,
        val actions: @Composable RowScope.() -> Unit
    ) : PMTopBarConfig

    data class Custom(
        val content: @Composable () -> Unit
    ) : PMTopBarConfig
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PMTopBar(
    config: PMTopBarConfig,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface
) {
    when (config) {
        is PMTopBarConfig.Hidden -> {}
        is PMTopBarConfig.Simple -> {
            TopAppBar(
                title = {
                    PMText(
                        text = config.title,
                        style = PMTextStyle.Title,
                        color = MaterialTheme.pmColors.textPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = config.onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.common_back),
                            tint = MaterialTheme.pmColors.textPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = containerColor
                ),
                modifier = modifier
            )
        }
        is PMTopBarConfig.WithActions -> {
            TopAppBar(
                title = {
                    PMText(
                        text = config.title,
                        style = PMTextStyle.Title,
                        color = MaterialTheme.pmColors.textPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = config.onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.common_back),
                            tint = MaterialTheme.pmColors.textPrimary
                        )
                    }
                },
                actions = config.actions,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = containerColor
                ),
                modifier = modifier
            )
        }
        is PMTopBarConfig.Custom -> {
            config.content()
        }
    }
}
