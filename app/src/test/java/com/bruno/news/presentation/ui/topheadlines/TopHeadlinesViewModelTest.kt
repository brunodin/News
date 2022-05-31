package com.bruno.news.presentation.ui.topheadlines

import com.bruno.news.domain.model.Article
import com.bruno.news.domain.model.News
import com.bruno.news.domain.model.Result
import com.bruno.news.domain.usecase.GetHeadlinesUseCase
import com.bruno.news.presentation.main.NewsViewModel.*
import com.bruno.news.presentation.ui.topheadlines.TopHeadlinesAction.ArticleClickedAction
import com.bruno.news.presentation.ui.topheadlines.TopHeadlinesUiState.ScreenState
import com.bruno.news.presentation.ui.topheadlines.TopHeadlinesViewModel.RetryAction.GetNews
import com.bruno.news.presentation.ui.topheadlines.TopHeadlinesViewModel.RetryAction.GetNewsPaginated
import com.bruno.news.presentation.ui.topheadlines.TopHeadlinesViewModel.ScreenEvent
import com.bruno.news.util.CoroutineTestRule
import com.bruno.news.util.Fingerprint
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import java.net.SocketTimeoutException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TopHeadlinesViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var viewModel: TopHeadlinesViewModel
    private val getHeadlinesUseCase: GetHeadlinesUseCase = mockk(relaxed = true)
    private val fingerprint: Fingerprint = mockk(relaxed = true)
    private val screenEvent: (ScreenEvent) -> Unit = mockk(relaxed = true)
    private val screenState: (ScreenState) -> Unit = mockk(relaxed = true)
    private val articles: (List<Article>) -> Unit = mockk(relaxed = true)
    private val isLoading: (Boolean) -> Unit = mockk(relaxed = true)

    @Before
    fun setup() {
        viewModel = TopHeadlinesViewModel(getHeadlinesUseCase, fingerprint)
        prepareObservables()
    }

    @After
    fun clear() {
        coroutineTestRule.testScope.cancel()
    }

    @Test
    fun `when viewModel starts then screen event has show fingerprint`() {
        //Check
        verify {
            screenEvent(ScreenEvent.RequestFingerprint(fingerprint))
        }
    }

    @Test
    fun `when setup is called then request is successful`() {
        //Prepare
        val articlesMock = News.mock().articles
        coEvery { getHeadlinesUseCase.execute(page = PAGE) } returns Result.Success(News.mock())
        //Action
        viewModel.setup()
        //Check
        verifyOrder {
            screenState(ScreenState.Loading)
            articles(articlesMock)
            screenState(ScreenState.ShowScreen)
        }
    }

    @Test
    fun `when setup is called then request fails`() {
        //Prepare
        coEvery { getHeadlinesUseCase.execute(page = PAGE) } returns Result.Failure(SocketTimeoutException())
        //Action
        viewModel.setup()
        //Check
        verifyOrder {
            screenState(ScreenState.Loading)
            screenState(ScreenState.Failure(GetNews))
        }
    }

    @Test
    fun `when onAction is called with ArticleClickedAction then save articles and navigate to next screen`() {
        //Prepare
        coEvery { getHeadlinesUseCase.execute(page = PAGE) } returns Result.Failure(SocketTimeoutException())
        //Action
        viewModel.onAction(ArticleClickedAction(Article.mock()))
        //Check
        verifyOrder {
            screenEvent(ScreenEvent.SaveArticle(Article.mock()))
            screenEvent(ScreenEvent.NavigateTo(Navigation.Details))
        }
    }

    @Test
    fun `when onAction is called with RetryAction and GetNews then request succeeds`() {
        //Prepare
        val articlesMock = News.mock().articles
        coEvery { getHeadlinesUseCase.execute(page = PAGE) } returns Result.Success(News.mock())
        //Action
        viewModel.onAction(TopHeadlinesAction.RetryButtonAction(GetNews))
        //Check
        verifyOrder {
            screenState(ScreenState.Loading)
            articles(articlesMock)
            screenState(ScreenState.ShowScreen)
        }
    }

    @Test
    fun `when onAction is called with EndReachedAction then paginated request succeeds`() {
        //Prepare
        val news = News.mock().copy(totalResults = TOTAL_RESULT)
        val articlesMock = arrayListOf<Article>()
        articlesMock.addAll(news.articles)
        coEvery { getHeadlinesUseCase.execute(page = NEXT_PAGE) } returns Result.Success(News.mock())
        viewModel.setupVariables(news = news, articles = articlesMock, page = NEXT_PAGE)
        articlesMock.addAll(news.articles)
        //Action
        viewModel.onAction(TopHeadlinesAction.EndReachedAction)
        //Check
        verifyOrder {
            isLoading(true)
            articles(articlesMock)
            isLoading(false)
        }
    }

    @Test
    fun `when onAction is called with EndReachedAction then paginated request fails`() {
        //Prepare
        val news = News.mock().copy(totalResults = TOTAL_RESULT)
        coEvery { getHeadlinesUseCase.execute(page = NEXT_PAGE) } returns Result.Failure(SocketTimeoutException())
        viewModel.setupVariables(news = news, articles = news.articles, page = NEXT_PAGE)
        //Action
        viewModel.onAction(TopHeadlinesAction.EndReachedAction)
        //Check
        verifyOrder {
            isLoading(true)
            screenState(ScreenState.Failure(GetNewsPaginated))
            isLoading(false)
        }
    }

    private fun prepareObservables() = coroutineTestRule.testScope.run {
        launch { viewModel.uiState.screenState.collect { screenState(it) } }
        launch { viewModel.uiState.articles.collect { articles(it) } }
        launch { viewModel.uiState.isLoading.collect { isLoading(it) } }
        launch { viewModel.eventFlow.collect { screenEvent(it) } }
    }

    private companion object {
        const val PAGE = 1
        const val NEXT_PAGE = 1
        const val TOTAL_RESULT = 100
    }
}