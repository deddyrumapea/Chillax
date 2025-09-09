package com.romnan.chillax.domain.repository

import kotlinx.coroutines.flow.Flow

interface RemoteConfigRepository {
    val appUpdateRequired: Flow<Boolean>
}