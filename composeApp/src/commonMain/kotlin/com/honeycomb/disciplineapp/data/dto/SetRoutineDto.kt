package com.honeycomb.disciplineapp.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class SetRoutineDto(
    val habits: List<SetHabitDto>,
) {


    @Serializable
    data class SetHabitDto(
        val id: String,
        val evidence: SetEvidenceDto,
        val details: Map<String, String>
    )

    @Serializable
    data class SetEvidenceDto(
        val type: String,
    )
}
