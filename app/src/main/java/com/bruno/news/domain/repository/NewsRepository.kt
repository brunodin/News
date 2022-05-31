package com.bruno.news.domain.repository

import com.bruno.news.domain.model.News
import com.bruno.news.domain.model.Result

interface NewsRepository {

    suspend fun getHeadlines(page: Int): Result<News>
}