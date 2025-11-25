package com.honeycomb.disciplineapp.data.repository

import com.honeycomb.disciplineapp.data.dto.OnboardingDto
import com.honeycomb.disciplineapp.domain.repository.OnboardingRepository
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.functions.FirebaseFunctions

class OnboardingRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val functions: FirebaseFunctions
): OnboardingRepository {
    override suspend fun getOnboarding(): Result<OnboardingDto> {
        try {
            val result = functions.httpsCallable("onboarding").invoke()
            return Result.success(result.data<OnboardingDto>())
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}