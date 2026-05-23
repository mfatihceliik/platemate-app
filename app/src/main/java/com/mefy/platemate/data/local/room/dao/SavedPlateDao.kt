package com.mefy.platemate.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mefy.platemate.data.local.room.entity.SavedPlateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedPlateDao {

    @Query(
        """
        SELECT * FROM saved_plates
        WHERE user_id = :userId
        ORDER BY saved_at DESC
        """
    )
    fun observeSavedPlates(userId: Long): Flow<List<SavedPlateEntity>>

    @Query(
        """
        SELECT normalized_plate_code FROM saved_plates
        WHERE user_id = :userId
        """
    )
    fun observeSavedPlateCodes(userId: Long): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: SavedPlateEntity)

    @Query(
        """
        DELETE FROM saved_plates
        WHERE user_id = :userId
          AND normalized_plate_code = :normalizedPlateCode
        """
    )
    suspend fun deleteByNormalizedPlate(userId: Long, normalizedPlateCode: String): Int
}
