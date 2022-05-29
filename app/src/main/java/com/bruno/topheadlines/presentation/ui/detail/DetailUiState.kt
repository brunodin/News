package com.bruno.topheadlines.presentation.ui.detail

import com.bruno.topheadlines.domain.model.Article
import kotlinx.coroutines.flow.MutableStateFlow

class DetailUiState(
    val article: MutableStateFlow<Article?> = MutableStateFlow(null)
)