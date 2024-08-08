package com.romnan.chillax.di

import android.content.Context
import android.os.SystemClock
import com.romnan.chillax.data.repository.AppSettingsRepositoryImpl
import com.romnan.chillax.data.repository.MoodRepositoryImpl
import com.romnan.chillax.data.repository.PlayerRepositoryImpl
import com.romnan.chillax.data.repository.SleepTimerRepositoryImpl
import com.romnan.chillax.data.source.AppDataSource
import com.romnan.chillax.data.util.CountDownTimer
import com.romnan.chillax.domain.notification.NotificationHelper
import com.romnan.chillax.domain.repository.AppSettingsRepository
import com.romnan.chillax.domain.repository.MoodRepository
import com.romnan.chillax.domain.repository.PlayerRepository
import com.romnan.chillax.domain.repository.SleepTimerRepository
import com.romnan.chillax.domain.util.TimeSource
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
    fun provideAppDataSource(
        @ApplicationContext appContext: Context,
    ): AppDataSource = AppDataSource(
        appContext = appContext,
    )

    @Provides
    @Singleton
    fun providePlayerRepository(
        @ApplicationContext appContext: Context,
        @ApplicationScope appScope: CoroutineScope,
        sleepTimerRepository: SleepTimerRepository,
        countDownTimer: CountDownTimer,
        appDataSource: AppDataSource,
        moodRepository: MoodRepository,
    ): PlayerRepository = PlayerRepositoryImpl(
        appContext = appContext,
        appScope = appScope,
        sleepTimerRepository = sleepTimerRepository,
        countDownTimer = countDownTimer,
        appDataSource = appDataSource,
        moodRepository = moodRepository,
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
    fun provideMoodRepository(
        @ApplicationContext appContext: Context,
        @ApplicationScope appScope: CoroutineScope,
        appDataSource: AppDataSource,
    ): MoodRepository = MoodRepositoryImpl(
        appContext = appContext,
        appScope = appScope,
        appDataSource = appDataSource,
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
    fun provideTimeSource(): TimeSource {
        return object : TimeSource {
            override val elapsedRealTime: Long
                get() = SystemClock.elapsedRealtime()
        }
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope