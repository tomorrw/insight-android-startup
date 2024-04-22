package com.tomorrow.convenire.shared.di

import com.tomorrow.convenire.shared.data.data_source.local.LocalDatabase
import com.tomorrow.convenire.shared.data.data_source.local.LocalDatabaseImplementation
import com.tomorrow.convenire.shared.data.data_source.remote.ApiService
import com.tomorrow.convenire.shared.data.data_source.remote.ApiServiceImplementation
import com.tomorrow.convenire.shared.data.repository.RepositoryImplementation
import com.tomorrow.convenire.shared.domain.model.MultipleFieldValidationError
import com.tomorrow.convenire.shared.domain.repositories.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
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