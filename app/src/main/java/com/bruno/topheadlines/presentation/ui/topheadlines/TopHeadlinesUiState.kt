package com.bruno.topheadlines.presentation.ui.topheadlines

import com.bruno.topheadlines.domain.model.Article
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesViewModel.*
import kotlinx.coroutines.flow.MutableStateFlow

class TopHeadlinesUiState(
    val screenState: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.Loading),
    val articles: MutableStateFlow<List<Article>> = MutableStateFlow(emptyList()),
    val isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
) {

    fun onFailure(retryAction: RetryAction) {
        this.screenState.value = ScreenState.Failure(retryAction)
        this.isLoading.value = false
    }

    fun onSuccess(articles: List<Article>) {
        this.articles.value = articles
        this.isLoading.value = false
        this.screenState.value = ScreenState.ShowScreen
    }

    sealed class ScreenState {
        object Loading : ScreenState()
        object ShowScreen : ScreenState()
        data class Failure(val retryAction: RetryAction): ScreenState()
    }
}