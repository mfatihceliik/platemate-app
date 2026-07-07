package com.mefy.platemate.domain.model.theme

/** Backend-driven accent palette: grid column count + ordered `#RRGGBB` hex strings. */
data class AccentColorCatalog(
    val gridSize: Int,
    val colors: List<String>
)
