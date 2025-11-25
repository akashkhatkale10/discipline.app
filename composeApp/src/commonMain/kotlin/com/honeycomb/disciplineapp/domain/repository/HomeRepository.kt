package com.honeycomb.disciplineapp.domain.repository

import com.honeycomb.disciplineapp.data.dto.HomeDto

interface HomeRepository {

    suspend fun getHome(): Result<HomeDto>
}