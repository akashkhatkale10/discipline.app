package com.honeycomb.disciplineapp.data.dto

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class FocusSessionDto(
    val id: String? = null,
    val startTimestamp: Long,
    val endTimestamp: Long?,
    val durationSeconds: Int,
    val notes: String? = null,
) {
    fun getStartInstant(): Instant = Instant.fromEpochMilliseconds(startTimestamp)
    fun getEndInstant(): Instant? = endTimestamp?.let { Instant.fromEpochMilliseconds(it) }
}