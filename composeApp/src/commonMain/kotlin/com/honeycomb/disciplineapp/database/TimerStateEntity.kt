package com.honeycomb.disciplineapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TimerState")
data class TimerStateEntity(
    @PrimaryKey val id: Int = 1,
    val state: String,
    val initialDuration: Long,
    val startTimestamp: Long?,
    val pausedRemainingSeconds: Long?,
)