package com.romnan.chillax.di

import android.content.Context
import com.romnan.chillax.core.data.repository.DefaultCoreRepository
import com.romnan.chillax.core.data.repository.DefaultPlayerStateRepository
import com.romnan.chillax.core.domain.notification.NotificationHelper
import com.romnan.chillax.core.domain.repository.CoreRepository
import com.romnan.chillax.core.domain.repository.PlayerStateRepository
import com.romnan.chillax.core.presentation.notification.DefaultNotificationHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideRepository(): CoreRepository = DefaultCoreRepository()

    @Provides
    @Singleton
    fun providePlayerStateRepository(
        @ApplicationContext appContext: Context
    ): PlayerStateRepository = DefaultPlayerStateRepository(
        appContext = appContext
    )

    @Provides
    @Singleton
    fun provideNotificationHelper(
        @ApplicationContext appContext: Context
    ): NotificationHelper = DefaultNotificationHelper(
        appContext = appContext
    )
}