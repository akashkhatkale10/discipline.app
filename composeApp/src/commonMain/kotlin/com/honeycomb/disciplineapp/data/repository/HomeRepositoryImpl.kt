package com.honeycomb.disciplineapp.data.repository

import com.honeycomb.disciplineapp.data.dto.HomeDto
import com.honeycomb.disciplineapp.domain.repository.HomeRepository
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.functions.FirebaseFunctions

class HomeRepositoryImpl(
    private val functions: FirebaseFunctions
): HomeRepository {
    override suspend fun getHome(): Result<HomeDto> {
        try {
            val result = functions.httpsCallable("home").invoke().data<HomeDto>()

            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}