package com.mefy.platemate.presentation.features.admin.hub

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.theme.pmColors
import androidx.compose.material3.MaterialTheme

/**
 * AdminHub'ın ViewModel'i yok; saf navigasyon callback'leri taşır. PMBaseScreen
 * iskeletini diğer admin ekranlarıyla tutarlı olacak şekilde Route sağlar; Screen
 * yalnızca içerik (satır listesi) çizer.
 */
@Composable
fun AdminHubRoute(
    onBackClick: () -> Unit,
    onPendingComments: () -> Unit,
    onCommentReports: () -> Unit,
    onPlateRemoval: () -> Unit,
    onHiddenPlates: () -> Unit,
    onReportTypes: () -> Unit,
    onSocialPlatforms: () -> Unit,
    onSettings: () -> Unit,
) {
    PMBaseScreen(
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.admin_panel_title),
            onBackClick = onBackClick
        ),
    ) { contentPadding ->
        AdminHubScreen(
            onPendingComments = onPendingComments,
            onCommentReports = onCommentReports,
            onPlateRemoval = onPlateRemoval,
            onHiddenPlates = onHiddenPlates,
            onReportTypes = onReportTypes,
            onSocialPlatforms = onSocialPlatforms,
            onSettings = onSettings,
            contentPadding = contentPadding
        )
    }
}
