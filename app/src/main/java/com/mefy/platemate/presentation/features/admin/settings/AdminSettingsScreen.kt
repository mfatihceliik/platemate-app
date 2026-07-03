package com.mefy.platemate.presentation.features.admin.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMTextField
import com.mefy.platemate.presentation.features.main.settings.components.SectionLabel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun AdminSettingsScreen(
    state: AdminSettingsUiState,
    onAction: (AdminSettingsUiAction) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions

    val onSaveClicked = remember(onAction) { { onAction(AdminSettingsUiAction.SaveClicked) } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
            .padding(dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {

        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            item {
                SectionLabel(text = stringResource(R.string.admin_settings_follow_limit))
                NumberField(value = state.followLimit, onValueChange = { onAction(AdminSettingsUiAction.FollowLimitChanged(it)) })
            }

            item {
                SectionLabel(text = stringResource(R.string.admin_settings_alarm_limit))
                NumberField(value = state.alarmLimit, onValueChange = { onAction(AdminSettingsUiAction.AlarmLimitChanged(it)) })
            }

            item {
                SectionLabel(text = stringResource(R.string.admin_settings_message_limit))
                NumberField(value = state.messageLimit, onValueChange = { onAction(AdminSettingsUiAction.MessageLimitChanged(it)) })
            }

            item {
                SectionLabel(text = stringResource(R.string.admin_settings_report_threshold))
                NumberField(value = state.reportThreshold, onValueChange = { onAction(AdminSettingsUiAction.ReportThresholdChanged(it)) }, imeAction = ImeAction.Done)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dims.spacing.s8),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            if (state.errorMessage == null && !state.isLoading) {
                PMButton(
                    text = stringResource(R.string.common_save),
                    onClick = onSaveClicked,
                    enabled = state.isSaveEnabled,
                    loading = state.isSaving,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun NumberField(
    value: String,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Next
) {
    PMTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = imeAction),
        modifier = Modifier.fillMaxWidth()
    )
}

private val adminSettingsPreviewState = AdminSettingsUiState(
    isLoading = false,
    followLimit = "50",
    alarmLimit = "10",
    messageLimit = "100",
    reportThreshold = "5"
)

@Preview(name = "AdminSettings Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun AdminSettingsScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        AdminSettingsScreen(state = adminSettingsPreviewState, onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}

@Preview(name = "AdminSettings Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun AdminSettingsScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        AdminSettingsScreen(state = adminSettingsPreviewState, onAction = {}, contentPadding = PaddingValues(0.dp))
    }
}

@Preview(name = "NumberField", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun NumberFieldPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        NumberField(value = "50", onValueChange = {})
    }
}
