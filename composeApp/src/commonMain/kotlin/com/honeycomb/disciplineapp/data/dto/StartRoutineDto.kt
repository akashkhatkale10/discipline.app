package com.honeycomb.disciplineapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartRoutineDto(
    @SerialName("title") val title: String? = null,
    @SerialName("options") val options: List<RoutineOptionDto>?
)

@Serializable
data class RoutineOptionDto(
    @SerialName("title") val title: String? = null,
    @SerialName("subtitle") val subtitle: String? = null,
    @SerialName("type") val type: String? = null, // HABIT? = null, PENALTY
    @SerialName("button") val button: ButtonDto? = null,
    @SerialName("habit_data") val habitData: HabitDataDto? = null // Only for type = "HABIT"
)

// ─────────────────────────────────────────────
// Habit Data Block
// ─────────────────────────────────────────────

@Serializable
data class HabitDataDto(
    @SerialName("evidence_bs") val evidenceBs: EvidenceBsDto? = null,
    @SerialName("evidences") val evidences: List<EvidenceDto>? = null,
    @SerialName("button") val button: ButtonDto?
)

// ─────────────────────────────────────────────
// Evidence Selection (Same as previous JSON)
// ─────────────────────────────────────────────

@Serializable
data class EvidenceBsDto(
    @SerialName("title") val title: String? = null,
    @SerialName("options") val options: List<OptionDto>?
)

@Serializable
data class OptionDto(
    @SerialName("type") val type: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("subtitle") val subtitle: String?
)

@Serializable
data class EvidenceDto(
    @SerialName("type") val type: String? = null,
    @SerialName("fields") val fields: List<FieldDto>?
)

@Serializable
data class FieldDto(
    @SerialName("type") val type: String? = null,
    @SerialName("id") val id: String? = null,
    @SerialName("bs") val bs: BottomSheetValueDto? = null,
    @SerialName("input_field") val inputField: InputFieldDto? = null,
    @SerialName("drop_down") val dropDown: DropDownDto? = null,
    @SerialName("selection") val selection: DropDownDto? = null,
)

@Serializable
data class BottomSheetValueDto(
    @SerialName("title") val title: String? = null,
    @SerialName("selected_value") val selected: String?
)

@Serializable
data class InputFieldDto(
    @SerialName("title") val title: String? = null,
    @SerialName("hint") val hint: List<String>? = null,
    @SerialName("max_length") val maxLength: Int?
)

@Serializable
data class DropDownDto(
    @SerialName("title") val title: String? = null,
    @SerialName("bs_title") val bsTitle: String? = null,
    @SerialName("options") val options: List<DropDownOptionDto>?
)

@Serializable
data class DropDownOptionDto(
    @SerialName("title") val title: String? = null,
    @SerialName("subtitle") val subtitle: String? = null,
    @SerialName("selected") val selected: Boolean? = null,
    @SerialName("id") val id: String?
)

