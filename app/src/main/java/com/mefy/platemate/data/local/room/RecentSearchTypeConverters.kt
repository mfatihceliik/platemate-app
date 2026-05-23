package com.mefy.platemate.data.local.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mefy.platemate.data.local.room.model.RecentSearchReportTypeLocal

class RecentSearchTypeConverters {

    private val gson = Gson()

    @TypeConverter
    fun fromReportTypes(types: List<RecentSearchReportTypeLocal>): String = gson.toJson(types)

    @TypeConverter
    fun toReportTypes(value: String?): List<RecentSearchReportTypeLocal> {
        if (value.isNullOrBlank()) return emptyList()
        val listType = object : TypeToken<List<RecentSearchReportTypeLocal>>() {}.type
        return runCatching {
            gson.fromJson<List<RecentSearchReportTypeLocal>>(value, listType).orEmpty()
        }.getOrElse { emptyList() }
    }
}
