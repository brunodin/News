package com.bruno.topheadlines.presentation.ui.topheadlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno.topheadlines.domain.model.Article
import com.bruno.topheadlines.domain.model.News
import com.bruno.topheadlines.domain.model.Result
import com.bruno.topheadlines.domain.usecase.GetHeadlinesUseCase
import com.bruno.topheadlines.presentation.main.NewsViewModel.Navigation
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesAction.ArticleClickedAction
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesAction.EndReachedAction
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesUiState.ScreenState
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesViewModel.Constants.INITIAL_PAGE
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesViewModel.RetryAction.GetNewsPaginated
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesViewModel.ScreenEvent
import com.bruno.topheadlines.util.EventFlow
import com.bruno.topheadlines.util.EventFlowImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class TopHeadlinesViewModel @Inject constructor(
    private val getHeadlinesUseCase: GetHeadlinesUseCase,
) : ViewModel(), EventFlow<ScreenEvent> by EventFlowImpl() {

    val uiState = TopHeadlinesUiState()

    private lateinit var news: News
    private val articles: ArrayList<Article> = arrayListOf()
    private var page = INITIAL_PAGE

    init {
        performFirstPaginationRequest()
    }

    fun onAction(action: TopHeadlinesAction) {
        when (action) {
            is ArticleClickedAction -> performNavigationToNextScreen(action.article)
            is TopHeadlinesAction.RetryAction -> handleRetryAction(action.retryAction)
            EndReachedAction -> performNextPaginationRequest()
        }
    }

    private fun performFirstPaginationRequest() {
        uiState.screenState.value = ScreenState.Loading
        fetchGetNews(
            onSuccess = ::updateNewsValues,
            onError = { uiState.onFailure(RetryAction.GetNews) }
        )
    }

    private fun handleRetryAction(action: RetryAction) {
        when(action) {
            RetryAction.GetNews -> performFirstPaginationRequest()
            GetNewsPaginated -> performNextPaginationRequest()
        }
    }

    private fun performNextPaginationRequest() {
        val isLoading = uiState.isLoading.value
        if (isLoading || articles.size >= news.totalResults) return
        uiState.isLoading.value = true
        fetchGetNews(
            onSuccess = ::updateNewsValues,
            onError = { uiState.onFailure(GetNewsPaginated) }
        )
    }

    private fun performNavigationToNextScreen(article: Article) {
        viewModelScope.sendEvent(ScreenEvent.SaveArticle(article))
        viewModelScope.sendEvent(ScreenEvent.NavigateTo(Navigation.Details))
    }

    private fun updateNewsValues(news: News) {
        this.news = news
        this.articles.addAll(news.articles)
        page = page.inc()
        uiState.onSuccess(articles = articles.toList())
    }

    private fun fetchGetNews(
        onSuccess: (News) -> Unit,
        onError: () -> Unit
    ) = viewModelScope.launch {
        getHeadlinesUseCase.execute(page = page).also { result ->
            when (result) {
                is Result.Failure -> onError()
                is Result.Success -> onSuccess(result.data)
            }
        }
    }

    private object Constants {
        const val INITIAL_PAGE = 1
    }

    sealed class ScreenEvent {
        data class NavigateTo(val navigation: Navigation) : ScreenEvent()
        data class SaveArticle(val article: Article) : ScreenEvent()
    }

    sealed class RetryAction {
        object GetNews : RetryAction()
        object GetNewsPaginated : RetryAction()
    }
}