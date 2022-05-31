package com.bruno.news.data.repository

import com.bruno.news.data.datasource.NewsRemoteDataSource
import com.bruno.news.data.mapper.NewsMapper.toNews
import com.bruno.news.domain.repository.NewsRepository
import com.bruno.news.util.safeRequest
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: NewsRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): NewsRepository {

    override suspend fun getHeadlines(page: Int) = safeRequest(dispatcher) {
        remoteDataSource.getHeadlines(page = page).toNews()
    }
}