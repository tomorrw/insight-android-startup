package com.tomorrow.convenire.shared.data.repository

import com.tomorrow.convenire.shared.data.data_source.local.EncryptedStorage
import com.tomorrow.convenire.shared.data.data_source.local.LocalDatabase
import com.tomorrow.convenire.shared.data.data_source.mapper.AppPlatformMapper
import com.tomorrow.convenire.shared.data.data_source.mapper.CompanyMapper
import com.tomorrow.convenire.shared.data.data_source.mapper.ConfigurationDataMapper
import com.tomorrow.convenire.shared.data.data_source.mapper.HomeDataMapper
import com.tomorrow.convenire.shared.data.data_source.mapper.NotificationMapper
import com.tomorrow.convenire.shared.data.data_source.mapper.OfferMapper
import com.tomorrow.convenire.shared.data.data_source.mapper.PostMapper
import com.tomorrow.convenire.shared.data.data_source.mapper.ProgressReportMapper
import com.tomorrow.convenire.shared.data.data_source.mapper.SessionMapper
import com.tomorrow.convenire.shared.data.data_source.mapper.SpeakerDetailMapper
import com.tomorrow.convenire.shared.data.data_source.mapper.SpinnerMapper
import com.tomorrow.convenire.shared.data.data_source.mapper.UpdateInfoMapper
import com.tomorrow.convenire.shared.data.data_source.mapper.UserMapper
import com.tomorrow.convenire.shared.data.data_source.remote.ApiService
import com.tomorrow.convenire.shared.data.data_source.remote.WebSocketService
import com.tomorrow.convenire.shared.data.repository.utils.getFromCacheAndRevalidate
import com.tomorrow.convenire.shared.domain.model.AppPlatform
import com.tomorrow.convenire.shared.domain.model.ColorTheme
import com.tomorrow.convenire.shared.domain.model.Company
import com.tomorrow.convenire.shared.domain.model.ConfigurationData
import com.tomorrow.convenire.shared.domain.model.Email
import com.tomorrow.convenire.shared.domain.model.HomeData
import com.tomorrow.convenire.shared.domain.model.Notification
import com.tomorrow.convenire.shared.domain.model.OTP
import com.tomorrow.convenire.shared.domain.model.Offer
import com.tomorrow.convenire.shared.domain.model.Post
import com.tomorrow.convenire.shared.domain.model.ProgressReport
import com.tomorrow.convenire.shared.domain.model.Session
import com.tomorrow.convenire.shared.domain.model.SpeakerDetail
import com.tomorrow.convenire.shared.domain.model.Spinner
import com.tomorrow.convenire.shared.domain.model.UpdateInfo
import com.tomorrow.convenire.shared.domain.model.User
import com.tomorrow.convenire.shared.domain.repositories.AppSettingsRepository
import com.tomorrow.convenire.shared.domain.repositories.AuthenticationRepository
import com.tomorrow.convenire.shared.domain.repositories.CompanyRepository
import com.tomorrow.convenire.shared.domain.repositories.HomeRepository
import com.tomorrow.convenire.shared.domain.repositories.LiveNotificationRepository
import com.tomorrow.convenire.shared.domain.repositories.OffersRepository
import com.tomorrow.convenire.shared.domain.repositories.PostRepository
import com.tomorrow.convenire.shared.domain.repositories.SessionRepository
import com.tomorrow.convenire.shared.domain.repositories.SpeakerRepository
import com.tomorrow.convenire.shared.domain.repositories.UserRepository
import com.tomorrow.convenire.shared.domain.utils.PhoneNumber
import com.tomorrow.convenire.shared.domain.utils.UUID
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration

