package com.mefy.platemate.core.common.pagination

import com.google.gson.annotations.SerializedName

data class PagedResult<T>(
    @SerializedName("items") val items: List<T>,
    @SerializedName("meta") val meta: PaginationMeta
)
