package com.bruno.news.domain.model

import com.bruno.news.util.DateConstant.BACKEND_FORMAT
import com.bruno.news.util.toDate
import java.util.Date

data class Article(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: Date,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
) {
    companion object {
        fun mock() = Article(
            author = "Jk",
            content = "Este artigo começa",
            description = "livro",
            publishedAt = "2022-02-20T20:37:32Z".toDate(BACKEND_FORMAT),
            source = Source("1", ""),
            title = "Titulo da manchete",
            url = "",
            urlToImage = "",
        )
    }
}