package com.romnan.chillax.data.repository

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.romnan.chillax.BuildConfig
import com.romnan.chillax.domain.repository.RemoteConfigRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import logcat.asLog
import logcat.logcat

class RemoteConfigRepositoryImpl(
    private val appContext: Context,
) : RemoteConfigRepository {

    val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    override val appUpdateRequired: Flow<Boolean> = callbackFlow {
        remoteConfig.fetchAndActivate().addOnCompleteListener(
            ContextCompat.getMainExecutor(appContext),
        ) { task ->
            when {
                task.isSuccessful -> {
                    val minVersion = remoteConfig.getLong(MIN_VERSION_CODE_KEY)
                    trySend(BuildConfig.VERSION_CODE < minVersion)
                }

                else -> {
                    logcat { task.exception?.asLog().orEmpty() }
                    trySend(false)
                }
            }
        }

        remoteConfig.addOnConfigUpdateListener(
            object : ConfigUpdateListener {
                override fun onUpdate(configUpdate: ConfigUpdate) {
                    if (configUpdate.updatedKeys.contains(MIN_VERSION_CODE_KEY)) {
                        remoteConfig.activate().addOnCompleteListener { task ->
                            when {
                                task.isSuccessful -> {
                                    val minVersion = remoteConfig.getLong(MIN_VERSION_CODE_KEY)
                                    trySend(BuildConfig.VERSION_CODE < minVersion)
                                }

                                else -> {
                                    logcat { task.exception?.asLog().orEmpty() }
                                    trySend(false)
                                }
                            }
                        }
                    }
                }

                override fun onError(error: FirebaseRemoteConfigException) {
                    logcat { error.asLog() }
                }
            },
        )

        awaitClose()
    }

    init {
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        })
        remoteConfig.setDefaultsAsync(mapOf(MIN_VERSION_CODE_KEY to BuildConfig.VERSION_CODE))
    }

    companion object {
        private const val MIN_VERSION_CODE_KEY = "android_min_supported_version_code"
    }
}