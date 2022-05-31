package com.bruno.news.domain.model

import com.bruno.news.util.DateConstant.BACKEND_FORMAT
import com.bruno.news.util.toDate

data class News(
    var articles: List<Article>,
    val status: String,
    val totalResults: Int
) {
    companion object {
        fun mock() = News(
            articles = listOf(
                Article.mock(),
                Article.mock().copy(publishedAt = "2022-03-13T20:37:32Z".toDate(BACKEND_FORMAT)),
                Article.mock().copy(publishedAt = "2022-05-25T20:37:32Z".toDate(BACKEND_FORMAT))
            ),
            status = "Active",
            totalResults = 0
        )
    }
}
