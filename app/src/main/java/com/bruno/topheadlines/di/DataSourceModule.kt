package com.bruno.topheadlines.di

import com.bruno.topheadlines.BuildConfig
import com.bruno.topheadlines.data.api.NewsApi
import com.bruno.topheadlines.data.datasource.NewsRemoteDataSource
import com.bruno.topheadlines.data.datasource.NewsRetrofitDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DataSourceModule {

    @Provides
    fun provideNewsRemoteDataSource(api: NewsApi): NewsRemoteDataSource {
        return NewsRetrofitDataSourceImpl(newsApi = api, BuildConfig.SOURCE_NAME)
    }
}