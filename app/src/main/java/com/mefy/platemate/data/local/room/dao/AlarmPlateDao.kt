package com.mefy.platemate.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mefy.platemate.data.local.room.entity.AlarmPlateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmPlateDao {

    @Query(
        """
        SELECT * FROM alarm_plates
        WHERE user_id = :userId
        ORDER BY saved_at DESC
        """
    )
    fun observeAlarmPlates(userId: Long): Flow<List<AlarmPlateEntity>>

    @Query(
        """
        SELECT normalized_plate_code FROM alarm_plates
        WHERE user_id = :userId
        """
    )
    fun observeAlarmPlateCodes(userId: Long): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: AlarmPlateEntity)

    @Query(
        """
        DELETE FROM alarm_plates
        WHERE user_id = :userId
          AND normalized_plate_code = :normalizedPlateCode
        """
    )
    suspend fun deleteByNormalizedPlate(userId: Long, normalizedPlateCode: String): Int

    @Query("DELETE FROM alarm_plates WHERE user_id = :userId")
    suspend fun deleteAllForUser(userId: Long)
}
