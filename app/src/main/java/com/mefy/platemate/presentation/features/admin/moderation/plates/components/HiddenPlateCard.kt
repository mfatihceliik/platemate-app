package com.mefy.platemate.presentation.features.admin.moderation.plates.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.moderation.plates.HiddenPlateUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun HiddenPlateCard(
    model: HiddenPlateUiModel,
    isActioning: Boolean,
    onRestore: () -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surfaceVariant, RoundedCornerShape(dims.radius.r12))
            .padding(dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            PMText(text = model.plateCode, style = PMTextStyle.Body, fontWeight = FontWeight.Bold, color = colors.textPrimary)
            PMText(text = model.statusCode, style = PMTextStyle.Note, color = colors.primary)
        }
        if (model.hiddenReason.isNotBlank()) {
            PMText(text = model.hiddenReason, style = PMTextStyle.Body, color = colors.textPrimary)
        }
        PMText(
            text = stringResource(R.string.admin_comment_report_count, model.reportCount),
            style = PMTextStyle.Note,
            color = colors.textLabel
        )
        PMButton(
            text = stringResource(R.string.admin_plate_restore),
            onClick = onRestore,
            enabled = !isActioning,
            loading = isActioning,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private val hiddenPlateCardPreviewModel = HiddenPlateUiModel(
    id = 1L,
    plateCode = "34 EK 0682",
    statusCode = "HIDDEN",
    hiddenReason = "Çok sayıda şikayet aldı.",
    reportCount = 7
)

@Preview(name = "HiddenPlateCard Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun HiddenPlateCardLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        HiddenPlateCard(model = hiddenPlateCardPreviewModel, isActioning = false, onRestore = {})
    }
}

@Preview(name = "HiddenPlateCard Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun HiddenPlateCardDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        HiddenPlateCard(model = hiddenPlateCardPreviewModel, isActioning = false, onRestore = {})
    }
}