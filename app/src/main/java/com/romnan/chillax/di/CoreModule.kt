package com.romnan.chillax.di

import com.romnan.chillax.core.data.repository.DefaultCoreRepository
import com.romnan.chillax.core.domain.repository.CoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideRepository(): CoreRepository = DefaultCoreRepository()
}