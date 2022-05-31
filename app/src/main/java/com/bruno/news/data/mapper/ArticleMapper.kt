package com.bruno.news.data.mapper

import com.bruno.news.data.mapper.SourceMapper.toSource
import com.bruno.news.data.response.ArticleResponse
import com.bruno.news.domain.model.Article
import com.bruno.news.util.DateConstant
import com.bruno.news.util.toDate
import java.util.Date

object ArticleMapper {

    fun ArticleResponse.toArticle() = Article(
        author = this.author.orEmpty(),
        content = this.content.orEmpty(),
        description = this.description.orEmpty(),
        publishedAt = this.publishedAt?.toDate(DateConstant.BACKEND_FORMAT) ?: Date(),
        source = this.source.toSource(),
        title = this.title.orEmpty(),
        url = this.url.orEmpty(),
        urlToImage = this.urlToImage.orEmpty(),
    )
}