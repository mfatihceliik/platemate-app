package com.mefy.platemate.domain.model.admin

data class PremiumFeatureAdmin(
    val id: Long,
    val iconKey: String,
    val titles: Map<String, String>,
    val subtitles: Map<String, String>?,
    val sortOrder: Int,
    val active: Boolean
)

/** Editable payload shared by add and update. */
data class PremiumFeatureInput(
    val iconKey: String,
    val titles: Map<String, String>,
    val subtitles: Map<String, String>?,
    val sortOrder: Int
)
