package com.honeycomb.disciplineapp.domain.repository

import com.honeycomb.disciplineapp.data.dto.OnboardingDto

interface OnboardingRepository {

    suspend fun getOnboarding(): Result<OnboardingDto>
}