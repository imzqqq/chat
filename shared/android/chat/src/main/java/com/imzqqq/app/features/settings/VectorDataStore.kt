package com.imzqqq.app.features.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "vector_settings")

class VectorDataStore @Inject constructor(
        private val context: Context
) {

    private val pushCounter = intPreferencesKey("push_counter")

    val pushCounterFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[pushCounter] ?: 0
    }

    suspend fun incrementPushCounter() {
        context.dataStore.edit { settings ->
            val currentCounterValue = settings[pushCounter] ?: 0
            settings[pushCounter] = currentCounterValue + 1
        }
    }
}
