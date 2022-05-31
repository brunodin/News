package com.bruno.news.presentation.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno.news.domain.model.Article
import com.bruno.news.presentation.ui.detail.DetailAction.BackButtonAction
import com.bruno.news.presentation.ui.detail.DetailViewModel.ScreenEvent
import com.bruno.news.util.EventFlow
import com.bruno.news.util.EventFlowImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(): ViewModel(), EventFlow<ScreenEvent> by EventFlowImpl() {

    val uiState = DetailUiState()

    fun setup(article: Article?) {
        uiState.article.value = article
    }

    fun onAction(action: DetailAction) {
        when(action) {
            BackButtonAction -> viewModelScope.sendEvent(ScreenEvent.GoBack)
        }
    }

    sealed class ScreenEvent {
        object GoBack : ScreenEvent()
    }
}