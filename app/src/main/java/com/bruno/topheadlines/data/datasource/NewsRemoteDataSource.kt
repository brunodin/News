package com.bruno.topheadlines.data.datasource

import com.bruno.topheadlines.data.response.NewsResponse

interface NewsRemoteDataSource {

    suspend fun getHeadlines(page: Int): NewsResponse
}