package com.chicken.minerunner.core.di

import com.chicken.minerunner.core.domain.repository.GameRepository
import com.chicken.minerunner.core.domain.usecase.GenerateTrackRowUseCase
import com.chicken.minerunner.core.data.InMemoryGameRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGameRepository(): GameRepository = InMemoryGameRepository()

    @Provides
    @Singleton
    fun provideGenerateTrackRowUseCase(): GenerateTrackRowUseCase = GenerateTrackRowUseCase()
}
