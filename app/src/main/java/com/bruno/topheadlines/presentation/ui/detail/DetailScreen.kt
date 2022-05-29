package com.bruno.topheadlines.presentation.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.bruno.topheadlines.R
import com.bruno.topheadlines.presentation.main.NewsViewModel
import com.bruno.topheadlines.presentation.main.NewsActivity
import com.bruno.topheadlines.presentation.theme.Primary100
import com.bruno.topheadlines.presentation.theme.Secondary100
import com.bruno.topheadlines.presentation.theme.Secondary200
import com.bruno.topheadlines.presentation.theme.TopHeadlinesTheme
import com.bruno.topheadlines.presentation.ui.detail.DetailAction.BackButtonAction
import com.bruno.topheadlines.presentation.ui.detail.DetailViewModel.*
import com.bruno.topheadlines.util.formatToDayMonthYear
import com.bruno.topheadlines.util.formatToHour

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    sharedViewModel: NewsViewModel
) {
    val activity = LocalContext.current as NewsActivity
    LaunchedEffect(key1 = Unit) { viewModel.setup(sharedViewModel.article) }
    Screen(uiState = viewModel.uiState, onAction = viewModel::onAction)
    EventConsumer(activity = activity, viewModel = viewModel)
}

@Composable
private fun EventConsumer(
    activity: NewsActivity,
    viewModel: DetailViewModel
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                ScreenEvent.GoBack -> activity.onBackPressed()
            }
        }
    }
}

@Composable
private fun Screen(
    uiState: DetailUiState,
    onAction: (DetailAction) -> Unit
) {
    val article by uiState.article.collectAsState()
    TopHeadlinesTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Secondary100)
        ) {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { onAction(BackButtonAction) }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(color = Color.White)
                        )
                    }
                },
                backgroundColor = Primary100
            )
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(space = 10.dp)
            ) {
                Text(
                    text = article?.title.orEmpty(),
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.authors, article?.author.orEmpty()),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.End
                )
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(article?.urlToImage.orEmpty())
                            .build()
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxSize()
                        .clip(shape = RoundedCornerShape(5.dp)),
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(
                        id = R.string.published,
                        article?.publishedAt?.formatToHour().orEmpty(),
                        article?.publishedAt?.formatToDayMonthYear().orEmpty()
                    ),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.End
                )
                Text(
                    text = article?.description.orEmpty(),
                    style = MaterialTheme.typography.h5,
                )
                Divider(
                    thickness = 1.dp,
                    color = Secondary200
                )
                Text(
                    text = article?.content.orEmpty(),
                    style = MaterialTheme.typography.h5,
                )
            }
        }
    }
}