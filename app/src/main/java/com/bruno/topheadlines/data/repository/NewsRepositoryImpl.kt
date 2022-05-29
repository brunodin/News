package com.bruno.topheadlines.data.repository

import com.bruno.topheadlines.data.datasource.NewsRemoteDataSource
import com.bruno.topheadlines.data.mapper.NewsMapper.toNews
import com.bruno.topheadlines.domain.repository.NewsRepository
import com.bruno.topheadlines.util.safeRequest
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