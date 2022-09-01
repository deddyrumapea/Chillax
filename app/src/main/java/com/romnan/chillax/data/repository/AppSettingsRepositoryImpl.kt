package com.romnan.chillax.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.romnan.chillax.data.model.AppSettingsSerializable
import com.romnan.chillax.data.serializer.AppSettingsSerializer
import com.romnan.chillax.domain.model.AppSettings
import com.romnan.chillax.domain.model.ThemeMode
import com.romnan.chillax.domain.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppSettingsRepositoryImpl(
    appContext: Context,
) : AppSettingsRepository {

    private val appSettingsDataStore: DataStore<AppSettingsSerializable> =
        appContext.appSettingsDataStore

    override val appSettings: Flow<AppSettings>
        get() = appSettingsDataStore.data.map { serializable ->
            AppSettings(
                themeMode = try {
                    ThemeMode.valueOf(serializable.themeModeName)
                } catch (e: IllegalArgumentException) {
                    ThemeMode.System
                },
                bedTimeInMillis = serializable.bedTimeInMillis,
            )
        }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        appSettingsDataStore.updateData {
            it.copy(themeModeName = themeMode.name)
        }
    }

    override suspend fun setBedTime(timeInMillis: Long?) {
        appSettingsDataStore.updateData {
            it.copy(bedTimeInMillis = timeInMillis)
        }
    }

    companion object {
        private const val FILE_NAME = "settings.json"

        private val Context.appSettingsDataStore by dataStore(
            fileName = FILE_NAME,
            serializer = AppSettingsSerializer,
        )
    }
}