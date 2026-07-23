package com.mefy.platemate.presentation.common.connectivity

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun OfflineBanner(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.error)
                .padding(vertical = spacing.s8),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMIcon(
                imageVector = Icons.Filled.WifiOff,
                tint = colors.onError
            )
            PMText(
                text = stringResource(R.string.connectivity_offline_banner),
                color = colors.onError,
                modifier = Modifier.padding(start = spacing.s8)
            )
        }
    }
}

@Preview(name = "OfflineBannerLight")
@Composable
private fun OfflineBannerLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        OfflineBanner(visible = true)
    }
}


@Preview(name = "OfflineBannerDark")
@Composable
private fun OfflineBannerDarkPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        OfflineBanner(visible = true)
    }
}