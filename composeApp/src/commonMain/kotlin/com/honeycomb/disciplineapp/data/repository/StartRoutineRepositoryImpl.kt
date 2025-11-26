package com.honeycomb.disciplineapp.data.repository

import com.honeycomb.disciplineapp.data.dto.StartRoutineDto
import com.honeycomb.disciplineapp.domain.repository.StartRoutineRepository
import com.honeycomb.disciplineapp.utils.readFromFile
import dev.gitlive.firebase.functions.FirebaseFunctions
import org.jetbrains.compose.resources.ExperimentalResourceApi

class StartRoutineRepositoryImpl(
    private val functions: FirebaseFunctions
): StartRoutineRepository {
    @OptIn(ExperimentalResourceApi::class)
    override suspend fun getStartRoutineData(): Result<StartRoutineDto> {
        try {
            val result = readFromFile<StartRoutineDto>("start_routine.json")
            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}