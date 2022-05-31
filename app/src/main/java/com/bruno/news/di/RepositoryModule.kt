package com.bruno.news.di

import com.bruno.news.data.datasource.NewsRemoteDataSource
import com.bruno.news.data.repository.NewsRepositoryImpl
import com.bruno.news.domain.repository.NewsRepository
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