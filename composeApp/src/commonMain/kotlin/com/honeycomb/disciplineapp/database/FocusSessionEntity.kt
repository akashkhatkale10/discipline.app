package com.honeycomb.disciplineapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FocusSession")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startTimestamp: Long,
    val endTimestamp: Long?,
    val durationSeconds: Int,
    val notes: String?,
)
