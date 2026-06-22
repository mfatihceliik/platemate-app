package com.mefy.platemate.presentation.common.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMBackButton(onBackClick: () -> Unit) {
    PMIconButton(onClick = onBackClick) {
        PMIcon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            size = MaterialTheme.pmDimensions.sizing.avatarIconInner,
            contentDescription = stringResource(R.string.common_back),
        )
    }
}