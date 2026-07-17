package com.mefy.platemate.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mefy.platemate.data.local.room.entity.ChatRoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatRoomDao {

    @Query(
        """
        SELECT * FROM chat_rooms
        WHERE owner_user_id = :ownerUserId
        ORDER BY last_message_at DESC
        """
    )
    fun observeRooms(ownerUserId: Long): Flow<List<ChatRoomEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ChatRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entities: List<ChatRoomEntity>)

    // Canlı mesaj geldiğinde odanın önizleme/sıralama alanlarını günceller. Etkilenen satır 0 ise
    // oda henüz yerelde yok demektir (yeni oda) → çağıran REST senk tetikler.
    @Query(
        """
        UPDATE chat_rooms
        SET last_message_content = :content, last_message_at = :sentAt, last_message_sender_id = :senderId
        WHERE owner_user_id = :ownerUserId AND room_id = :roomId
        """
    )
    suspend fun updatePreview(ownerUserId: Long, roomId: Long, content: String?, sentAt: String?, senderId: Long?): Int

    // Karşıdan yeni mesaj gelince (kullanıcı o odada değilken) okunmamış sayacını artırır.
    @Query(
        """
        UPDATE chat_rooms
        SET unread_count = unread_count + 1
        WHERE owner_user_id = :ownerUserId AND room_id = :roomId
        """
    )
    suspend fun incrementUnread(ownerUserId: Long, roomId: Long): Int

    // Oda açılıp okunduğunda sayacı sıfırlar.
    @Query(
        """
        UPDATE chat_rooms
        SET unread_count = 0
        WHERE owner_user_id = :ownerUserId AND room_id = :roomId
        """
    )
    suspend fun clearUnread(ownerUserId: Long, roomId: Long)

    @Query("DELETE FROM chat_rooms WHERE owner_user_id = :ownerUserId")
    suspend fun deleteAllForUser(ownerUserId: Long)

    @Query("DELETE FROM chat_rooms WHERE owner_user_id = :ownerUserId AND room_id = :roomId")
    suspend fun deleteRoom(ownerUserId: Long, roomId: Long)
}
