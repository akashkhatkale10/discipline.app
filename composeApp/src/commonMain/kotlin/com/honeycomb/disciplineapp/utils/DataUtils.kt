package com.honeycomb.disciplineapp.utils

import disciplineapp.composeapp.generated.resources.Res
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
internal suspend inline fun <reified T> readFromFile(fileName: String): T {
    val readBytes = Res.readBytes("files/$fileName")
    val json = Json {
        ignoreUnknownKeys = true

    }
    return json.decodeFromString(readBytes.decodeToString())
}