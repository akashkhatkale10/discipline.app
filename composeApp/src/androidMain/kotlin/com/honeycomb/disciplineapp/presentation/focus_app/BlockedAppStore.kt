package com.honeycomb.disciplineapp.presentation.focus_app

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.blockedAppsDataStore: DataStore<Preferences> by preferencesDataStore(name = "blocked_apps")
@SuppressLint("StaticFieldLeak")
object BlockedAppsStore {
    private const val PREF_BLOCKED_APPS = "blocked_apps_set"

    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    private val blockedAppsKey = stringSetPreferencesKey(PREF_BLOCKED_APPS)

    suspend fun addBlockedApp(packageName: String) {
        context.blockedAppsDataStore.edit { prefs ->
            val current = prefs[blockedAppsKey] ?: emptySet()
            prefs[blockedAppsKey] = current + packageName
        }
    }

    suspend fun removeBlockedApp(packageName: String) {
        context.blockedAppsDataStore.edit { prefs ->
            val current = prefs[blockedAppsKey] ?: emptySet()
            prefs[blockedAppsKey] = current - packageName
        }
    }

    suspend fun isBlocked(packageName: String): Boolean {
        return context.blockedAppsDataStore.data.map { it[blockedAppsKey] ?: emptySet() }.first().contains(packageName)
    }

    fun getBlockedAppsFlow(): Flow<Set<String>> {
        return context.blockedAppsDataStore.data.map { it[blockedAppsKey] ?: emptySet() }
    }

    suspend fun getBlockedApps(): Set<String> {
        return context.blockedAppsDataStore.data
            .map { prefs -> prefs[blockedAppsKey] ?: emptySet() }
            .first()
    }
}