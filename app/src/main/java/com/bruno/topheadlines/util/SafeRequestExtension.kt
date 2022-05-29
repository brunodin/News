package com.bruno.topheadlines.util

import com.bruno.topheadlines.domain.model.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

suspend fun <T> safeRequest(
    dispatcher: CoroutineDispatcher,
    block: suspend CoroutineScope.() -> (T)
) = withContext(dispatcher) {
    try {
        Result.Success(block())
    } catch (e: Exception) {
        Result.Failure(e)
    }
}