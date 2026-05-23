package com.mefy.platemate.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mefy.platemate.data.local.room.model.RecentSearchReportTypeLocal

@Entity(
    tableName = "recent_searches",
    indices = [
        Index(value = ["user_id", "normalized_plate_code"], unique = true),
        Index(value = ["user_id", "searched_at"])
    ]
)
data class RecentSearchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "normalized_plate_code")
    val normalizedPlateCode: String,
    @ColumnInfo(name = "formatted_plate_code")
    val formattedPlateCode: String,
    @ColumnInfo(name = "city_name")
    val cityName: String?,
    @ColumnInfo(name = "rating_average")
    val ratingAverage: Double,
    @ColumnInfo(name = "comment_count")
    val commentCount: Long,
    @ColumnInfo(name = "report_types")
    val reportTypes: List<RecentSearchReportTypeLocal>,
    @ColumnInfo(name = "searched_at")
    val searchedAt: Long
)
