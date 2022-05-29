package com.bruno.topheadlines.presentation.ui.topheadlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno.topheadlines.domain.model.Article
import com.bruno.topheadlines.domain.model.News
import com.bruno.topheadlines.domain.model.Result
import com.bruno.topheadlines.domain.usecase.GetHeadlinesUseCase
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesAction.ArticleClickedAction
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesUiState.ScreenState
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesViewModel.Constants.INITIAL_PAGE
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesViewModel.ScreenEvent
import com.bruno.topheadlines.util.EventFlow
import com.bruno.topheadlines.util.EventFlowImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class TopHeadlinesViewModel @Inject constructor(
    private val getHeadlinesUseCase: GetHeadlinesUseCase
) : ViewModel(), EventFlow<ScreenEvent> by EventFlowImpl() {

    val uiState = TopHeadlinesUiState()
    private lateinit var news: News
    private var page = INITIAL_PAGE

    init {
        fetchGetNews()
    }

    fun onAction(action: TopHeadlinesAction) {
        when (action) {
            is ArticleClickedAction -> viewModelScope.sendEvent(ScreenEvent.NavigateTo(action.article))
        }
    }

    private fun fetchGetNews() = viewModelScope.launch {
        uiState.screenState.value = ScreenState.Loading
        val result = getHeadlinesUseCase.execute(page = page)
        when (result) {
            is Result.Failure -> uiState.onFailure(result.e)
            is Result.Success -> handleGetHeadlinesSuccessResult(result.data)
        }
    }

    private fun handleGetHeadlinesSuccessResult(news: News) {
        this.news = news
        uiState.onSuccess(news.articles)
    }

    private object Constants {
        const val INITIAL_PAGE = 0
    }

    sealed class ScreenEvent {
        data class NavigateTo(val article: Article) : ScreenEvent()
    }
}