package com.mefy.platemate.data.remote.dto.admin

import com.google.gson.annotations.SerializedName

/**
 * Shared body for adding and updating a plate report type.
 * Backend resolves severity by severityCode (RED / YELLOW).
 */
data class PlateReportTypeRequest(
    @SerializedName("code") val code: String,
    @SerializedName("label") val label: String,
    @SerializedName("description") val description: String,
    @SerializedName("iconKey") val iconKey: String,
    @SerializedName("severityCode") val severityCode: String,
    @SerializedName("colorHex") val colorHex: String,
    @SerializedName("weight") val weight: Int,
    @SerializedName("sortOrder") val sortOrder: Int
)

data class UpdateReportTypeActiveRequest(
    @SerializedName("active") val active: Boolean
)
