package com.bruno.topheadlines.data.mapper

import com.bruno.topheadlines.data.mapper.SourceMapper.toSource
import com.bruno.topheadlines.data.response.ArticleResponse
import com.bruno.topheadlines.domain.model.Article
import com.bruno.topheadlines.util.DateConstant
import com.bruno.topheadlines.util.toDate
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