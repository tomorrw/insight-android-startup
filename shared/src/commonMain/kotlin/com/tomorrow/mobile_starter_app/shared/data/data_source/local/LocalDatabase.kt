package com.tomorrow.mobile_starter_app.shared.data.data_source.local

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

interface LocalDatabase {
    //realm caches goes here
    suspend fun clearData()
}