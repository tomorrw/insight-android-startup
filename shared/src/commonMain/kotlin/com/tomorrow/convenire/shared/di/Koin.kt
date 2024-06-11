package com.tomorrow.convenire.shared.di

import com.tomorrow.convenire.shared.data.data_source.local.LocalDatabase
import com.tomorrow.convenire.shared.data.data_source.local.LocalDatabaseImplementation
import com.tomorrow.convenire.shared.data.data_source.remote.ApiService
import com.tomorrow.convenire.shared.data.data_source.remote.ApiServiceImplementation
import com.tomorrow.convenire.shared.data.data_source.remote.WebSocketService
import com.tomorrow.convenire.shared.data.data_source.remote.WebSocketServiceImplementation
import com.tomorrow.convenire.shared.data.repository.RepositoryImplementation
import com.tomorrow.kmmProjectStartup.domain.model.MultipleFieldValidationError
import com.tomorrow.convenire.shared.domain.repositories.*
import com.tomorrow.kmmProjectStartup.utils.createHttpClient
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

    single<WebSocketService> {
        WebSocketServiceImplementation(
            {  get<BearerTokensContainer>().scope.get() },
            Constants.PRODUCTION_WEBSOCKET_BASE_URL,
            443,
            get()
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

class BearerTokensContainer(private val encryptedLocalDatabase: com.tomorrow.convenire.shared.data.data_source.local.EncryptedStorage) :
    KoinScopeComponent {
    override var scope: Scope = getOrCreateScope().value
        get() = getOrCreateScope().value
        private set

    val authTokens: BearerTokens?
        get() = encryptedLocalDatabase.bearerTokens
}