package com.github.xe11.rvrside.utils

import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

suspend inline fun <V> runSuspendCatching(block: () -> V): Result<V> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        coroutineContext.ensureActive()
        Result.failure(e)
    }
}
