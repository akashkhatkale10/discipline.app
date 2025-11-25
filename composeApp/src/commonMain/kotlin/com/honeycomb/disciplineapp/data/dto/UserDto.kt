package com.honeycomb.disciplineapp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val userId: String = "",
    val personalDetails: PersonalDetails? = null
) {
    @Serializable
    data class PersonalDetails(
        val profilePictureUrl: String? = null,
        val email: String? = null,
        val name: String? = null,
    )
}