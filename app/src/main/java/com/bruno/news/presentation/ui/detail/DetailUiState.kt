package com.bruno.news.presentation.ui.detail

import com.bruno.news.domain.model.Article
import kotlinx.coroutines.flow.MutableStateFlow

class DetailUiState(
    val article: MutableStateFlow<Article?> = MutableStateFlow(null)
)