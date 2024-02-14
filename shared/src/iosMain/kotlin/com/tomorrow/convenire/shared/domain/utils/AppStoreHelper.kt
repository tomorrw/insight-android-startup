package com.tomorrow.convenire.shared.domain.utils

import com.tomorrow.convenire.shared.di.createHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppStoreHelper : KoinComponent {
    private val engine: HttpClientEngine by inject()
    private val json: Json by inject()
    private val httpClient: HttpClient = createHttpClient(
        httpClientEngine = engine,
        json = json,
        enableNetworkLogs = false,
    )

    class AppInfo(
        val name: String,
        val updateUrl: String,
    )

    @Serializable
    private class AppleResponse(val results: List<AppstoreInfoDTO>) {
        @Serializable
        class AppstoreInfoDTO(
            val trackName: String,
            val trackViewUrl: String,
        )
    }

    suspend fun getAppNameAndUpdateUrl(bundleId: String): AppInfo? = try {
        httpClient.get("https://itunes.apple.com/lookup?bundleId=$bundleId").body<ByteArray>()
            .let { json.decodeFromString<AppleResponse>(it.decodeToString()) }
            .results.firstOrNull()?.let { AppInfo(name = it.trackName, updateUrl = it.trackViewUrl) }
    } catch (e: Throwable) {
        null
    }
}