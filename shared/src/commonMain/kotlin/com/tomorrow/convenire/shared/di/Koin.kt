package com.tomorrow.convenire.shared.di

import com.tomorrow.convenire.shared.data.data_source.local.LocalDatabase
import com.tomorrow.convenire.shared.data.data_source.local.LocalDatabaseImplementation
import com.tomorrow.convenire.shared.data.data_source.remote.ApiService
import com.tomorrow.convenire.shared.data.data_source.remote.ApiServiceImplementation
import com.tomorrow.convenire.shared.data.data_source.remote.WebSocketService
import com.tomorrow.convenire.shared.data.data_source.remote.WebSocketServiceImplementation
import com.tomorrow.convenire.shared.data.repository.RepositoryImplementation
import com.tomorrow.convenire.shared.domain.model.MultipleFieldValidationError
import com.tomorrow.convenire.shared.domain.repositories.AppSettingsRepository
import com.tomorrow.convenire.shared.domain.repositories.AuthenticationRepository
import com.tomorrow.convenire.shared.domain.repositories.CompanyRepository
import com.tomorrow.convenire.shared.domain.repositories.HomeRepository
import com.tomorrow.convenire.shared.domain.repositories.LiveNotificationRepository
import com.tomorrow.convenire.shared.domain.repositories.OffersRepository
import com.tomorrow.convenire.shared.domain.repositories.PostRepository
import com.tomorrow.convenire.shared.domain.repositories.SessionRepository
import com.tomorrow.convenire.shared.domain.repositories.SpeakerRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.HttpRequest
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.getOrCreateScope
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.binds
import org.koin.dsl.module

fun initKoin(enableNetworkLogs: Boolean = false, appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(platformModule(), commonModule(enableNetworkLogs = enableNetworkLogs))
    }

// called by iOS
fun initKoin() = initKoin(enableNetworkLogs = false)

private fun commonModule(enableNetworkLogs: Boolean) = module {
    single { createJson() }

    single { CoroutineScope(Dispatchers.Default + SupervisorJob()) }

    single<com.tomorrow.convenire.shared.data.data_source.local.EncryptedStorage> {
        com.tomorrow.convenire.shared.data.data_source.local.EncryptedStorageImplementation(
            get(named(com.tomorrow.convenire.shared.data.data_source.local.EncryptedStorageImplementation.SETTING_NAME)),
            get()
        )
    }

    single { BearerTokensContainer(get()) }

    scope<BearerTokensContainer> {
        scoped {
            createHttpClient(
                // httpClientEngine is resolved from each platform module
                httpClientEngine = get(),
                json = get(),
                enableNetworkLogs = enableNetworkLogs,
                authToken = get<BearerTokensContainer>().authTokens
            )
        }
    }

    single<ApiService> {
        ApiServiceImplementation(
            { get<BearerTokensContainer>().scope.get() },
            Constants.PRODUCTION_API_BASE_URL
        )
    }

    single<WebSocketService> {
        WebSocketServiceImplementation(
            {  get<BearerTokensContainer>().scope.get() },
            Constants.PRODUCTION_WEBSOCKET_BASE_URL,
            443
        )
    }


    single<LocalDatabase> { LocalDatabaseImplementation(get()) }

    single { RepositoryImplementation() } binds arrayOf(
        PostRepository::class,
        SessionRepository::class,
        CompanyRepository::class,
        SpeakerRepository::class,
        AppSettingsRepository::class,
        AuthenticationRepository::class,
        UserRepository:: class,
        HomeRepository::class,
        OffersRepository::class,
        LiveNotificationRepository::class,
    )
}

fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    prettyPrint = true
}

fun createHttpClient(
    httpClientEngine: HttpClientEngine,
    json: Json,
    enableNetworkLogs: Boolean = false,
    authToken: BearerTokens? = null,
    expectSuccess: Boolean = true,
    responseValidator: suspend (Throwable, HttpRequest) -> Unit = responseValidator@{ exception, _ ->
        val clientException = exception as? ClientRequestException ?: return@responseValidator
        val response = clientException.response

        if (response.status == HttpStatusCode.UnprocessableEntity)
            throw clientException.response.body<GeneralValidationError>()
                .toMultipleFieldValidationError()

        if (response.status == HttpStatusCode.Forbidden)
            throw clientException.response.body<GeneralError>()

        return@responseValidator
    }
) = HttpClient(httpClientEngine) {
    defaultRequest { contentType(ContentType.Application.Json) }

    this.expectSuccess = expectSuccess

    HttpResponseValidator { handleResponseExceptionWithRequest(responseValidator) }

    install(ContentNegotiation) { json(json) }

    install(WebSockets) { contentConverter = KotlinxWebsocketSerializationConverter(json) }

    install(Auth) {
        bearer {
            authToken?.let {
                loadTokens {
                    BearerTokens(
                        accessToken = it.accessToken, refreshToken = it.refreshToken
                    )
                }
            }
        }
    }

    if (enableNetworkLogs) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
    }
}

@Serializable
data class GeneralValidationError(
    override val message: String, val errors: Map<String, List<String>>
) : Exception(message)

@Serializable
data class GeneralError(
    override val message: String
) : Exception(message)

fun GeneralValidationError.toMultipleFieldValidationError() = MultipleFieldValidationError(
    errors = this.errors, message = this.message
)

class BearerTokensContainer(private val encryptedLocalDatabase: com.tomorrow.convenire.shared.data.data_source.local.EncryptedStorage) :
    KoinScopeComponent {
    override var scope: Scope = getOrCreateScope().value
        get() = getOrCreateScope().value
        private set

    val authTokens: BearerTokens?
        get() = encryptedLocalDatabase.bearerTokens
}