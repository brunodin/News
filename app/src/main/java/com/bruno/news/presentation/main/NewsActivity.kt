package com.bruno.news.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bruno.news.presentation.main.NewsViewModel.Navigation
import com.bruno.news.presentation.ui.detail.DetailScreen
import com.bruno.news.presentation.ui.topheadlines.TopHeadlinesScreen
import com.bruno.news.util.setNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsActivity : FragmentActivity() {

    private val sharedViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigation(
            startDestination = Navigation.TopHeadlines.router,
            navGraphBuilder = ::navigationBuilder,
            navEventFlow = sharedViewModel.eventFlow,
            navEvent = ::navEvent
        )
    }

    private fun navigationBuilder(builder: NavGraphBuilder) = builder.apply {
        composable(Navigation.TopHeadlines.router) {
            TopHeadlinesScreen(sharedViewModel = sharedViewModel)
        }
        composable(Navigation.Details.router) {
            DetailScreen(sharedViewModel = sharedViewModel)
        }
    }

    private fun navEvent(navController: NavController, navScreen: Navigation) {
        navController.navigate(route = navScreen.router)
    }
}