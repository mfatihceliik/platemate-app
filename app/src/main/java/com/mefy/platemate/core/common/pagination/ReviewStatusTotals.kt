package com.mefy.platemate.core.common.pagination

import com.google.gson.annotations.SerializedName

data class ReviewStatusTotals(
    @SerializedName("approved") val approved: Long = 0L,
    @SerializedName("pendingReview") val pendingReview: Long = 0L,
    @SerializedName("rejected") val rejected: Long = 0L,
    @SerializedName("removedByUser") val removedByUser: Long = 0L,
    @SerializedName("removedByModerator") val removedByModerator: Long = 0L,
    @SerializedName("removedByLegalRequest") val removedByLegalRequest: Long = 0L
)
