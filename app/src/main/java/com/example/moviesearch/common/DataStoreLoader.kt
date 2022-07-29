package com.example.moviesearch.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.socarassignment.common.logger
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

private val SIZE = intPreferencesKey("size")

@Singleton
class DataStoreLoader @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "words")
    private val Context.sizeDataStore: DataStore<Preferences> by preferencesDataStore(name = "currnet")
    private var size: Int = 0

    init {
        val ceh = CoroutineExceptionHandler { _, e ->
            logger("DataStoreLoader load size error ${e.message}")
        }
        CoroutineScope(Job() + ceh).launch {
            context.sizeDataStore.data.collect { preference ->
                if (preference.contains(SIZE)) {
                    preference[SIZE]?.let { size = it }
                }
            }
        }
    }

    suspend fun setLog(word: String) {
        context.dataStore.edit { settings ->
            if (size < 10) {
                settings[stringPreferencesKey("$size")] = word
                size++
            } else {
                size = 0
                settings[stringPreferencesKey("$size")] = word
            }
        }
        context.sizeDataStore.edit { settings -> settings[SIZE] = size }
    }

    fun getLog() = context.dataStore.data
        .mapNotNull { preferences ->
            preferences.asMap().values.toList().map { it.toString() }
        }

}