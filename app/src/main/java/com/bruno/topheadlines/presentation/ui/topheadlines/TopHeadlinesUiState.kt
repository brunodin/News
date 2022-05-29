package com.bruno.topheadlines.presentation.ui.topheadlines

import com.bruno.topheadlines.domain.model.Article
import kotlinx.coroutines.flow.MutableStateFlow

class TopHeadlinesUiState(
    val screenState: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.Loading),
    val articles: MutableStateFlow<List<Article>> = MutableStateFlow(emptyList())
) {

    fun onFailure(e: Throwable) {
        this.screenState.value = ScreenState.Failure(e)
    }

    fun onSuccess(articles: List<Article>) {
        this.articles.value = articles
        this.screenState.value = ScreenState.ShowScreen
    }

    sealed class ScreenState {
        object Loading : ScreenState()
        object ShowScreen : ScreenState()
        data class Failure(val e: Throwable): ScreenState()
    }
}