package com.romnan.chillax.di

import android.content.Context
import com.romnan.chillax.data.notification.NotificationHelperImpl
import com.romnan.chillax.data.repository.PlayerRepositoryImpl
import com.romnan.chillax.domain.notification.NotificationHelper
import com.romnan.chillax.domain.repository.PlayerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePlayerRepository(
        @ApplicationContext appContext: Context
    ): PlayerRepository = PlayerRepositoryImpl(
        appContext = appContext
    )

    @Provides
    @Singleton
    fun provideNotificationHelper(
        @ApplicationContext appContext: Context
    ): NotificationHelper = NotificationHelperImpl(
        appContext = appContext
    )
}