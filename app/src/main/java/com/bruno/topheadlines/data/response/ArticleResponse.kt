package com.bruno.topheadlines.data.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(
    @SerializedName("author")
    val author: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("publishedAt")
    val publishedAt: String?,
    @SerializedName("source")
    val source: SourceResponse?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("urlToImage")
    val urlToImage: String?
) {
    companion object {
        fun mock() = ArticleResponse(
            author = "Jk",
            content = "Este artigo começa",
            description = "livro",
            publishedAt = "2022-02-20T20:37:32Z",
            source = SourceResponse("1", ""),
            title = "Titulo da manchete",
            url = "",
            urlToImage = "",
        )
    }
}