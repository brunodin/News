package com.bruno.topheadlines.data.mapper

import com.bruno.topheadlines.data.mapper.ArticleMapper.toArticle
import com.bruno.topheadlines.data.response.NewsResponse
import com.bruno.topheadlines.domain.model.News

object NewsMapper {

    fun NewsResponse.toNews() = News(
        articles = this.articles?.map { it.toArticle() } ?: emptyList(),
        status = this.status.orEmpty(),
        totalResults = this.totalResults ?: 0,
    )
}