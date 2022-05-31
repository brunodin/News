package com.bruno.news.presentation.ui.topheadlines

import com.bruno.news.domain.model.Article

sealed class TopHeadlinesAction {
    data class ArticleClickedAction(val article: Article) : TopHeadlinesAction()
    data class RetryButtonAction(val retryAction: TopHeadlinesViewModel.RetryAction) : TopHeadlinesAction()
    object EndReachedAction : TopHeadlinesAction()
}
