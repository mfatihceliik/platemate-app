package com.mefy.platemate.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RecentActivityDto(
    @SerializedName("username") val username: String?,
    @SerializedName("plateCode") val plateCode: String?,
    @SerializedName("actionType") val actionType: String?,
    @SerializedName("occurredAt") val occurredAt: String?,
    @SerializedName("rating") val rating: Double,
    @SerializedName("comment") val comment: String?,
    @SerializedName("reportTypeCode") val reportTypeCode: String?,
    @SerializedName("reportTypeLabel") val reportTypeLabel: String?
)
