package com.honeycomb.disciplineapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OnboardingDto(
    @SerialName("login") val login: OnboardingLoginDto?,
) {

    @Serializable
    data class OnboardingLoginDto(
        @SerialName("title") val title: List<StyleText>?,
        @SerialName("sub_title") val subTitle: List<StyleText>?,
        @SerialName("footer") val footer: List<StyleText>?,
        @SerialName("first_buttons") val firstButtons: List<ButtonDto>?,
        @SerialName("second_buttons") val secondButtons: List<ButtonDto>?,
    )
}
