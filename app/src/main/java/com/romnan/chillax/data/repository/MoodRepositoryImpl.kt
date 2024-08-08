package com.romnan.chillax.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.datastore.dataStore
import com.romnan.chillax.data.model.CustomMoodList
import com.romnan.chillax.data.model.CustomMoodSerializable
import com.romnan.chillax.data.serializer.CustomMoodsSerializer
import com.romnan.chillax.data.source.AppDataSource
import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.repository.MoodRepository
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

class MoodRepositoryImpl(
    private val appContext: Context,
    private val appScope: CoroutineScope,
    private val appDataSource: AppDataSource,
) : MoodRepository {

    private val customImagesDir = File(
        appContext.filesDir,
        DIR_NAME_MOOD_CUSTOM_IMAGES,
    ).apply { mkdirs() }

    private val savedMoodCustomImagesDir = File(
        appContext.filesDir,
        DIR_NAME_SAVED_MOOD_CUSTOM_IMAGES,
    ).apply { mkdirs() }

    override val moods: Flow<List<Mood>>
        get() = appContext.customMoodsDataStore.data.map { moodList: CustomMoodList ->
            moodList.customMoods.reversed().map { customMood: CustomMoodSerializable ->
                Mood(
                    uuid = customMood.uuid,
                    customMood = customMood,
                )
            } + appDataSource.presetMoods
        }
    override val moodPresetImageUris: Flow<Set<String>>
        get() = flowOf(appDataSource.moodImageUris)

    private val _moodCustomImageUris = MutableStateFlow(setOf<String>())
    override val moodCustomImageUris: Flow<Set<String>>
        get() = _moodCustomImageUris

    init {
        appScope.launch {
            refreshMoodCustomImageUris()
        }
    }

    override suspend fun saveCustomMood(
        readableName: String,
        imageUri: String,
        soundIdToVolume: Map<String, Float>,
    ): Mood? = withContext(Dispatchers.IO) {

        try {
            val uuid = UUID.randomUUID().toString()

            val saveImageUri = when (_moodCustomImageUris.value.contains(imageUri)) {
                true -> {
                    val file = File(savedMoodCustomImagesDir, Mood.CUSTOM_MOOD_ID_PREFIX.plus(uuid))

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

            appContext.customMoodsDataStore.updateData { customMoodList: CustomMoodList ->
                customMoodList.copy(
                    customMoods = customMoodList.customMoods.toPersistentList().add(
                        CustomMoodSerializable(
                            uuid = uuid,
                            readableName = readableName,
                            imageUri = saveImageUri,
                            soundIdToVolume = soundIdToVolume,
                        )
                    ),
                )
            }

            moods.firstOrNull()?.find { mood: Mood ->
                mood.id == Mood.CUSTOM_MOOD_ID_PREFIX.plus(uuid)
            }
        } catch (e: Exception) {
            logcat { e.asLog() }
            null
        }
    }

    override suspend fun deleteCustomMood(
        moodId: String,
    ): Unit = withContext(Dispatchers.IO) {
        appContext.customMoodsDataStore.updateData { customMoodList: CustomMoodList ->
            customMoodList.copy(
                customMoods = customMoodList.customMoods.toPersistentList()
                    .removeAll { customMood: CustomMoodSerializable ->
                        customMood.uuid == moodId.removePrefix(Mood.CUSTOM_MOOD_ID_PREFIX)
                    },
            )
        }

        File(savedMoodCustomImagesDir, moodId).deleteRecursively()
    }

    override suspend fun saveMoodCustomImage(
        uri: Uri,
    ): Uri = withContext(Dispatchers.IO) {
        val file = File(customImagesDir, UUID.randomUUID().toString())

        file.outputStream().use { outputStream: OutputStream ->
            appContext.contentResolver.openInputStream(uri)?.use { inputStream: InputStream ->
                outputStream.write(inputStream.readBytes())
            }
        }

        refreshMoodCustomImageUris()

        file.toUri()
    }

    override suspend fun deleteMoodCustomImage(
        uri: String,
    ): Unit = withContext(Dispatchers.IO) {
        try {
            uri.toUri().toFile().delete()
        } catch (e: Exception) {
            logcat { e.asLog() }
        }

        refreshMoodCustomImageUris()
    }

    private suspend fun refreshMoodCustomImageUris() = withContext(Dispatchers.IO) {
        _moodCustomImageUris.update {
            customImagesDir.listFiles()
                .orEmpty()
                .mapNotNull { file: File? -> file?.toUri()?.toString() }
                .toSet()
        }
    }

    companion object {
        private const val DIR_NAME_MOOD_CUSTOM_IMAGES = "mood_custom_images"
        private const val DIR_NAME_SAVED_MOOD_CUSTOM_IMAGES = "saved_mood_custom_images"

        private const val FILE_NAME_CUSTOM_MOODS_DATA_STORE = "custom_moods.json"

        private val Context.customMoodsDataStore by dataStore(
            fileName = FILE_NAME_CUSTOM_MOODS_DATA_STORE,
            serializer = CustomMoodsSerializer,
        )
    }
}