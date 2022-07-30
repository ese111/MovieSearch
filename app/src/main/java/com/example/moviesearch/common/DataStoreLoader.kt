package com.example.moviesearch.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

private val SIZE = intPreferencesKey("size") // 검색어 개수 저장 key

@Singleton
class DataStoreLoader @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "words") // 검색어
    private val Context.sizeDataStore: DataStore<Preferences> by preferencesDataStore(name = "currnet") // 데이터 개수
    private var size: Int = 0

    init {
        // 검색기록 데이터 개수 받아오기
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

    // 10개 까지만 저장, 추가 저장 시 앞에서부터 다시 저장
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

    // 검색기록을 List로 변경
    fun getLog() = context.dataStore.data
        .mapNotNull { preferences ->
            preferences.asMap().values.toList().map { it.toString() }
        }

}