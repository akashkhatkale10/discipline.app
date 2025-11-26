package com.honeycomb.disciplineapp.domain.repository

import com.honeycomb.disciplineapp.data.dto.StartRoutineDto

interface StartRoutineRepository {

    suspend fun getStartRoutineData(): Result<StartRoutineDto>

}