package com.mefy.platemate.data.remote.dto.admin

import com.google.gson.annotations.SerializedName

/**
 * Shared review request for comment reports and plate removal requests.
 * Backend resolves the target status by statusCode.
 */
data class AdminReviewRequest(
    @SerializedName("statusCode") val statusCode: String,
    @SerializedName("adminNote") val adminNote: String?
)
