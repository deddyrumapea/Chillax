package com.romnan.chillax.di

import android.content.Context
import com.romnan.chillax.data.model.TimeSourceImpl
import com.romnan.chillax.data.repository.AppSettingsRepositoryImpl
import com.romnan.chillax.data.repository.PlayerRepositoryImpl
import com.romnan.chillax.data.repository.SleepTimerRepositoryImpl
import com.romnan.chillax.data.util.CountDownTimer
import com.romnan.chillax.domain.model.TimeSource
import com.romnan.chillax.domain.notification.NotificationHelper
import com.romnan.chillax.domain.repository.AppSettingsRepository
import com.romnan.chillax.domain.repository.PlayerRepository
import com.romnan.chillax.domain.repository.SleepTimerRepository
import com.romnan.chillax.presentation.notification.NotificationHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope = CoroutineScope(context = SupervisorJob())

    @Provides
    @Singleton
    fun providePlayerRepository(
        @ApplicationContext appContext: Context,
        @ApplicationScope appScope: CoroutineScope,
        sleepTimerRepository: SleepTimerRepository,
        countDownTimer: CountDownTimer,
    ): PlayerRepository = PlayerRepositoryImpl(
        appContext = appContext,
        appScope = appScope,
        sleepTimerRepository = sleepTimerRepository,
        countDownTimer = countDownTimer,
    )

    @Provides
    @Singleton
    fun provideAppSettingsRepository(
        @ApplicationContext appContext: Context,
    ): AppSettingsRepository = AppSettingsRepositoryImpl(
        appContext = appContext,
    )

    @Provides
    @Singleton
    fun provideNotificationHelper(
        @ApplicationContext appContext: Context
    ): NotificationHelper = NotificationHelperImpl(
        appContext = appContext
    )

    @Provides
    @Singleton
    fun provideSleepTimerRepository(
        @ApplicationContext appContext: Context,
        @ApplicationScope appScope: CoroutineScope,
    ): SleepTimerRepository = SleepTimerRepositoryImpl(
        appContext = appContext,
    )

    @Provides
    @Singleton
    fun provideCountDownTimer(
        @ApplicationScope appScope: CoroutineScope,
        timeSource: TimeSource,
    ): CountDownTimer = CountDownTimer(
        appScope = appScope,
        timeSource = timeSource,
    )

    @Provides
    @Singleton
    fun provideTimeSource(): TimeSource = TimeSourceImpl()
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope