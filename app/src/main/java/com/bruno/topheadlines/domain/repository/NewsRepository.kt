package com.bruno.topheadlines.domain.repository

import com.bruno.topheadlines.domain.model.News
import com.bruno.topheadlines.domain.model.Result

interface NewsRepository {

    suspend fun getHeadlines(page: Int): Result<News>
}