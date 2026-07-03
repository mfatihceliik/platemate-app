package com.mefy.platemate.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mefy.platemate.data.local.room.entity.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {

    @Query(
        """
        SELECT * FROM chat_messages
        WHERE owner_user_id = :ownerUserId AND chat_room_id = :roomId
        ORDER BY sent_at ASC
        """
    )
    fun observeByRoom(ownerUserId: Long, roomId: Long): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ChatMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entities: List<ChatMessageEntity>)

    // Tek mesajın durumu (ör. delivered sinyali messageId taşır).
    @Query(
        """
        UPDATE chat_messages SET status = :status
        WHERE owner_user_id = :ownerUserId AND message_id = :messageId
        """
    )
    suspend fun updateStatus(ownerUserId: Long, messageId: Long, status: String): Int

    // Okundu sinyali messageId taşımaz: gönderenin o odadaki tüm mesajlarını işaretle.
    @Query(
        """
        UPDATE chat_messages SET status = :status
        WHERE owner_user_id = :ownerUserId AND chat_room_id = :roomId AND sender_user_id = :senderUserId
        """
    )
    suspend fun updateStatusForSender(ownerUserId: Long, roomId: Long, senderUserId: Long, status: String)

    @Query("DELETE FROM chat_messages WHERE owner_user_id = :ownerUserId")
    suspend fun deleteAllForUser(ownerUserId: Long)

    @Query("DELETE FROM chat_messages WHERE owner_user_id = :ownerUserId AND chat_room_id = :roomId")
    suspend fun deleteMessagesForRoom(ownerUserId: Long, roomId: Long)
}
