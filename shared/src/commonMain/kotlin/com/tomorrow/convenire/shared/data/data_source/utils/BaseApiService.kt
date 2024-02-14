package com.tomorrow.convenire.shared.data.data_source.utils

import com.tomorrow.convenire.shared.domain.repositories.AuthenticationRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.http.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class BaseApiService(
    val clientProvider: () -> HttpClient,
) {
    suspend inline fun <reified Model> get(
        urlString: String
    ): Result<Model> = try {
        Result.success(clientProvider().get(urlString).body())
    } catch (e: Throwable) {
        Result.failure(e)
    }

    suspend inline fun <reified Model> get(
        urlString: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Result<Model> = try {
        Result.success(clientProvider().get(urlString) { block() }.body())
    } catch (e: Throwable) {
        Result.failure(e)
    }

    suspend inline fun <reified Model> post(
        urlString: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Result<Model> = try {
        Result.success(clientProvider().post {
            contentType(ContentType.Application.Json)
            url(urlString); block()
        }.body())
    } catch (e: Throwable) {
        Result.failure(e)
    }

    suspend inline fun <reified Model> put(
        urlString: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Result<Model> = try {
        Result.success(clientProvider().put { url(urlString); block() }.body())
    } catch (e: Throwable) {
        Result.failure(e)
    }

    suspend inline fun <reified Model> delete(
        urlString: String,
    ): Result<Model> = try {
        Result.success(clientProvider().delete(urlString).body())
    } catch (e: Throwable) {
        Result.failure(e)
    }
}