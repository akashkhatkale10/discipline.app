package com.honeycomb.disciplineapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ButtonDto(
    @SerialName("type") val type: String?,
    @SerialName("title") val title: String?,
    @SerialName("action") val action: String?,
) {

    enum class ButtonType {
        PRIMARY_BUTTON, SECONDARY_BUTTON,
        PRIMARY_SMALL_BUTTON, SECONDARY_SMALL_BUTTON, NONE;

        companion object {
            fun fromString(value: String?): ButtonType? = entries.find { it.name == value } ?: NONE
        }
    }

}
