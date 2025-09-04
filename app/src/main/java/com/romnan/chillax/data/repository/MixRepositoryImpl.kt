package com.romnan.chillax.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.datastore.dataStore
import com.romnan.chillax.data.model.CustomMixList
import com.romnan.chillax.data.model.CustomMixSerializable
import com.romnan.chillax.data.serializer.CustomMixesSerializer
import com.romnan.chillax.data.source.AppDataSource
import com.romnan.chillax.domain.model.Mix
import com.romnan.chillax.domain.repository.MixRepository
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import logcat.asLog
import logcat.logcat
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

class MixRepositoryImpl(
    private val appContext: Context,
    private val appScope: CoroutineScope,
    private val appDataSource: AppDataSource,
) : MixRepository {

    private val customImagesDir = File(
        appContext.filesDir,
        DIR_NAME_MIX_CUSTOM_IMAGES,
    ).apply { mkdirs() }

    private val savedMixCustomImagesDir = File(
        appContext.filesDir,
        DIR_NAME_SAVED_MIX_CUSTOM_IMAGES,
    ).apply { mkdirs() }

    override val mixes: Flow<List<Mix>>
        get() = appContext.customMixesDataStore.data.map { mixList: CustomMixList ->
            mixList.customMixes.reversed().map { customMix: CustomMixSerializable ->
                Mix(
                    uuid = customMix.uuid,
                    customMix = customMix,
                )
            } + appDataSource.presetMixes
        }
    override val mixPresetImageUris: Flow<Set<String>>
        get() = flowOf(appDataSource.mixImageUris)

    private val _mixCustomImageUris = MutableStateFlow(setOf<String>())
    override val mixCustomImageUris: Flow<Set<String>>
        get() = _mixCustomImageUris

    init {
        appScope.launch {
            refreshMixCustomImageUris()
        }
    }

    override suspend fun saveCustomMix(
        readableName: String,
        imageUri: String,
        soundIdToVolume: Map<String, Float>,
    ): Mix? = withContext(Dispatchers.IO) {

        try {
            val uuid = UUID.randomUUID().toString()

            val saveImageUri = when (_mixCustomImageUris.value.contains(imageUri)) {
                true -> {
                    val file = File(savedMixCustomImagesDir, Mix.CUSTOM_MIX_ID_PREFIX.plus(uuid))

                    file.outputStream().use { outputStream: OutputStream ->
                        appContext.contentResolver.openInputStream(imageUri.toUri())
                            ?.use { inputStream: InputStream ->
                                outputStream.write(inputStream.readBytes())
                            }
                    }

                    file.toUri().toString()
                }

                false -> imageUri
            }

            appContext.customMixesDataStore.updateData { customMixList: CustomMixList ->
                customMixList.copy(
                    customMixes = customMixList.customMixes.toPersistentList().add(
                        CustomMixSerializable(
                            uuid = uuid,
                            readableName = readableName,
                            imageUri = saveImageUri,
                            soundIdToVolume = soundIdToVolume,
                        )
                    ),
                )
            }

            mixes.firstOrNull()?.find { mix: Mix ->
                mix.id == Mix.CUSTOM_MIX_ID_PREFIX.plus(uuid)
            }
        } catch (e: Exception) {
            logcat { e.asLog() }
            null
        }
    }

    override suspend fun deleteCustomMix(
        mixId: String,
    ): Unit = withContext(Dispatchers.IO) {
        appContext.customMixesDataStore.updateData { customMixList: CustomMixList ->
            customMixList.copy(
                customMixes = customMixList.customMixes.toPersistentList()
                    .removeAll { customMix: CustomMixSerializable ->
                        customMix.uuid == mixId.removePrefix(Mix.CUSTOM_MIX_ID_PREFIX)
                    },
            )
        }

        File(savedMixCustomImagesDir, mixId).deleteRecursively()
    }

    override suspend fun saveMixCustomImage(
        uri: Uri,
    ): Uri = withContext(Dispatchers.IO) {
        val file = File(customImagesDir, UUID.randomUUID().toString())

        file.outputStream().use { outputStream: OutputStream ->
            appContext.contentResolver.openInputStream(uri)?.use { inputStream: InputStream ->
                outputStream.write(inputStream.readBytes())
            }
        }

        refreshMixCustomImageUris()

        file.toUri()
    }

    override suspend fun deleteMixCustomImage(
        uri: String,
    ): Unit = withContext(Dispatchers.IO) {
        try {
            uri.toUri().toFile().delete()
        } catch (e: Exception) {
            logcat { e.asLog() }
        }

        refreshMixCustomImageUris()
    }

    private suspend fun refreshMixCustomImageUris() = withContext(Dispatchers.IO) {
        _mixCustomImageUris.update {
            customImagesDir.listFiles()
                .orEmpty()
                .mapNotNull { file: File? -> file?.toUri()?.toString() }
                .toSet()
        }
    }

    companion object {
        private const val DIR_NAME_MIX_CUSTOM_IMAGES = "mix_custom_images"
        private const val DIR_NAME_SAVED_MIX_CUSTOM_IMAGES = "saved_mix_custom_images"

        private const val FILE_NAME_CUSTOM_MIXES_DATA_STORE = "custom_mixes.json"

        private val Context.customMixesDataStore by dataStore(
            fileName = FILE_NAME_CUSTOM_MIXES_DATA_STORE,
            serializer = CustomMixesSerializer,
        )
    }
}