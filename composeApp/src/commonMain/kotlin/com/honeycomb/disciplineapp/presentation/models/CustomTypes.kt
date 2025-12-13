package com.honeycomb.disciplineapp.presentation.models

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import com.eygraber.uri.UriCodec
import com.honeycomb.disciplineapp.data.dto.OnboardingDto
import com.honeycomb.disciplineapp.presentation.ui.focus_app.CreateFocusState
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

val CreateFocusStateType = object : NavType<CreateFocusState>(
    isNullableAllowed = false
) {
    override fun get(bundle: Bundle, key: String): CreateFocusState? {
        return Json.decodeFromString<CreateFocusState>(bundle.getString(key) ?: return null)
    }

    override fun parseValue(value: String): CreateFocusState {
        return Json.decodeFromString<CreateFocusState>(UriCodec.decode(value))
    }

    override fun put(bundle: Bundle, key: String, value: CreateFocusState) {
        val serialized = Json.encodeToString<CreateFocusState>(value)
        bundle.putString(key, serialized)
    }

    override fun serializeAsValue(value: CreateFocusState): String {
        return UriCodec.encode(Json.encodeToString<CreateFocusState>(value))
    }
}