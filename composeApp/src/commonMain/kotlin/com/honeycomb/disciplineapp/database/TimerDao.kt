package com.honeycomb.disciplineapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TimerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateTimerState(entity: TimerStateEntity)

    @Query("SELECT * FROM TimerState WHERE id = 1")
    suspend fun getTimerState(): TimerStateEntity?

    @Query("DELETE FROM TimerState")
    suspend fun clearTimerState()
}
