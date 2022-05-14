package com.romnan.chillax.core.domain.repository

import com.romnan.chillax.core.domain.model.Mood
import com.romnan.chillax.core.domain.model.Sound
import kotlinx.coroutines.flow.Flow

interface CoreRepository {
    fun getSounds(): Flow<List<Sound>>
    fun getMoods(): Flow<List<Mood>>
}