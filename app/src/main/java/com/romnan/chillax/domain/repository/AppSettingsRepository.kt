package com.romnan.chillax.domain.repository

import com.romnan.chillax.domain.model.AppSettings
import com.romnan.chillax.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {
    val appSettings: Flow<AppSettings>
    suspend fun setThemeMode(themeMode: ThemeMode)
    suspend fun setBedtime(timeInMillis: Long?)
}