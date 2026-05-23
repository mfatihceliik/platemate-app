package com.mefy.platemate.core.common.pagination

import com.google.gson.annotations.SerializedName

data class PaginationMeta(
    @SerializedName("page") val page: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("totalElements") val totalElements: Long,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("hasNext") val hasNext: Boolean,
    @SerializedName("hasPrevious") val hasPrevious: Boolean
)