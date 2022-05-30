package com.bruno.topheadlines.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow

private const val FIRST_INDEX = 0

@Composable
fun rememberEndReachedState(onEndReached: () -> Unit): LazyListState {
    val scrollState = rememberLazyListState()
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = scrollState.layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf true

            lastVisibleItem.index == scrollState.layoutInfo.totalItemsCount.dec() && lastVisibleItem.index != FIRST_INDEX
        }
    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }.collect {
                if (it) onEndReached()
            }
    }
    return scrollState
}