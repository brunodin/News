package com.bruno.topheadlines.presentation.ui.topheadlines

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.bruno.topheadlines.R
import com.bruno.topheadlines.domain.model.Article
import com.bruno.topheadlines.presentation.main.NewsViewModel
import com.bruno.topheadlines.presentation.theme.Dimension
import com.bruno.topheadlines.presentation.theme.TopHeadlinesTheme
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesAction.ArticleClickedAction
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesAction.EndReachedAction
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesAction.RetryButtonAction
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesUiState.ScreenState
import com.bruno.topheadlines.presentation.ui.topheadlines.TopHeadlinesViewModel.ScreenEvent
import com.bruno.topheadlines.util.rememberEndReachedState

@Composable
fun TopHeadlinesScreen(
    viewModel: TopHeadlinesViewModel = hiltViewModel(),
    sharedViewModel: NewsViewModel,
) {
    LaunchedEffect(key1 = Unit) { viewModel.setup() }
    Screen(
        uiState = viewModel.uiState,
        onAction = viewModel::onAction
    )
    EventConsumer(viewModel = viewModel, sharedViewModel = sharedViewModel)
}

@Composable
private fun EventConsumer(
    viewModel: TopHeadlinesViewModel,
    sharedViewModel: NewsViewModel
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is ScreenEvent.NavigateTo -> sharedViewModel.navigate(event.navigation)
                is ScreenEvent.SaveArticle -> sharedViewModel.article = event.article
            }
        }
    }
}

@Composable
private fun Screen(
    uiState: TopHeadlinesUiState,
    onAction: (TopHeadlinesAction) -> Unit
) {
    val screenState = uiState.screenState.collectAsState().value
    TopHeadlinesTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
        ) {
            ToolBar()
            when (screenState) {
                is ScreenState.Failure ->  ScreenError(
                    onRetryClicked = { onAction(RetryButtonAction(screenState.retryAction)) }
                )
                ScreenState.Loading -> ScreenLoading()
                ScreenState.ShowScreen -> ScreenContent(uiState = uiState, onAction = onAction)
            }
        }
    }
}

@Composable
private fun ScreenContent(
    uiState: TopHeadlinesUiState,
    onAction: (TopHeadlinesAction) -> Unit
) {
    val articles by uiState.articles.collectAsState()
    val isLoading by uiState.isLoading.collectAsState()
    if (articles.isNotEmpty()) {
        LazyColumn(
            state = rememberEndReachedState(onEndReached = { onAction(EndReachedAction) })
        ) {
            articlesList(articles = articles, onAction = onAction)
            if (isLoading) bottomLoading()
        }
    } else {
        EmptyListWarning()
    }
}

@Composable
private fun ScreenLoading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgress()
        Spacer(modifier = Modifier.height(height = Dimension.SizeSM))
        Text(
            text = stringResource(id = R.string.loading),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ScreenError(onRetryClicked: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = stringResource(id = R.string.error),
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(height = Dimension.SizeMD))
        Button(
            onClick = onRetryClicked,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondaryVariant)
        ) {
            Text(
                text = stringResource(id = R.string.retry),
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
private fun ToolBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold
            )
        },
        backgroundColor = MaterialTheme.colors.primary
    )
}

@Composable
private fun EmptyListWarning() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_sad_face),
            contentDescription = null,
            modifier = Modifier.size(size = Dimension.SizeLG)
        )
        Spacer(modifier = Modifier.height(height = Dimension.SizeMD))
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.empty_list),
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CircularProgress() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = Dimension.SizeSM),
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

private fun LazyListScope.bottomLoading() {
    item {
        CircularProgress()
    }
}

private fun LazyListScope.articlesList(
    articles: List<Article>,
    onAction: (TopHeadlinesAction) -> Unit
) {
    items(articles) { article ->
        Card(
            elevation = Dimension.SizeSM,
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(
                width = Dimension.SizeXSM,
                color = MaterialTheme.colors.secondaryVariant
            ),
            modifier = Modifier
                .padding(top = Dimension.SizeSM, end = Dimension.SizeSM, start = Dimension.SizeSM)
                .clickable { onAction(ArticleClickedAction(article)) }
        ) {
            Spacer(modifier = Modifier.height(height = Dimension.SizeMD))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = Dimension.SizeXLG)
                    .padding(all = Dimension.SizeSM),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(data = article.urlToImage)
                            .build()
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .size(110.dp)
                        .clip(shape = MaterialTheme.shapes.medium),
                )
                Text(
                    modifier = Modifier.padding(start = Dimension.SizeSM),
                    text = article.title,
                    style = MaterialTheme.typography.h6,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 4
                )
            }
            Spacer(modifier = Modifier.height(height = Dimension.SizeMD))
        }
    }
    item {
        Spacer(modifier = Modifier.height(height = Dimension.SizeSM))
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