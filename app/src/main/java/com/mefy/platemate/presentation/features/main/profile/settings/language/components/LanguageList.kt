package com.mefy.platemate.presentation.features.main.profile.settings.language.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mefy.platemate.domain.model.language.AppLanguage
import com.mefy.platemate.presentation.features.main.profile.settings.language.LanguageItem
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun LanguageList(
    languages: List<LanguageItem>,
    selectedLanguage: AppLanguage?,
    onSelect: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.large)
            .border(dims.stroke.st1, colors.cardBorder, MaterialTheme.shapes.large)
    ) {
        languages.forEachIndexed { index, item ->
            LanguageRow(
                item = item,
                isSelected = item.language == selectedLanguage,
                onSelect = { item.language?.let(onSelect) },
                showDivider = index < languages.lastIndex
            )
        }
    }
}
