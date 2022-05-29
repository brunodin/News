package com.bruno.topheadlines.di

import com.bruno.topheadlines.data.datasource.NewsRemoteDataSource
import com.bruno.topheadlines.data.repository.NewsRepositoryImpl
import com.bruno.topheadlines.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    fun provideNewsRepository(dataSource: NewsRemoteDataSource): NewsRepository {
        return NewsRepositoryImpl(remoteDataSource = dataSource)
    }
}