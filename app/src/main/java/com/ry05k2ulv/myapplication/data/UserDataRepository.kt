package com.ry05k2ulv.myapplication.data

import com.ry05k2ulv.myapplication.datastore.MagPreferencesDataSource
import com.ry05k2ulv.myapplication.model.DarkThemeConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepository @Inject constructor(
    private val dataSource: MagPreferencesDataSource
) {
    val userData = dataSource.userData

    suspend fun setUseDynamicColor(useDynamicColor: Boolean) {
        dataSource.setUseDynamicColor(useDynamicColor)
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        dataSource.setDarkThemeConfig(darkThemeConfig)
    }
}