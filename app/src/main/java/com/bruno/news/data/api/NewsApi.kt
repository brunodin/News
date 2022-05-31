package com.bruno.news.data.api

import com.bruno.news.data.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getHeadlines(
        @Query("sources") sources: String,
        @Query("page") page: Int,
    ): NewsResponse
}