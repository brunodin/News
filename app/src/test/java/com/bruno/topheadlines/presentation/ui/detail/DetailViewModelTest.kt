package com.bruno.topheadlines.presentation.ui.detail

import com.bruno.topheadlines.domain.model.Article
import com.bruno.topheadlines.domain.model.News
import com.bruno.topheadlines.domain.model.Result
import com.bruno.topheadlines.presentation.ui.detail.DetailAction.BackButtonAction
import com.bruno.topheadlines.presentation.ui.detail.DetailViewModel.ScreenEvent
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesAction
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesUiState
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesViewModelTest
import com.bruno.topheadlines.util.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var viewModel: DetailViewModel
    private val screenEvent: (ScreenEvent) -> Unit = mockk(relaxed = true)
    private val article: (Article?) -> Unit = mockk(relaxed = true)

    @Before
    fun setup() {
        viewModel = DetailViewModel()
        prepareObservables()
    }

    @After
    fun clear() {
        coroutineTestRule.testScope.cancel()
    }

    @Test
    fun `when setup is called then article updates`() {
        //Action
        viewModel.setup(Article.mock())
        //Check
        verify {
            article(Article.mock())
        }
    }

    @Test
    fun `when onAction is called with BackButtonAction then screenEvent has GoBack`() {
        //Action
        viewModel.onAction(BackButtonAction)
        //Check
        verify {
            screenEvent(ScreenEvent.GoBack)
        }
    }

    private fun prepareObservables() = coroutineTestRule.testScope.run {
        launch { viewModel.uiState.article.collect { article(it) } }
        launch { viewModel.eventFlow.collect { screenEvent(it) } }
    }
}