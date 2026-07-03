package com.mefy.platemate.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

/**
 * Sohbet odasının yerel kopyası (tek doğru kaynak). Her satır bir kullanıcıya aittir
 * ([ownerUserId]) — çok hesaplı cihazda veriler karışmasın diye (SavedPlate deseni).
 */
@Entity(
    tableName = "chat_rooms",
    primaryKeys = ["owner_user_id", "room_id"],
    indices = [Index(value = ["owner_user_id", "last_message_at"])]
)
data class ChatRoomEntity(
    @ColumnInfo(name = "owner_user_id")
    val ownerUserId: Long,
    @ColumnInfo(name = "room_id")
    val roomId: Long,
    @ColumnInfo(name = "room_name")
    val roomName: String?,
    // ISO-8601 string; sıralama sözlüksel olarak doğru çalışır (NULL'lar DESC'te en sona düşer).
    @ColumnInfo(name = "last_message_at")
    val lastMessageAt: String?,
    @ColumnInfo(name = "last_message_content")
    val lastMessageContent: String?,
    @ColumnInfo(name = "other_participant_name")
    val otherParticipantName: String?,
    @ColumnInfo(name = "other_participant_id")
    val otherParticipantId: Long?,
    @ColumnInfo(name = "is_group")
    val isGroup: Boolean,
    @ColumnInfo(name = "request_status")
    val requestStatus: String,
    @ColumnInfo(name = "initiator_id")
    val initiatorId: Long?,
    @ColumnInfo(name = "unread_count")
    val unreadCount: Int = 0
)
