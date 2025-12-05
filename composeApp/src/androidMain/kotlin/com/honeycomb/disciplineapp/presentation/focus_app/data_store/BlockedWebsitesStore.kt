package com.honeycomb.disciplineapp.presentation.focus_app.data_store

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val blockedWebsitesKey = stringSetPreferencesKey("blocked_websites")

@SuppressLint("StaticFieldLeak")
object BlockedWebsitesStore {
    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    suspend fun addWebsite(url: String) {
        val clean = url.lowercase().removePrefix("http://").removePrefix("https://").removePrefix("www.")
        context.blockedAppsDataStore.edit { prefs ->
            val current = prefs[blockedWebsitesKey] ?: emptySet()
            prefs[blockedWebsitesKey] = current + clean
        }
    }

    suspend fun removeWebsite(url: String) {
        val clean = url.lowercase().removePrefix("http://").removePrefix("https://").removePrefix("www.")
        context.blockedAppsDataStore.edit { prefs ->
            val current = prefs[blockedWebsitesKey] ?: emptySet()
            prefs[blockedWebsitesKey] = current - clean
        }
    }

    fun getBlockedWebsitesFlow(): Flow<Set<String>> =
        context.blockedAppsDataStore.data.map { it[blockedWebsitesKey] ?: emptySet() }
}