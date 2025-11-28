package com.honeycomb.disciplineapp.presentation.ui.add_habit

import androidx.lifecycle.ViewModel
import com.honeycomb.disciplineapp.data.dto.EvidenceDto
import com.honeycomb.disciplineapp.data.dto.SetRoutineDto
import com.honeycomb.disciplineapp.presentation.utils.Constants.TIME_ANYTIME_ENABLED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddHabitViewModel: ViewModel() {

    @OptIn(ExperimentalUuidApi::class)
    private val _state: MutableStateFlow<AddHabitState> =
        MutableStateFlow(AddHabitState(
            habit = SetRoutineDto.SetHabitDto(
                id = Uuid.random().toString(),
                evidence = SetRoutineDto.SetEvidenceDto(
                    type = ""
                ),
                details = mapOf()
            )
        ))
    val state = _state.asStateFlow()

    fun setInitialData(
        selectedEvidence: EvidenceDto
    ) {
        val s = selectedEvidence.fields?.associate {
            it.id.orEmpty() to when (it.type) {
                "INPUT_FIELD" -> ""
                "DROP_DOWN" -> it.dropDown?.options?.firstOrNull {
                    it.selected == true
                }?.id.orEmpty()
                "SELECTION" -> it.selection?.options?.firstOrNull()?.id.orEmpty()
                "BOTTOMSHEET" -> ""
                else -> ""
            }
        }
        _state.update {
            it.copy(
                habit = it.habit?.copy(
                    details = it.habit.details.toMutableMap().apply {
                        putAll(s.orEmpty())
                        put(TIME_ANYTIME_ENABLED, "true")
                    },
                    evidence = SetRoutineDto.SetEvidenceDto(
                        type = selectedEvidence.type.orEmpty()
                    )
                )
            )
        }
    }

    fun setParam(
        key: String,
        value: String
    ) {
        _state.update {
            it.copy(
                habit = it.habit?.copy(
                    details = it.habit.details.toMutableMap().apply {
                        put(key, value)
                    }
                )
            )
        }
    }
}

data class AddHabitState(
    val habit: SetRoutineDto.SetHabitDto? = null
)