class RepositoryImplementation : LiveNotificationRepository, CompanyRepository, SpeakerRepository,
    PostRepository,
    SessionRepository, AppSettingsRepository, HomeRepository, AuthenticationRepository,
    UserRepository,
    OffersRepository,
    KoinComponent {

    private val apiService: ApiService by inject()
    private val localDatabase: LocalDatabase by inject()
    private val encryptedStorage: EncryptedStorage by inject()
    private val companyMapper = CompanyMapper()
    private val sessionMapper = SessionMapper()
    private val postMapper = PostMapper()
    private val speakerMapper = SpeakerDetailMapper()
    private val reportMapper = ProgressReportMapper()
    private val homeDataMapper = HomeDataMapper()
    private val userMapper = UserMapper()
    private val notificationMapper = NotificationMapper()
    private val isAuthenticated = MutableStateFlow<Boolean?>(null)
    private val colorTheme = MutableStateFlow(encryptedStorage.colorTheme ?: ColorTheme.Auto)
    private val scope: CoroutineScope by inject()
    private val webSocketService: WebSocketService by inject()

    init {
        scope.launch {
            try {
                getLoggedInUser().collect()
            } catch (e: Exception) {
                println("$e")
            }
            apiService.addUnAuthenticatedInterceptor { internalLogout() }
        }
    }

    override fun getCompanyById(id: String): Flow<Company> = getCompanies().map { companies ->
        companies.find { it.id == id } ?: throw Exception("Company with id $id not found")
    }

    override fun getCompanies(): Flow<List<Company>> = getFromCacheAndRevalidate(
        getFromCache = { localDatabase.getCompanies() },
        getFromApi = { apiService.getCompanies() },
        setInCache = { localDatabase.replaceCompanies(it) },
        cacheAge = localDatabase.lastUpdatedCompanies(),
        revalidateIfOlderThan = Duration.parse("30m")
    ).map { companies -> companies.map { companyMapper.mapFromEntity(it) } }

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


    override fun getPostById(id: String): Flow<Post> = getFromCacheAndRevalidate(
        getFromCache = {
            localDatabase.getHomeResponse().getOrNull()?.posts?.find { it.id == id }
                ?.let { Result.success(it) }
                ?: Result.failure(Exception("Post with id $id not found"))
        },
        getFromApi = { apiService.getPost(id) },
        setInCache = { },
        cacheAge = localDatabase.lastUpdatedSessions(),
        revalidateIfOlderThan = Duration.parse("0m")
    ).map { post -> postMapper.mapFromEntity(post) }

    override suspend fun hitPostUrl(url: String): Result<String> = apiService.hitPostUrl(url)

    override fun getSessionById(id: String): Flow<Session> = getFromCacheAndRevalidate(
        getFromCache = {
            localDatabase.getSessions().getOrNull()?.find { it.id == id }
                ?.let { Result.success(it) }
                ?: Result.failure(Exception("Session with id $id not found"))
        },
        getFromApi = { apiService.getSession(id) },
        setInCache = { session -> localDatabase.replaceSession(session) },
        cacheAge = localDatabase.lastUpdatedSessions(),
        revalidateIfOlderThan = Duration.parse("0m")
    ).map { session -> sessionMapper.mapFromEntity(session) }

    @Throws(Throwable::class)
    override fun getSessions(): Flow<List<Session>> = getFromCacheAndRevalidate(
        getFromCache = { localDatabase.getSessions() },
        getFromApi = { apiService.getSessions() },
        setInCache = { sessions -> localDatabase.replaceSessions(sessions) },
        cacheAge = localDatabase.lastUpdatedSessions(),
        revalidateIfOlderThan = Duration.parse("30m")
    ).map { sessions -> sessions.map { sessionMapper.mapFromEntity(it) } }

    override fun getCachedSessionById(id: String): Session? = localDatabase.let {
        val allSession =
            (it.getSessions().getOrNull() ?: listOf()) + (it.getHomeResponse()
                .getOrNull()?.upcomingSessions
                ?: listOf())
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

    override fun getSpeakerById(id: String): Flow<SpeakerDetail> = getSpeakers()
        .map { speakers ->
            speakers.find { it.id == id } ?: throw Exception("Speaker with id $id not found")
        }


    override fun getSpeakers(): Flow<List<SpeakerDetail>> = getFromCacheAndRevalidate(
        getFromCache = { localDatabase.getSpeakers() },
        getFromApi = { apiService.getSpeakers() },
        setInCache = { speakers -> localDatabase.replaceSpeakers(speakers) },
        cacheAge = localDatabase.lastUpdatedSpeakers(),
        revalidateIfOlderThan = Duration.parse("30m")
    ).map { speakers -> speakers.map { speakerMapper.mapFromEntity(it) } }


    override fun refreshSpeakers(): Flow<List<SpeakerDetail>> = flow {
        val speakers = apiService.getSpeakers().getOrThrow()
            .also { localDatabase.replaceSpeakers(it) }
            .sortedBy { it.firstName }
            .map { speakerMapper.mapFromEntity(it) }

        emit(speakers)
    }

    override fun getProgressReport(): Flow<ProgressReport> = getFromCacheAndRevalidate(
        getFromCache = { localDatabase.getProgressReport() },
        getFromApi = { apiService.getProgressReport() },
        setInCache = { speakers -> localDatabase.replaceProgressReport(speakers) },
        cacheAge = localDatabase.lastUpdatedProgressReport(),
        revalidateIfOlderThan = Duration.parse("0m")
    ).map { reportMapper.mapFromEntity(it) }

    override fun refreshProgressReport(): Flow<ProgressReport> = flow {
        val report = apiService.getProgressReport().getOrThrow()
            .also { localDatabase.replaceProgressReport(it) }
            .let { reportMapper.mapFromEntity(it) }

        emit(report)
    }


    override suspend fun getUpdateInfo(appPlatform: AppPlatform): Result<UpdateInfo> =
        apiService.getUpdate(AppPlatformMapper().mapToEntity(appPlatform))
            .mapCatching { UpdateInfoMapper().mapFromEntity(it) }

    override fun getHomeData(): Flow<HomeData> = getFromCacheAndRevalidate(
        getFromCache = { localDatabase.getHomeResponse() },
        getFromApi = { apiService.getHome() },
        setInCache = { homeData -> localDatabase.replaceHomeResponse(homeData) },
        cacheAge = localDatabase.lastUpdatedHomeResponse(),
        revalidateIfOlderThan = Duration.parse("30m")
    ).map { homeData -> homeDataMapper.mapFromEntity(homeData) }

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
        userMapper.mapFromEntityIfNotNull(encryptedStorage.user)?.let {
            emit(it)
        }
        apiService.getUser().getOrElse {
            if (it is ClientRequestException && it.response.status == HttpStatusCode.Unauthorized) {
                setIsAuthenticated(false)
                internalLogout()
            }
            // offline mode
            else setIsAuthenticated(true)

            throw it
        }.let {
            setIsAuthenticated(true)
            encryptedStorage.user = it
            emit(userMapper.mapFromEntity(it))
            runBlocking { encryptedStorage.fcmToken?.let { fcm -> apiService.saveFCMToken(fcm) } }
        }

    }

    private fun setIsAuthenticated(value: Boolean) {
        if (value != isAuthenticated.value) isAuthenticated.value = value
    }

    override fun isAuthenticated(): StateFlow<Boolean?> = isAuthenticated

    private suspend fun internalLogout() = apiService.logout().map { clearAllData() }.onFailure {
        if (it is ClientRequestException && it.response.status == HttpStatusCode.Unauthorized) clearAllData()
    }

    override suspend fun logout(): Result<Unit> {
        encryptedStorage.fcmToken?.let { apiService.deleteFCMToken(it) }
        return internalLogout()
    }

    override fun getConfiguration(): Flow<ConfigurationData> = flow {
        //TODO add it to caches when business logic is ready
        apiService.getConfig().getOrThrow().let {
            emit(ConfigurationDataMapper().mapFromEntity(it))
        }
    }

    override fun getColorTheme(): StateFlow<ColorTheme> = colorTheme

    override fun setColorTheme(colorTheme: ColorTheme): Result<String> {
        encryptedStorage.colorTheme = colorTheme
        this.colorTheme.value = encryptedStorage.colorTheme ?: ColorTheme.Auto
        return Result.success("Successfully changed")
    }

    override fun saveFCMToken(fcmToken: String?): Result<String> {
        encryptedStorage.fcmToken = fcmToken
        return Result.success("Successfully saved")
    }

    private suspend fun clearAllData() {
        encryptedStorage.bearerTokens = null
        encryptedStorage.user = null
        encryptedStorage.colorTheme = null
        setIsAuthenticated(false)
        localDatabase.clearData()
    }

    override fun getOffers(): Flow<List<Offer>> = flow {
        val offers = apiService.getOffers().getOrThrow().map { OfferMapper().mapFromEntity(it) }
        emit(offers)
    }

    override fun getSpinners(): Flow<List<Spinner>> = flow {
        val spinner =
            apiService.getSpinners().getOrThrow().map { SpinnerMapper().mapFromEntity(it) }
        emit(spinner)
    }

    override fun getClaimedOffers(): Flow<List<Offer>> = flow {
        val offers =
            apiService.getClaimedOffers().getOrThrow().map { OfferMapper().mapFromEntity(it) }
        emit(offers)
    }

    override suspend fun startReceivingMessages(id: String, setMessage: (Result<Notification>) -> Unit) {
        return webSocketService.startListeningToQr(
            id,
            notificationMapper.mapToEntity(
                setMessage
            )
        )
    }

    override suspend fun stopReceivingMessages() =
        webSocketService.stopListeningToQr()
}