package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.theme.ThemeCatalogDto
import com.mefy.platemate.domain.model.theme.AccentColorCatalog
import javax.inject.Inject

class ThemeCatalogMapper @Inject constructor() : Mapper<ThemeCatalogDto, AccentColorCatalog> {
    override fun map(input: ThemeCatalogDto): AccentColorCatalog {
        val colors = input.colors.orEmpty()
            .sortedBy { it.sortOrder ?: 0 }
            .mapNotNull { it.hex?.takeIf { hex -> hex.isNotBlank() } }
        return AccentColorCatalog(
            gridSize = (input.gridSize ?: 4).coerceIn(1, 8),
            colors = colors
        )
    }
}
