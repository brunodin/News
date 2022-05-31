package com.bruno.news.domain.usecase

import com.bruno.news.domain.model.Article
import com.bruno.news.domain.model.News
import com.bruno.news.domain.model.Result
import com.bruno.news.domain.repository.NewsRepository
import com.bruno.news.util.CoroutineTestRule
import com.bruno.news.util.DateConstant.BACKEND_FORMAT
import com.bruno.news.util.toDate
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.net.SocketTimeoutException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetHeadlinesUseCaseTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val repository: NewsRepository = mockk()
    private lateinit var useCase: GetHeadlinesUseCase

    @Before
    fun setup() {
        useCase = GetHeadlinesUseCase(repository)
    }

    @Test
    fun `when execute is called then request is successful`() = runTest {
        //Prepare
        val sortedNews = News.mock().copy(
            articles = listOf(
                Article.mock().copy(publishedAt = "2022-05-25T20:37:32Z".toDate(BACKEND_FORMAT)),
                Article.mock().copy(publishedAt = "2022-03-13T20:37:32Z".toDate(BACKEND_FORMAT)),
                Article.mock(),
            )
        )
        coEvery { repository.getHeadlines(PAGE) } returns Result.Success(News.mock())
        //Action
        val result = useCase.execute(PAGE)
        //Check
        coVerify(exactly = 1) { repository.getHeadlines(PAGE) }
        assertNotEquals(News.mock(), (result as Result.Success).data)
        assertEquals(sortedNews, result.data)
    }

    @Test
    fun `when execute is called then request fails`() = runTest {
        //Prepare
        val errorMock = SocketTimeoutException()
        coEvery { repository.getHeadlines(PAGE) } returns Result.Failure(errorMock)
        //Action
        val result = useCase.execute(PAGE)
        //Check
        assertEquals(errorMock, (result as Result.Failure).e)
    }

    private companion object {
        const val PAGE = 1
    }
}