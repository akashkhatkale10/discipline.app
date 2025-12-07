package com.honeycomb.disciplineapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FocusDto(
    @SerialName("id") val id: String?,
    @SerialName("focus_name") val focusName: String?,
    @SerialName("focus_type") val focusType: String?,
) {

    companion object {
        const val FOCUS_TYPE_HARD = "FOCUS_TYPE_HARD"
        const val FOCUS_TYPE_SOFT = "FOCUS_TYPE_SOFT"
    }

}
