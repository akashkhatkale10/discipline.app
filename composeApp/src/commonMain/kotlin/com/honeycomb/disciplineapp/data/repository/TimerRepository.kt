package com.honeycomb.disciplineapp.data.repository

import com.honeycomb.disciplineapp.database.TimerDao
import com.honeycomb.disciplineapp.database.TimerStateEntity
import com.honeycomb.disciplineapp.presentation.ui.focus_app.TimerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class TimerPersistedState(
    val state: TimerState,
    val initialDuration: Int,
    val startTimestamp: Instant?,
    val pausedRemainingSeconds: Int?,
)

class TimerRepository(private val dao: TimerDao) {

    suspend fun saveTimerState(
        state: TimerState,
        initialDuration: Int,
        startTimestamp: Instant?,
        pausedRemainingSeconds: Int?,
    ) = withContext(Dispatchers.IO) {
        dao.insertOrUpdateTimerState(
            TimerStateEntity(
                state = state.name,
                initialDuration = initialDuration.toLong(),
                startTimestamp = startTimestamp?.toEpochMilliseconds(),
                pausedRemainingSeconds = pausedRemainingSeconds?.toLong()
            )
        )
    }

    suspend fun loadTimerState(): TimerPersistedState? = withContext(Dispatchers.IO) {
        val entity = dao.getTimerState() ?: return@withContext null
        TimerPersistedState(
            state = TimerState.find(entity.state),
            initialDuration = entity.initialDuration.toInt(),
            startTimestamp = entity.startTimestamp?.let { Instant.fromEpochMilliseconds(it) },
            pausedRemainingSeconds = entity.pausedRemainingSeconds?.toInt()
        )
    }

    suspend fun clearTimerState() = withContext(Dispatchers.IO) {
        dao.clearTimerState()
    }
}
