package com.bruno.topheadlines.presentation.ui.topheadlines

import com.bruno.topheadlines.domain.model.Article

sealed class TopHeadlinesAction {
    data class ArticleClickedAction(val article: Article) : TopHeadlinesAction()
    data class RetryAction(val retryAction: TopHeadlinesViewModel.RetryAction) : TopHeadlinesAction()
    object EndReachedAction : TopHeadlinesAction()
}
