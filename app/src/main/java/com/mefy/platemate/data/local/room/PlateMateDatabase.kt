package com.mefy.platemate.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mefy.platemate.data.local.room.dao.AlarmPlateDao
import com.mefy.platemate.data.local.room.dao.ChatMessageDao
import com.mefy.platemate.data.local.room.dao.ChatRoomDao
import com.mefy.platemate.data.local.room.dao.RecentSearchDao
import com.mefy.platemate.data.local.room.dao.SavedPlateDao
import com.mefy.platemate.data.local.room.entity.AlarmPlateEntity
import com.mefy.platemate.data.local.room.entity.ChatMessageEntity
import com.mefy.platemate.data.local.room.entity.ChatRoomEntity
import com.mefy.platemate.data.local.room.entity.RecentSearchEntity
import com.mefy.platemate.data.local.room.entity.SavedPlateEntity

@Database(
    entities = [
        RecentSearchEntity::class,
        SavedPlateEntity::class,
        AlarmPlateEntity::class,
        ChatRoomEntity::class,
        ChatMessageEntity::class
    ],
    version = 8,
    exportSchema = false
)
@TypeConverters(RecentSearchTypeConverters::class)
abstract class PlateMateDatabase : RoomDatabase() {
    abstract fun recentSearchDao(): RecentSearchDao
    abstract fun savedPlateDao(): SavedPlateDao
    abstract fun alarmPlateDao(): AlarmPlateDao
    abstract fun chatRoomDao(): ChatRoomDao
    abstract fun chatMessageDao(): ChatMessageDao

    companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
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
                db.execSQL(
                    """
                    CREATE UNIQUE INDEX IF NOT EXISTS `index_saved_plates_user_id_normalized_plate_code`
                    ON `saved_plates` (`user_id`, `normalized_plate_code`)
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS `index_saved_plates_user_id_saved_at`
                    ON `saved_plates` (`user_id`, `saved_at`)
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `alarm_plates` (
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
                db.execSQL(
                    """
                    CREATE UNIQUE INDEX IF NOT EXISTS `index_alarm_plates_user_id_normalized_plate_code`
                    ON `alarm_plates` (`user_id`, `normalized_plate_code`)
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS `index_alarm_plates_user_id_saved_at`
                    ON `alarm_plates` (`user_id`, `saved_at`)
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `chat_rooms` (
                        `owner_user_id` INTEGER NOT NULL,
                        `room_id` INTEGER NOT NULL,
                        `room_name` TEXT,
                        `last_message_at` TEXT,
                        `last_message_content` TEXT,
                        `other_participant_name` TEXT,
                        `other_participant_id` INTEGER,
                        `is_group` INTEGER NOT NULL,
                        `request_status` TEXT NOT NULL,
                        `initiator_id` INTEGER,
                        PRIMARY KEY(`owner_user_id`, `room_id`)
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS `index_chat_rooms_owner_user_id_last_message_at`
                    ON `chat_rooms` (`owner_user_id`, `last_message_at`)
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `chat_messages` (
                        `owner_user_id` INTEGER NOT NULL,
                        `message_id` INTEGER NOT NULL,
                        `chat_room_id` INTEGER,
                        `sender_user_id` INTEGER,
                        `sender_username` TEXT NOT NULL,
                        `content` TEXT NOT NULL,
                        `sent_at` TEXT,
                        `is_read` INTEGER NOT NULL,
                        `status` TEXT NOT NULL,
                        `delivered_at` TEXT,
                        `read_at` TEXT,
                        PRIMARY KEY(`owner_user_id`, `message_id`)
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS `index_chat_messages_owner_user_id_chat_room_id_sent_at`
                    ON `chat_messages` (`owner_user_id`, `chat_room_id`, `sent_at`)
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `chat_rooms` ADD COLUMN `unread_count` INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        val MIGRATION_5_6: Migration = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `chat_rooms` ADD COLUMN `last_message_sender_id` INTEGER"
                )
            }
        }

        val MIGRATION_6_7: Migration = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `chat_messages` ADD COLUMN `client_message_id` TEXT"
                )
            }
        }

        val MIGRATION_7_8: Migration = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `chat_messages` ADD COLUMN `reply_to_message_id` INTEGER"
                )
                db.execSQL(
                    "ALTER TABLE `chat_messages` ADD COLUMN `reply_to_sender_username` TEXT"
                )
                db.execSQL(
                    "ALTER TABLE `chat_messages` ADD COLUMN `reply_to_content_preview` TEXT"
                )
            }
        }
    }
}
