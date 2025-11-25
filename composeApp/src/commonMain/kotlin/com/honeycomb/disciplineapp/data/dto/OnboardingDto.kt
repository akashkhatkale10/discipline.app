package com.honeycomb.disciplineapp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class OnboardingDto(
    val pages: List<OnboardingPage>?,
    val buttonText: String?
) {

    @Serializable
    data class OnboardingPage(
        val title: String?,
        val subtitle: String?,
        val type: String?,
        val points: List<String>?,
    )
}
