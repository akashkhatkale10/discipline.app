package com.honeycomb.disciplineapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartRoutineDto(
    @SerialName("title") val title: String?,
    @SerialName("options") val options: List<RoutineOptionDto>?
)

@Serializable
data class RoutineOptionDto(
    @SerialName("title") val title: String?,
    @SerialName("subtitle") val subtitle: String?,
    @SerialName("type") val type: String?, // HABIT?, PENALTY
    @SerialName("button") val button: ButtonDto?,
    @SerialName("habit_data") val habitData: HabitDataDto? = null // Only for type = "HABIT"
)

// ─────────────────────────────────────────────
// Habit Data Block
// ─────────────────────────────────────────────

@Serializable
data class HabitDataDto(
    @SerialName("evidence_bs") val evidenceBs: EvidenceBsDto?,
    @SerialName("evidences") val evidences: List<EvidenceDto>?
)

// ─────────────────────────────────────────────
// Evidence Selection (Same as previous JSON)
// ─────────────────────────────────────────────

@Serializable
data class EvidenceBsDto(
    @SerialName("title") val title: String?,
    @SerialName("options") val options: List<OptionDto>?
)

@Serializable
data class OptionDto(
    @SerialName("type") val type: String?,
    @SerialName("title") val title: String?,
    @SerialName("subtitle") val subtitle: String?
)

@Serializable
data class EvidenceDto(
    @SerialName("type") val type: String?,
    @SerialName("fields") val fields: List<FieldDto>?
)

@Serializable
data class FieldDto(
    @SerialName("type") val type: String?,
    @SerialName("id") val id: String?,
    @SerialName("bs") val bs: BottomSheetValueDto? = null,
    @SerialName("input_field") val inputField: InputFieldDto? = null,
    @SerialName("drop_down") val dropDown: DropDownDto? = null,
    @SerialName("bottomsheet") val bottomSheet: BottomSheetDto? = null
)

@Serializable
data class BottomSheetValueDto(
    @SerialName("title") val title: String?,
    @SerialName("selected") val selected: String?
)

@Serializable
data class InputFieldDto(
    @SerialName("title") val title: String?,
    @SerialName("hint") val hint: List<String>?,
    @SerialName("max_length") val maxLength: Int?
)

@Serializable
data class DropDownDto(
    @SerialName("title") val title: String?,
    @SerialName("options") val options: List<DropDownOptionDto>?
)

@Serializable
data class DropDownOptionDto(
    @SerialName("title") val title: String? = null,
    @SerialName("selected") val selected: Boolean?,
    @SerialName("id") val id: String?
)

@Serializable
data class BottomSheetDto(
    @SerialName("title") val title: String?
)
