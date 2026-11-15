package com.lichvannien.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.lichvannien.core.model.ThemeMode
import java.io.IOException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.themeDataStore by preferencesDataStore("appearance")
class ThemeStore(context: Context) {
    private val store = context.applicationContext.themeDataStore
    private val key = stringPreferencesKey("theme_mode")
    val theme = store.data.catch { if(it is IOException) emit(emptyPreferences()) else throw it }.map { p -> runCatching { ThemeMode.valueOf(p[key] ?: "SYSTEM") }.getOrDefault(ThemeMode.SYSTEM) }
    suspend fun set(mode: ThemeMode) { store.edit { it[key] = mode.name } }
}
