package com.tomorrow.lda.shared.data.repository

import com.tomorrow.lda.shared.data.data_source.local.EncryptedStorage
import com.tomorrow.lda.shared.data.data_source.local.LocalDatabase
import com.tomorrow.lda.shared.data.data_source.mapper.*
import com.tomorrow.lda.shared.data.data_source.model.HomeDataDTO
import com.tomorrow.lda.shared.data.data_source.remote.ApiService
import com.tomorrow.lda.shared.data.data_source.utils.Loadable
import com.tomorrow.lda.shared.data.data_source.utils.NotLoaded
import com.tomorrow.lda.shared.domain.model.*
import com.tomorrow.lda.shared.domain.repositories.*
import com.tomorrow.lda.shared.domain.utils.PhoneNumber
import com.tomorrow.lda.shared.domain.utils.UUID
import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RepositoryImplementation : CompanyRepository, SpeakerRepository, PostRepository,
    SessionRepository, AppSettingsRepository, HomeRepository, AuthenticationRepository, OffersRepository,
    KoinComponent {

    private val apiService: ApiService by inject()
    private val localDatabase: LocalDatabase by inject()
    private val encryptedStorage: EncryptedStorage by inject()
    private val companyMapper = CompanyMapper()
    private val sessionMapper = SessionMapper()
    private val postMapper = PostMapper()
    private val speakerMapper = SpeakerDetailMapper()
    private val homeDataMapper = HomeDataMapper()
    private val userMapper = UserMapper()
    private val isAuthenticated = MutableStateFlow<Boolean?>(null)
    private val scope: CoroutineScope by inject()

    init {
        scope.launch {
            try {
                getLoggedInUser().collect()
            } catch (_: Exception) {
            }
        }
    }

    override fun getCompanyById(id: String): Flow<Company> = flow {
        var localDataAvailable = false

        localDatabase.getCompanies()?.find { it.id == id }?.let {
            emit(companyMapper.mapFromEntity(it)).also { localDataAvailable = true }
        }

        val lastUpdated = localDatabase.lastUpdatedCompanies()

        val thirtyMinutesAgo =
            Clock.System.now().minus(30, DateTimeUnit.MINUTE, TimeZone.currentSystemDefault())

        if (lastUpdated != null && lastUpdated > thirtyMinutesAgo && localDataAvailable) return@flow

        val companyDTO = apiService.getCompanies().getOrElse {
            if (it is ClientRequestException && it.response.status == HttpStatusCode.Unauthorized) logout()

            if (localDataAvailable) return@flow
            else throw it
        }.also { localDatabase.replaceCompanies(it) }.find { it.id == id }
            ?: throw Exception("company with id $id could not be not found")

        emit(companyMapper.mapFromEntity(companyDTO))
    }

    override fun getCompanies(): Flow<List<Company>> = flow {
        var localDataAvailable = false

        localDatabase.getCompanies()?.map { companyMapper.mapFromEntity(it) }
            ?.let { emit(it).also { localDataAvailable = true } }

        val lastUpdated = localDatabase.lastUpdatedCompanies()

        val thirtyMinutesAgo =
            Clock.System.now().minus(30, DateTimeUnit.MINUTE, TimeZone.currentSystemDefault())

        if (lastUpdated != null && lastUpdated > thirtyMinutesAgo && localDataAvailable) return@flow

        val companies = apiService.getCompanies().getOrElse {
            if (it is ClientRequestException && it.response.status == HttpStatusCode.Unauthorized) logout()

            if (localDataAvailable) return@flow
            else throw it
        }.also { localDatabase.replaceCompanies(it) }.map { companyMapper.mapFromEntity(it) }

        emit(companies)
    }

    override fun refreshCompanies(): Flow<List<Company>> = flow {
        val companies =
            apiService.getCompanies().getOrThrow().also { localDatabase.replaceCompanies(it) }
                .map { companyMapper.mapFromEntity(it) }

        emit(companies)
    }

    override fun getCategories(): Flow<List<Company.Category>> = getCompanies().map { companies ->
        companies.flatMap { it.categories }.distinct()
    }

    override fun refreshCategories(): Flow<List<Company.Category>> =
        refreshCompanies().map { companies -> companies.flatMap { it.categories }.distinct() }


    override fun getPostById(id: String): Flow<Post> = flow {
        localDatabase.getHomeResponse()?.posts?.find { it.id == id }?.let {
            emit(postMapper.mapFromEntity(it))
        }

        apiService.getPost(id).getOrElse {
            if (it is ClientRequestException && it.response.status == HttpStatusCode.Unauthorized) logout()
            throw it
        }.let { emit(postMapper.mapFromEntity(it)) }
    }

    override suspend fun hitPostUrl(url: String): Result<String> = apiService.hitPostUrl(url)

    override fun getSessionById(id: String): Flow<Session> = flow {
        var localDataAvailable = false

        localDatabase.getSessions()?.find { it.id == id }?.let {
            emit(sessionMapper.mapFromEntity(it)).also { localDataAvailable = true }
        }

        apiService.getSession(id).getOrElse {
            if (it is ClientRequestException && it.response.status == HttpStatusCode.Unauthorized) logout()
            if (localDataAvailable) return@flow
            else throw it
        }.also {
            localDatabase.replaceSession(it)
            emit(sessionMapper.mapFromEntity(it))
        }
    }

    @Throws(Throwable::class)
    override fun getSessions(): Flow<List<Session>> = flow {
        var localDataAvailable = false

        localDatabase.getSessions()?.map { sessionMapper.mapFromEntity(it) }
            ?.sortedBy { it.startTime }?.let { emit(it).also { localDataAvailable = true } }

        val lastUpdated = localDatabase.lastUpdatedSessions()

        val thirtyMinutesAgo =
            Clock.System.now().minus(30, DateTimeUnit.MINUTE, TimeZone.currentSystemDefault())

        if (lastUpdated != null && lastUpdated > thirtyMinutesAgo && localDataAvailable) return@flow

        val sessions = apiService.getSessions().getOrElse {
            if (it is ClientRequestException && it.response.status == HttpStatusCode.Unauthorized) logout()
            if (localDataAvailable) return@flow
            else throw it
        }.also { localDatabase.replaceSessions(it) }
            .map { sessionMapper.mapFromEntity(it) }
            .sortedBy { it.startTime }

        emit(sessions)
    }

    override fun getCachedSessionById(id: String): Session? = localDatabase.let {
        val allSession =
            (it.getSessions() ?: listOf()) + (it.getHomeResponse()?.upcomingSessions ?: listOf())
        sessionMapper.mapFromEntityIfNotNull(allSession.find { s -> s.id == id })
    }

    override fun refreshSessions(): Flow<List<Session>> = flow {
        val sessions =
            apiService.getSessions().getOrThrow().also { localDatabase.replaceSessions(it) }
                .map { sessionMapper.mapFromEntity(it) }.sortedBy { it.startTime }

        emit(sessions)
    }

    override fun isBookmarked(id: String): Boolean = localDatabase.isBookmarked(id)

    override fun isBookmarkedFlow(id: String): Flow<Boolean> = localDatabase.isBookmarkedFlow(id)

    override fun isBookmarkedFlow(listIds: List<String>): Flow<Map<String, Boolean>> =
        localDatabase.isBookmarkedFlow(listIds)

    override fun eventsBookmarks(): Map<String, Boolean> = localDatabase.eventsBookmarks()

    override suspend fun toggleShouldNotify(id: String) {
        localDatabase.toggleShouldNotify(id)
    }

    override fun getSpeakerById(id: String): Flow<SpeakerDetail> = flow {
        var localDataAvailable = false

        localDatabase.getSpeakers()?.find { it.id == id }?.let {
            emit(speakerMapper.mapFromEntity(it)).also { localDataAvailable = true }
        }

        val lastUpdated = localDatabase.lastUpdatedSpeakers()

        val thirtyMinutesAgo =
            Clock.System.now().minus(30, DateTimeUnit.MINUTE, TimeZone.currentSystemDefault())

        if (lastUpdated != null && lastUpdated > thirtyMinutesAgo && localDataAvailable) return@flow

        val speakerDto = apiService.getSpeakers().getOrElse {
            if (it is ClientRequestException && it.response.status == HttpStatusCode.Unauthorized) logout()
            if (localDataAvailable) return@flow
            else throw it
        }.also { localDatabase.replaceSpeakers(it) }.find { it.id == id }
            ?: throw Exception("speaker with id $id could not be not found")

        emit(speakerMapper.mapFromEntity(speakerDto))
    }


    override fun getSpeakers(): Flow<List<SpeakerDetail>> = flow {
        var localDataAvailable = false

        localDatabase.getSpeakers()
            ?.sortedBy { it.firstName }
            ?.map { speakerMapper.mapFromEntity(it) }
            ?.let { emit(it).also { localDataAvailable = true } }

        val lastUpdated = localDatabase.lastUpdatedSpeakers()

        val thirtyMinutesAgo =
            Clock.System.now().minus(30, DateTimeUnit.MINUTE, TimeZone.currentSystemDefault())

        if (lastUpdated != null && lastUpdated > thirtyMinutesAgo && localDataAvailable) return@flow

        val speakers = apiService.getSpeakers().getOrElse {
            if (it is ClientRequestException && it.response.status == HttpStatusCode.Unauthorized) logout()
            if (localDataAvailable) return@flow
            else throw it
        }
            .also { localDatabase.replaceSpeakers(it) }
            .sortedBy { it.firstName }
            .map { speakerMapper.mapFromEntity(it) }

        emit(speakers)
    }

    override fun refreshSpeakers(): Flow<List<SpeakerDetail>> = flow {
        val speakers = apiService.getSpeakers().getOrThrow()
            .also { localDatabase.replaceSpeakers(it) }
            .sortedBy { it.firstName }
            .map { speakerMapper.mapFromEntity(it) }

        emit(speakers)
    }

    override suspend fun getUpdateInfo(appPlatform: AppPlatform): Result<UpdateInfo> =
        apiService.getUpdate(AppPlatformMapper().mapToEntity(appPlatform))
            .mapCatching { UpdateInfoMapper().mapFromEntity(it) }

    override fun getHomeData(): Flow<HomeData> = flow {
        var localDataAvailable = false

        try {
            localDatabase.getHomeResponse()?.let {
                if (it.isEmpty()) return@let

                homeDataMapper.mapFromEntity(it)
                    .also { homeData -> emit(homeData); localDataAvailable = true }
            }
        } catch (e: Exception) {
            // if home data changed this will throw errors only the first time its called
            localDatabase.replaceHomeResponse(HomeDataDTO())
        }

        val lastUpdated = localDatabase.lastUpdatedHomeResponse()

        val fiveMinutesAgo =
            Clock.System.now().minus(10, DateTimeUnit.MINUTE, TimeZone.currentSystemDefault())

        if (lastUpdated != null && lastUpdated > fiveMinutesAgo && localDataAvailable) return@flow

        val homeData = apiService.getHome().getOrElse {
            if (it is ClientRequestException && it.response.status == HttpStatusCode.Unauthorized) logout()

            if (localDataAvailable) return@flow
            else throw it
        }.also { localDatabase.replaceHomeResponse(it) }

        emit(homeDataMapper.mapFromEntity(homeData))
    }

    override suspend fun askQuestion(eventId: String, question: String, isAnonymous: Boolean) =
        apiService.askQuestion(eventId, question, isAnonymous).isSuccess

    override fun refreshHomeData(): Flow<HomeData> = flow {
        val homeData =
            apiService.getHome().getOrThrow().also { localDatabase.replaceHomeResponse(it) }

        emit(homeDataMapper.mapFromEntity(homeData))
    }

    override suspend fun register(
        firstName: String, lastName: String, email: Email?, phoneNumber: PhoneNumber
    ): Result<UUID> = apiService.register(
        firstName, lastName, email?.value, phoneNumber.number ?: ""
    )

    override suspend fun verify(email: Email, otp: OTP): Result<User> =
        apiService.verifyOTP(email = email.value, phoneNumber = null, otp.toString()).map {
            setIsAuthenticated(true)
            encryptedStorage.bearerTokens = it.first
            encryptedStorage.user = it.second
            userMapper.mapFromEntity(it.second)
        }

    override suspend fun verify(phoneNumber: PhoneNumber, otp: OTP): Result<User> =
        apiService.verifyOTP(email = null, phoneNumber = phoneNumber.number ?: "", otp.toString())
            .map {
                setIsAuthenticated(true)
                encryptedStorage.bearerTokens = it.first
                encryptedStorage.user = it.second
                userMapper.mapFromEntity(it.second)
            }

    override suspend fun login(phoneNumber: PhoneNumber): Result<UUID> =
        apiService.login(phoneNumber = phoneNumber.number, email = null)

    override suspend fun login(email: Email): Result<UUID> =
        apiService.login(email = email.value, phoneNumber = null)

    override fun getLoggedInUser(): Flow<User> = flow {
        userMapper.mapFromEntityIfNotNull(encryptedStorage.user)?.let { emit(it) }

        apiService.getUser().getOrElse {
            if (it is ClientRequestException && it.response.status == HttpStatusCode.Unauthorized) {
                setIsAuthenticated(false)
                logout()
            }
            // offline mode
            else setIsAuthenticated(true)

            throw it
        }.let {
            setIsAuthenticated(true)
            encryptedStorage.user = it
            emit(userMapper.mapFromEntity(it))
        }
    }

    private fun setIsAuthenticated(value: Boolean) {
        if (value != isAuthenticated.value) isAuthenticated.value = value
    }

    override fun isAuthenticated(): StateFlow<Boolean?> = isAuthenticated

    override suspend fun logout() = apiService.logout().map { clearAllData() }.onFailure {
        if (it is ClientRequestException && it.response.status == HttpStatusCode.Unauthorized) clearAllData()
    }

    private suspend fun clearAllData() {
        encryptedStorage.bearerTokens = null
        encryptedStorage.user = null
        setIsAuthenticated(false)
        localDatabase.clearData()
    }

    override fun getOffers(): Flow<List<Offer>> = flow {

        val offers = apiService.getOffers().getOrElse {
            if (it is ClientRequestException && it.response.status == HttpStatusCode.Unauthorized) logout()
            throw it
        }.map { OfferMapper().mapFromEntity(it) }

        emit(offers)
    }

    override fun getSpinners(): Flow<List<Spinner>> = flow {
        val spinner = apiService.getSpinners().getOrElse {
            if (it is ClientRequestException && it.response.status == HttpStatusCode.Unauthorized) logout()
            throw it
        }.map { SpinnerMapper().mapFromEntity(it) }

        emit(spinner)
    }

    override fun getClaimedOffers(): Flow<List<Offer>> = flow {
        val offers = apiService.getClaimedOffers().getOrElse {
            if (it is ClientRequestException && it.response.status == HttpStatusCode.Unauthorized) logout()
            throw it
        }.map { OfferMapper().mapFromEntity(it) }

        emit(offers)
    }

}