package com.bruno.news.data.datasource

import com.bruno.news.data.api.NewsApi
import com.bruno.news.data.response.NewsResponse
import javax.inject.Inject

class NewsRetrofitDataSourceImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val source: String
): NewsRemoteDataSource {

    override suspend fun getHeadlines(page: Int): NewsResponse {
        return newsApi.getHeadlines(sources = source, page = page)
    }
}