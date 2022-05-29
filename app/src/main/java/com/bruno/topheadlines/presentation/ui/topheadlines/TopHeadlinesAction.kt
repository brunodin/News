package com.bruno.topheadlines.presentation.ui.topheadlines

import com.bruno.topheadlines.domain.model.Article

sealed class TopHeadlinesAction {
    data class ArticleClickedAction(val article: Article) : TopHeadlinesAction()
}
