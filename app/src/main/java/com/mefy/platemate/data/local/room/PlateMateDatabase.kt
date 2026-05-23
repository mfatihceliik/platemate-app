package com.mefy.platemate.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mefy.platemate.data.local.room.dao.RecentSearchDao
import com.mefy.platemate.data.local.room.dao.SavedPlateDao
import com.mefy.platemate.data.local.room.entity.RecentSearchEntity
import com.mefy.platemate.data.local.room.entity.SavedPlateEntity

@Database(
    entities = [RecentSearchEntity::class, SavedPlateEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(RecentSearchTypeConverters::class)
abstract class PlateMateDatabase : RoomDatabase() {
    abstract fun recentSearchDao(): RecentSearchDao
    abstract fun savedPlateDao(): SavedPlateDao

    companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `saved_plates` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `user_id` INTEGER NOT NULL,
                        `normalized_plate_code` TEXT NOT NULL,
                        `formatted_plate_code` TEXT NOT NULL,
                        `city_name` TEXT,
                        `rating_average` REAL NOT NULL,
                        `comment_count` INTEGER NOT NULL,
                        `report_types` TEXT NOT NULL,
                        `saved_at` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                database.execSQL(
                    """
                    CREATE UNIQUE INDEX IF NOT EXISTS `index_saved_plates_user_id_normalized_plate_code`
                    ON `saved_plates` (`user_id`, `normalized_plate_code`)
                    """.trimIndent()
                )
                database.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS `index_saved_plates_user_id_saved_at`
                    ON `saved_plates` (`user_id`, `saved_at`)
                    """.trimIndent()
                )
            }
        }
    }
}
