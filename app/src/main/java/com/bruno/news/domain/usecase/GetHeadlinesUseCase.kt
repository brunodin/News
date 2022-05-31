package com.bruno.news.domain.usecase

import com.bruno.news.domain.model.News
import com.bruno.news.domain.model.Result
import com.bruno.news.domain.repository.NewsRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetHeadlinesUseCase @Inject constructor(
    private val repository: NewsRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun execute(page: Int): Result<News> = withContext(dispatcher) {
        val resultNews = repository.getHeadlines(page = page)
        if(resultNews is Result.Failure) return@withContext resultNews
        val news = (resultNews as Result.Success).data
        val sortedArticles = news.articles.sortedByDescending { it.publishedAt.time }
        return@withContext Result.Success(news.copy(articles = sortedArticles))
    }
}