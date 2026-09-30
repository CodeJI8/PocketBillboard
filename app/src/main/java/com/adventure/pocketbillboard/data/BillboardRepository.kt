package com.adventure.pocketbillboard.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "billboards_prefs")

class BillboardRepository(private val context: Context) {
    private val gson = Gson()
    private val billboardsKey = stringPreferencesKey("billboards_list_json")

    val billboardsFlow: Flow<List<Billboard>> = context.dataStore.data.map { preferences ->
        val json = preferences[billboardsKey]
        if (json.isNullOrEmpty()) {
            defaultBillboards
        } else {
            try {
                val type = object : TypeToken<List<Billboard>>() {}.type
                gson.fromJson<List<Billboard>>(json, type) ?: defaultBillboards
            } catch (e: Exception) {
                defaultBillboards
            }
        }
    }

    suspend fun saveBillboard(billboard: Billboard) {
        context.dataStore.edit { preferences ->
            val json = preferences[billboardsKey]
            val type = object : TypeToken<List<Billboard>>() {}.type
            val currentList: MutableList<Billboard> = if (!json.isNullOrEmpty()) {
                try {
                    gson.fromJson(json, type) ?: mutableListOf()
                } catch (e: Exception) {
                    mutableListOf()
                }
            } else {
                defaultBillboards.toMutableList()
            }

            val index = currentList.indexOfFirst { it.id == billboard.id }
            if (index >= 0) {
                currentList[index] = billboard
            } else {
                currentList.add(0, billboard)
            }
            preferences[billboardsKey] = gson.toJson(currentList)
        }
    }

    suspend fun deleteBillboard(id: String) {
        context.dataStore.edit { preferences ->
            val json = preferences[billboardsKey]
            val currentList: MutableList<Billboard> = if (!json.isNullOrEmpty()) {
                val type = object : TypeToken<List<Billboard>>() {}.type
                try {
                    gson.fromJson(json, type) ?: defaultBillboards.toMutableList()
                } catch (e: Exception) {
                    defaultBillboards.toMutableList()
                }
            } else {
                defaultBillboards.toMutableList()
            }
            currentList.removeAll { it.id == id }
            preferences[billboardsKey] = gson.toJson(currentList)
        }
    }

    suspend fun toggleFavorite(id: String) {
        context.dataStore.edit { preferences ->
            val json = preferences[billboardsKey]
            val currentList: MutableList<Billboard> = if (!json.isNullOrEmpty()) {
                val type = object : TypeToken<List<Billboard>>() {}.type
                try {
                    gson.fromJson(json, type) ?: defaultBillboards.toMutableList()
                } catch (e: Exception) {
                    defaultBillboards.toMutableList()
                }
            } else {
                defaultBillboards.toMutableList()
            }
            val index = currentList.indexOfFirst { it.id == id }
            if (index >= 0) {
                val item = currentList[index]
                currentList[index] = item.copy(isFavorite = !item.isFavorite)
                preferences[billboardsKey] = gson.toJson(currentList)
            }
        }
    }

    companion object {
        val defaultBillboards = listOf(
            Billboard(
                id = "default_1",
                title = "Uber / Lyft Pickup",
                text = "ALEX (BAGS 2)",
                themePresetId = "obsidian_neon",
                fontStyle = "sans_bold",
                isFavorite = true,
                createdAt = System.currentTimeMillis() - 1000
            ),
            Billboard(
                id = "default_2",
                title = "Airport Arrival Gate",
                text = "GATE B12 - SMITH",
                themePresetId = "cyber_cyan",
                fontStyle = "sans_bold",
                isFavorite = true,
                createdAt = System.currentTimeMillis() - 2000
            ),
            Billboard(
                id = "default_3",
                title = "Wi-Fi Guest Password",
                text = "WIFI: CaféCentral\nPASS: espresso24",
                themePresetId = "matrix_green",
                fontStyle = "monospace",
                isFavorite = true,
                createdAt = System.currentTimeMillis() - 3000
            ),
            Billboard(
                id = "default_4",
                title = "Delivery / Address",
                text = "123 MAIN ST, APT 4B",
                themePresetId = "sunset_magenta",
                fontStyle = "sans_bold",
                isFavorite = false,
                createdAt = System.currentTimeMillis() - 4000
            )
        )
    }
}
