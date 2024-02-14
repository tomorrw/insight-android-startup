package com.tomorrow.convenire.shared.data.repository.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlin.time.Duration

fun <Data> getFromCacheAndRevalidate(
    getFromCache: suspend () -> Result<Data>,
    getFromApi: suspend () -> Result<Data>,
    setInCache: suspend (Data) -> Unit,
    cacheAge: Instant?,
    revalidateIfOlderThan: Duration,
): Flow<Data> {
    return flow {
        var localDataFound = false

        getFromCache().getOrNull()?.let {
            emit(it)
            localDataFound = true
        }

        val minutesAgo =
            Clock.System.now()
                .minus(
                    revalidateIfOlderThan.inWholeMinutes,
                    DateTimeUnit.MINUTE,
                    TimeZone.currentSystemDefault()
                )

        if (cacheAge != null && cacheAge < minutesAgo && localDataFound) return@flow

        getFromApi().getOrThrow()?.let {
            emit(it)
            setInCache(it)
        }
    }
}