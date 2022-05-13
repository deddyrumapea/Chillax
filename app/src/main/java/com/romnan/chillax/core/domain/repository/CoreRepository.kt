package com.romnan.chillax.core.domain.repository

import com.romnan.chillax.core.domain.model.Mood
import com.romnan.chillax.core.domain.model.Sound

interface CoreRepository {
    fun getSounds(): List<Sound>
    fun getMoods(): List<Mood>
}