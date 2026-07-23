package com.mefy.platemate.presentation.app.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.mefy.platemate.presentation.app.viewmodel.AppViewModel
import com.mefy.platemate.presentation.common.dialog.DialogFactory
import com.mefy.platemate.presentation.common.dialog.DialogHostState
import com.mefy.platemate.presentation.common.global.GlobalAppEvent
import com.mefy.platemate.presentation.navigation.navigateToAuthAndClearBackStack
import androidx.navigation.NavHostController

@Composable
internal fun AppUiEffects(
    navController: NavHostController,
    dialogHostState: DialogHostState,
    viewModel: AppViewModel = hiltViewModel()
) {
    LaunchedEffect(viewModel) {
        viewModel.globalUiEvents.collect { event ->
            when (event) {
                GlobalAppEvent.SessionExpired -> dialogHostState.showDialog(
                    DialogFactory.sessionExpiredDialog(
                        onConfirm = { navController.navigateToAuthAndClearBackStack() }
                    )
                )
            }
        }
    }
}
