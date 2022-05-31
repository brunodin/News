package com.bruno.news.di

import com.bruno.news.BuildConfig
import com.bruno.news.data.api.NewsApi
import com.bruno.news.data.datasource.NewsRemoteDataSource
import com.bruno.news.data.datasource.NewsRetrofitDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

//@InstallIn(SingletonComponent::class)
//@Module
class AppModule {

//    @Provides
//    fun provideNewsRemoteDataSource(app: ): NewsRemoteDataSource {
//        return NewsRetrofitDataSourceImpl(newsApi = api, source = BuildConfig.SOURCE_NAME)
//    }
}