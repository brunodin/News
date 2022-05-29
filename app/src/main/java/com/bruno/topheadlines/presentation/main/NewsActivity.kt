package com.bruno.topheadlines.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bruno.topheadlines.presentation.main.NewsViewModel.Navigation
import com.bruno.topheadlines.presentation.theme.Primary100
import com.bruno.topheadlines.presentation.ui.detail.DetailScreen
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesScreen
import com.bruno.topheadlines.util.setNavigationContent
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsActivity : ComponentActivity() {

    private val viewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigationContent(
            startDestination = Navigation.TopHeadlines.router,
            navGraphBuilder = ::navigationBuilder,
            navEventFlow = viewModel.eventFlow,
            navEvent = ::navEvent
        ) {
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(
                color = Primary100
            )
        }
    }

    private fun navigationBuilder(builder: NavGraphBuilder) = builder.apply {
        composable(Navigation.TopHeadlines.router) {
            TopHeadlinesScreen(
                onNext = { article ->
                    viewModel.article = article
                    viewModel.navigate(Navigation.Details)
                }
            )
        }
        composable(Navigation.Details.router) {
            DetailScreen(sharedViewModel = viewModel)
        }
    }

    private fun navEvent(navController: NavController, navScreen: Navigation) {
        navController.navigate(route = navScreen.router)
    }
}