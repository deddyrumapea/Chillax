package com.romnan.chillax.data.repository

import android.app.AlarmManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.romnan.chillax.data.model.AppSettingsSerializable
import com.romnan.chillax.data.receiver.BedtimeBroadcastReceiver
import com.romnan.chillax.data.serializer.AppSettingsSerializer
import com.romnan.chillax.domain.model.AppSettings
import com.romnan.chillax.domain.model.ThemeMode
import com.romnan.chillax.domain.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import logcat.logcat

class AppSettingsRepositoryImpl(
    private val appContext: Context,
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
                bedtimeInMillis = serializable.bedtimeInMillis,
            )
        }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        appSettingsDataStore.updateData {
            it.copy(themeModeName = themeMode.name)
        }
    }

    override suspend fun setBedtime(timeInMillis: Long?) {
        appSettingsDataStore.updateData {
            if (timeInMillis != null) setBedtimeAlarm(timeInMillis)
            else cancelBedtimeAlarm()

            it.copy(bedtimeInMillis = timeInMillis)
        }
    }

    private fun setBedtimeAlarm(timeInMillis: Long) {
        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmManager?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            AlarmManager.INTERVAL_DAY,
            BedtimeBroadcastReceiver.getPendingIntent(appContext = appContext),
        )
        logcat { "bed time alarm set $timeInMillis" }
    }

    private fun cancelBedtimeAlarm() {
        val pendingIntent = BedtimeBroadcastReceiver.getPendingIntent(appContext = appContext)
        pendingIntent.cancel()

        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmManager?.cancel(pendingIntent)
        logcat { "bed time alarm canceled" }
    }

    companion object {
        private const val FILE_NAME = "settings.json"

        private val Context.appSettingsDataStore by dataStore(
            fileName = FILE_NAME,
            serializer = AppSettingsSerializer,
        )
    }
}