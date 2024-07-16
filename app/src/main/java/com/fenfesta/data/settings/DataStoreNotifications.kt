package com.fenfesta.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class NotificationPreferences(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "notifications")
        private val NOTIFIED_EVENTS = stringSetPreferencesKey("notified_events")
    }

    val notifiedEvents: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[NOTIFIED_EVENTS] ?: emptySet()
        }

    suspend fun addNotifiedEvent(eventId: String) {
        context.dataStore.edit { preferences ->
            val currentSet = preferences[NOTIFIED_EVENTS] ?: emptySet()
            preferences[NOTIFIED_EVENTS] = currentSet + eventId
        }
    }

    suspend fun removeNotifiedEvent(eventId: String) {
        context.dataStore.edit { preferences ->
            val currentSet = preferences[NOTIFIED_EVENTS] ?: emptySet()
            preferences[NOTIFIED_EVENTS] = currentSet - eventId
        }
    }

    suspend fun isEventNotified(eventId: String): Boolean {
        return context.dataStore.data.map { preferences ->
            val currentSet = preferences[NOTIFIED_EVENTS] ?: emptySet()
            currentSet.contains(eventId)
        }.first()
    }
}