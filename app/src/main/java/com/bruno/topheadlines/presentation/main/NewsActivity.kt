package com.bruno.topheadlines.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bruno.topheadlines.presentation.main.NewsViewModel.Navigation
import com.bruno.topheadlines.presentation.ui.detail.DetailScreen
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesScreen
import com.bruno.topheadlines.util.setNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsActivity : ComponentActivity() {

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