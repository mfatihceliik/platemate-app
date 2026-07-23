package com.mefy.platemate.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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

    // Bir önceki oturumdan kalan taşlaşmış (DELETED) satırları temizler; taze bir oturum
    // başlarken bir kez çağrılır ki "silindi" balonu bir sonraki girişte tekrar görünmesin.
    @Query(
        "DELETE FROM chat_messages WHERE owner_user_id = :ownerUserId AND chat_room_id = :roomId AND status = 'DELETED'"
    )
    suspend fun deleteTombstonedForRoom(ownerUserId: Long, roomId: Long)

    @Query("DELETE FROM chat_messages WHERE owner_user_id = :ownerUserId AND message_id = :messageId")
    suspend fun deleteById(ownerUserId: Long, messageId: Long): Int

    @Query(
        "SELECT * FROM chat_messages WHERE owner_user_id = :ownerUserId AND message_id = :messageId"
    )
    suspend fun getByOwnerAndId(ownerUserId: Long, messageId: Long): ChatMessageEntity?

    // Cold-start recovery: any PENDING row found here belongs to a coroutine that died with the
    // previous process (a live process always resolves its own PENDING rows to SENT/FAILED),
    // so every row returned is by definition orphaned.
    @Query("SELECT * FROM chat_messages WHERE owner_user_id = :ownerUserId AND status = 'PENDING'")
    suspend fun getAllPendingForOwner(ownerUserId: Long): List<ChatMessageEntity>

    // Server ack arrived: drop the temp PENDING row and commit the real server-assigned row.
    @Transaction
    suspend fun reconcilePending(ownerUserId: Long, tempId: Long, real: ChatMessageEntity) {
        deleteById(ownerUserId, tempId)
        upsert(real)
    }

    // Tombstone for "delete for everyone": clears content, flips status, keeps the row (and its
    // PK) in place — both the local optimistic write and the later server broadcast reconcile
    // through this same idempotent query.
    @Query(
        """
        UPDATE chat_messages SET status = :status, content = ''
        WHERE owner_user_id = :ownerUserId AND message_id = :messageId
        """
    )
    suspend fun tombstone(ownerUserId: Long, messageId: Long, status: String): Int

    // Cross-room message content search (Messages list "search inside messages"). DELETED rows
    // are excluded — their content is blanked by tombstone() above, so they'd otherwise surface
    // as empty-content hits. No FTS index exists yet, so this is a plain LIKE scan; fine at the
    // app's current local data scale.
    @Query(
        """
        SELECT * FROM chat_messages
        WHERE owner_user_id = :ownerUserId
          AND status != 'DELETED'
          AND content LIKE '%' || :term || '%'
        ORDER BY sent_at DESC
        LIMIT :limit
        """
    )
    suspend fun search(ownerUserId: Long, term: String, limit: Int): List<ChatMessageEntity>
}
