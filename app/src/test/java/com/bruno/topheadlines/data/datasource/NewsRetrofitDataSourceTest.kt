package com.bruno.topheadlines.data.datasource

import com.bruno.topheadlines.data.api.NewsApi
import com.bruno.topheadlines.data.response.NewsResponse
import com.bruno.topheadlines.util.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.net.SocketTimeoutException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NewsRetrofitDataSourceTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val api: NewsApi = mockk(relaxed = true)
    private lateinit var datasource: NewsRemoteDataSource

    @Before
    fun setup() {
        datasource = NewsRetrofitDataSourceImpl(api, "BBC")
    }

    @Test
    fun `when getHeadlines is called then request is successful`() = runTest {
        //Prepare
        coEvery { api.getHeadlines(page = PAGE, source = SOURCE) } returns NewsResponse.mock()
        //Action
        val result = datasource.getHeadlines(PAGE)
        //Check
        coVerify(exactly = 1) { api.getHeadlines(page = PAGE, source = SOURCE) }
        Assert.assertEquals(NewsResponse.mock(), result)
    }

    @Test(expected = SocketTimeoutException::class)
    fun `when getHeadlines is called then request fails`() = runTest {
        coEvery { api.getHeadlines(page = PAGE, source = SOURCE) } throws SocketTimeoutException()
        //Action
        datasource.getHeadlines(PAGE)
    }

    private companion object {
        const val PAGE = 1
        const val SOURCE = "BBC"
    }
}