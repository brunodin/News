package com.bruno.topheadlines.data.response


import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("articles")
    val articles: List<ArticleResponse>?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("totalResults")
    val totalResults: Int?
) {
    companion object {
        fun mock() = NewsResponse(
            articles = listOf(
                ArticleResponse.mock(),
                ArticleResponse.mock().copy(publishedAt = "2022-03-13T20:37:32Z"),
                ArticleResponse.mock().copy(publishedAt = "2022-05-25T20:37:32Z")
            ),
            status = "Active",
            totalResults = 0
        )
    }
}
