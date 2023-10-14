package com.ry05k2ulv.myapplication.datastore

import androidx.datastore.core.DataStore
import com.ry05k2ulv.myapplication.model.DarkThemeConfig
import com.ry05k2ulv.myapplication.model.UserData
import com.ry05k2ulv.sudokusolver.datastore.UserPreferences
import com.ry05k2ulv.sudokusolver.datastore.UserPreferences.DarkThemeConfigProto.*
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MagPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val userData = userPreferences.data.map {
        UserData(
            useDynamicColor = it.useDynamicColor,
            darkThemeConfig = when (it.darkThemeConfig) {
                null, UNRECOGNIZED, SYSTEM -> DarkThemeConfig.SYSTEM
                LIGHT -> DarkThemeConfig.LIGHT
                DARK -> DarkThemeConfig.DARK
            }
        )
    }

    suspend fun setUseDynamicColor(useDynamicColor: Boolean) {
        userPreferences.updateData {
            it.toBuilder().setUseDynamicColor(useDynamicColor).build()
        }
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        userPreferences.updateData {
            it.toBuilder().setDarkThemeConfig(
                when (darkThemeConfig) {
                    DarkThemeConfig.SYSTEM -> SYSTEM
                    DarkThemeConfig.LIGHT -> LIGHT
                    DarkThemeConfig.DARK -> DARK
                }
            ).build()
        }
    }
}