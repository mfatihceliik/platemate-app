package com.mefy.platemate.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mefy.platemate.data.local.room.entity.RecentSearchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {

    @Query(
        """
        SELECT * FROM recent_searches
        WHERE user_id = :userId
        ORDER BY searched_at DESC
        LIMIT :limit
        """
    )
    fun observeRecent(userId: Long, limit: Int): Flow<List<RecentSearchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: RecentSearchEntity)

    @Query(
        """
        DELETE FROM recent_searches
        WHERE user_id = :userId
          AND id NOT IN (
            SELECT id
            FROM recent_searches
            WHERE user_id = :userId
            ORDER BY searched_at DESC
            LIMIT :limit
        )
        """
    )
    suspend fun trimToLimit(userId: Long, limit: Int)

    @Query("DELETE FROM recent_searches WHERE user_id = :userId")
    suspend fun clearRecent(userId: Long)
}
