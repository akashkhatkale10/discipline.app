package com.honeycomb.disciplineapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FocusSessionDao {
    @Insert
    suspend fun insertFocusSession(entity: FocusSessionEntity)

    @Query("SELECT * FROM FocusSession ORDER BY startTimestamp DESC")
    suspend fun getAllFocusSessions(): List<FocusSessionEntity>
}