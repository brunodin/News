package com.bruno.topheadlines.data.repository

import com.bruno.topheadlines.data.datasource.NewsRemoteDataSource
import com.bruno.topheadlines.data.response.NewsResponse
import com.bruno.topheadlines.domain.model.News
import com.bruno.topheadlines.domain.model.Result
import com.bruno.topheadlines.domain.repository.NewsRepository
import com.bruno.topheadlines.util.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.net.SocketTimeoutException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NewsRepositoryTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val datasource: NewsRemoteDataSource = mockk(relaxed = true)
    private lateinit var repository: NewsRepository

    @Before
    fun setup() {
        repository = NewsRepositoryImpl(datasource, coroutineTestRule.testDispatcher)
    }

    @Test
    fun `when getHeadlines is called then request is successful`() = runTest {
        //Prepare
        coEvery { datasource.getHeadlines(PAGE) } returns NewsResponse.mock()
        //Action
        val result = repository.getHeadlines(PAGE)
        //Check
        coVerify(exactly = 1) { datasource.getHeadlines(PAGE) }
        assertEquals(News.mock(), (result as Result.Success).data)
    }

    @Test
    fun `when getHeadlines is called then request fails`() = runTest {
        //Prepare
        val errorMock = SocketTimeoutException()
        coEvery { repository.getHeadlines(PAGE) } throws errorMock
        //Action
        val result = repository.getHeadlines(PAGE)
        //Check
        assertEquals(errorMock, (result as Result.Failure).e)
    }

    private companion object {
        const val PAGE = 1
    }
}