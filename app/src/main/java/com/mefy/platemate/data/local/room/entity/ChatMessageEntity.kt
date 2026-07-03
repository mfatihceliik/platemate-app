package com.mefy.platemate.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

/**
 * Mesajın yerel kopyası (tek doğru kaynak). [messageId] sunucu id'sidir; aynı (owner, message)
 * tekrar gelirse REPLACE ile üzerine yazılır → gönderen echo'su ile çift kayıt olmaz.
 */
@Entity(
    tableName = "chat_messages",
    primaryKeys = ["owner_user_id", "message_id"],
    indices = [Index(value = ["owner_user_id", "chat_room_id", "sent_at"])]
)
data class ChatMessageEntity(
    @ColumnInfo(name = "owner_user_id")
    val ownerUserId: Long,
    @ColumnInfo(name = "message_id")
    val messageId: Long,
    @ColumnInfo(name = "chat_room_id")
    val chatRoomId: Long?,
    @ColumnInfo(name = "sender_user_id")
    val senderUserId: Long?,
    @ColumnInfo(name = "sender_username")
    val senderUsername: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "sent_at")
    val sentAt: String?,
    @ColumnInfo(name = "is_read")
    val isRead: Boolean,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "delivered_at")
    val deliveredAt: String?,
    @ColumnInfo(name = "read_at")
    val readAt: String?
)
