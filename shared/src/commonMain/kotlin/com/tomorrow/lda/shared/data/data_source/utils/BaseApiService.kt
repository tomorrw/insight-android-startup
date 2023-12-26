package com.tomorrow.lda.shared.data.data_source.utils

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

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
        block: HttpRequestBuilder.() -> Unit = {}
    ): Result<Model> = try {
        Result.success(clientProvider().get { block() }.body())
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