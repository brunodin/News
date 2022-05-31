package com.bruno.news.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno.news.domain.model.Article
import com.bruno.news.presentation.main.NewsViewModel.Navigation
import com.bruno.news.util.EventFlow
import com.bruno.news.util.EventFlowImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(): ViewModel(), EventFlow<Navigation> by EventFlowImpl() {

    var article: Article? = null

    fun navigate(navigation: Navigation) {
        viewModelScope.sendEvent(navigation)
    }

    sealed class Navigation(val router: String) {
        object TopHeadlines : Navigation("top_headlines")
        object Details : Navigation("details")
    }
}