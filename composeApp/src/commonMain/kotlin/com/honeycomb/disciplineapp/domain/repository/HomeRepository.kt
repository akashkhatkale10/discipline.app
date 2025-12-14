package com.honeycomb.disciplineapp.domain.repository

import com.honeycomb.disciplineapp.data.dto.FocusDto
import com.honeycomb.disciplineapp.data.dto.FocusSessionDto
import com.honeycomb.disciplineapp.data.dto.HomeDto

interface HomeRepository {

    suspend fun getHome(): Result<List<FocusSessionDto>>
}