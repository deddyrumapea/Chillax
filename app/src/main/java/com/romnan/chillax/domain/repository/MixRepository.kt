package com.romnan.chillax.domain.repository

import android.net.Uri
import com.romnan.chillax.domain.model.Mix
import kotlinx.coroutines.flow.Flow

interface MixRepository {

    val mixes: Flow<List<Mix>>

    val mixPresetImageUris: Flow<Set<String>>

    val mixCustomImageUris: Flow<Set<String>>

    suspend fun saveCustomMix(
        readableName: String,
        imageUri: String,
        soundIdToVolume: Map<String, Float>,
    ): Mix?

    suspend fun deleteCustomMix(mixId: String)

    suspend fun saveMixCustomImage(uri: Uri): Uri

    suspend fun deleteMixCustomImage(uri: String)
}