package com.bruno.topheadlines.presentation.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno.topheadlines.domain.model.Article
import com.bruno.topheadlines.presentation.ui.detail.DetailViewModel.ScreenEvent
import com.bruno.topheadlines.util.EventFlow
import com.bruno.topheadlines.util.EventFlowImpl
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
            DetailAction.BackButtonAction -> viewModelScope.sendEvent(ScreenEvent.GoBack)
        }
    }

    sealed class ScreenEvent {
        object GoBack : ScreenEvent()
    }
}