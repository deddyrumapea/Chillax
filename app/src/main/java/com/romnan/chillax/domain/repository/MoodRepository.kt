package com.romnan.chillax.domain.repository

import android.net.Uri
import com.romnan.chillax.domain.model.Mood
import kotlinx.coroutines.flow.Flow

interface MoodRepository {

    val moods: Flow<List<Mood>>

    val moodPresetImageUris: Flow<Set<String>>

    val moodCustomImageUris: Flow<Set<String>>

    suspend fun saveCustomMood(
        readableName: String,
        imageUri: String,
        soundIdToVolume: Map<String, Float>,
    ): Mood?

    suspend fun deleteCustomMood(moodId: String)

    suspend fun saveMoodCustomImage(uri: Uri): Uri

    suspend fun deleteMoodCustomImage(uri: String)
}