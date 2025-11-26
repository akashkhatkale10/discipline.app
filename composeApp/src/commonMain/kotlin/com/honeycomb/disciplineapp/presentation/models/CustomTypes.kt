package com.honeycomb.disciplineapp.presentation.models

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import com.eygraber.uri.UriCodec
import com.honeycomb.disciplineapp.data.dto.HabitDataDto
import com.honeycomb.disciplineapp.data.dto.OnboardingDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val OnboardingScreenType = object : NavType<OnboardingDto>(
    isNullableAllowed = false
) {
    override fun get(bundle: Bundle, key: String): OnboardingDto? {
        return Json.decodeFromString(bundle.getString(key) ?: return null)
    }

    override fun parseValue(value: String): OnboardingDto {
        return Json.decodeFromString(UriCodec.decode(value))
    }

    override fun put(bundle: Bundle, key: String, value: OnboardingDto) {
        bundle.putString(key, Json.encodeToString(value))
    }


    override fun serializeAsValue(value: OnboardingDto): String {
        return UriCodec.encode(Json.encodeToString(value))
    }

}

val HabitDataScreenType = object : NavType<HabitDataDto>(
    isNullableAllowed = false
) {
    override fun get(bundle: Bundle, key: String): HabitDataDto? {
        return Json.decodeFromString<HabitDataDto>(bundle.getString(key) ?: return null)
    }

    override fun parseValue(value: String): HabitDataDto {
        return Json.decodeFromString<HabitDataDto>(UriCodec.decode(value))
    }

    override fun put(bundle: Bundle, key: String, value: HabitDataDto) {
        val serialized = Json.encodeToString<HabitDataDto>(value)
        bundle.putString(key, serialized)
    }

    override fun serializeAsValue(value: HabitDataDto): String {
        return UriCodec.encode(Json.encodeToString<HabitDataDto>(value))
    }
}