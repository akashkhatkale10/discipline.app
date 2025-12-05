package com.honeycomb.disciplineapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StyleText(
    @SerialName("text") val text: String?,
    @SerialName("font_size") val fontSize: Int?,
    @SerialName("font_weight") val fontWeight: String?,
    @SerialName("color") val color: String?,
)
