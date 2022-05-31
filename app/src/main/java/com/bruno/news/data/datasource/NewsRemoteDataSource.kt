package com.bruno.news.data.datasource

import com.bruno.news.data.response.NewsResponse

interface NewsRemoteDataSource {

    suspend fun getHeadlines(page: Int): NewsResponse
}