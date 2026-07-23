package com.mefy.platemate.presentation.features.main.scanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.dialog.DialogFactory
import com.mefy.platemate.presentation.app.providers.LocalNavController
import com.mefy.platemate.presentation.app.providers.LocalUiMessageHandlers
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.common.basescreen.PMBaseScreen
import com.mefy.platemate.presentation.common.ext.findActivity

@Composable
fun CameraScannerRoute(
    modifier: Modifier = Modifier,
    viewModel: ScannerViewModel,
    onPlateFound: (String) -> Unit
) {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val uiMessageHandlers = LocalUiMessageHandlers.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
            if (!granted) {
                val activity = context.findActivity()
                val permanentlyDenied = activity != null &&
                    !ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)
                if (permanentlyDenied) {
                    uiMessageHandlers.onShowDialog(
                        DialogFactory.cameraPermissionDeniedDialog(
                            onOpenSettings = {
                                context.startActivity(
                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                        data = Uri.fromParts("package", context.packageName, null)
                                    }
                                )
                                navController.navigateUp()
                            },
                            onDismiss = { navController.navigateUp() }
                        )
                    )
                } else {
                    navController.navigateUp()
                }
            }
        }
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.onAction(ScannerUiAction.OnGalleryImageSelected(uri))
            }
        }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            viewModel.onAction(ScannerUiAction.OnScanStarted)
        }
    }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ScannerUiEffect.PlateFound -> {
                    onPlateFound(effect.plate)
                }
            }
        }
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.camera_scanner_screen),
            onBackClick = { navController.navigateUp() }
        ),
    ) { innerPadding ->
        CameraScannerScreen(
            modifier = modifier,
            viewModel = viewModel,
            hasCameraPermission = hasCameraPermission,
            onGalleryClick = {
                galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        )
    }
}