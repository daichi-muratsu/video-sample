package com.example.videosample.di

import com.example.videosample.data.repository.MockVideoRepositoryImpl
import com.example.videosample.domain.repository.VideoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindVideoRepository(
        videoRepositoryImpl: MockVideoRepositoryImpl
    ): VideoRepository
}