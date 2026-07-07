package com.mefy.platemate.data.remote.dto.premium

import com.google.gson.annotations.SerializedName

data class PremiumCatalogDto(
    @SerializedName("plans") val plans: List<PremiumPlanDto>?,
    @SerializedName("features") val features: List<PremiumFeatureDto>?
)
