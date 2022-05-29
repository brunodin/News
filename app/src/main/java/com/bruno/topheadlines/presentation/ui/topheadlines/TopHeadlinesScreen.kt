package com.bruno.topheadlines.presentation.ui.topheadlines

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.bruno.topheadlines.BuildConfig
import com.bruno.topheadlines.domain.model.Article
import com.bruno.topheadlines.presentation.theme.Primary100
import com.bruno.topheadlines.presentation.theme.Secondary100
import com.bruno.topheadlines.presentation.theme.Secondary200
import com.bruno.topheadlines.presentation.theme.TopHeadlinesTheme
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesAction.ArticleClickedAction
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesViewModel.ScreenEvent

@Composable
fun TopHeadlinesScreen(
    viewModel: TopHeadlinesViewModel = hiltViewModel(),
    onNext: (Article) -> Unit,
) {
    Screen(
        uiState = viewModel.uiState,
        onAction = viewModel::onAction
    )
    EventConsumer(onNext = onNext, viewModel = viewModel)
}

@Composable
private fun EventConsumer(
    onNext: (Article) -> Unit,
    viewModel: TopHeadlinesViewModel
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.eventFlow.collect {event ->
            when(event) {
                is ScreenEvent.NavigateTo -> onNext(event.article)
            }
        }
    }
}

@Composable
private fun Screen(
    uiState: TopHeadlinesUiState,
    onAction: (TopHeadlinesAction) -> Unit
) {
    TopHeadlinesTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Secondary100)
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = BuildConfig.SOURCE_NAME,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold
                    )
                },
                backgroundColor = Primary100
            )
            ScreenContent(uiState = uiState, onAction = onAction)
        }
    }
}

@Composable
private fun ScreenContent(
    uiState: TopHeadlinesUiState,
    onAction: (TopHeadlinesAction) -> Unit
) {
    val articles by uiState.articles.collectAsState()
    LazyColumn {
        articlesList(articles = articles, onAction = onAction)
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

private fun LazyListScope.articlesList(
    articles: List<Article>,
    onAction: (TopHeadlinesAction) -> Unit
) {
    items(articles) { article ->
        Card(
            elevation = 10.dp,
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(width = 1.dp, color = Secondary200),
            modifier = Modifier
                .padding(top = 10.dp, end = 10.dp, start = 10.dp)
                .clickable { onAction(ArticleClickedAction(article)) }
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(all = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(article.urlToImage)
                            .build()
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .size(110.dp)
                        .clip(shape = RoundedCornerShape(5.dp)),
                )
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = article.title,
                    style = MaterialTheme.typography.h6,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 4
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Preview
@Composable
fun Preview() {
    Screen(
        uiState = TopHeadlinesUiState(),
        onAction = {}
    )
}