package com.honeycomb.disciplineapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class HomeDto(
    @SerialName("habits") val habits: List<HabitDto?>?,
    @SerialName("addNewHabit") val addNewScreen: AddNewScreenDto?
) {
    @Serializable
    data class AddNewScreenDto(
        val title: String?,
        val subtitle: String?,
        val buttonTitle: String?,
    )
}
