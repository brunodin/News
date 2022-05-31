package com.bruno.news.data.mapper

import com.bruno.news.data.mapper.ArticleMapper.toArticle
import com.bruno.news.data.response.NewsResponse
import com.bruno.news.domain.model.News

object NewsMapper {

    fun NewsResponse.toNews() = News(
        articles = this.articles?.map { it.toArticle() } ?: emptyList(),
        status = this.status.orEmpty(),
        totalResults = this.totalResults ?: 0,
    )
}