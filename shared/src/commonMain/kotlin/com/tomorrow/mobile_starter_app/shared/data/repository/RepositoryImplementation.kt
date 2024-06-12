package com.tomorrow.mobile_starter_app.shared.data.repository

import com.tomorrow.mobile_starter_app.shared.data.data_source.local.LocalDatabase
import com.tomorrow.mobile_starter_app.shared.data.data_source.mapper.AppPlatformMapper
import com.tomorrow.mobile_starter_app.shared.data.data_source.mapper.UpdateInfoMapper
import com.tomorrow.mobile_starter_app.shared.data.data_source.mapper.UserMapper
import com.tomorrow.mobile_starter_app.shared.data.data_source.remote.ApiService
import com.tomorrow.mobile_starter_app.shared.domain.model.*
import com.tomorrow.mobile_starter_app.shared.domain.repositories.*
import com.tomorrow.kmmProjectStartup.domain.model.AppPlatform
import com.tomorrow.kmmProjectStartup.domain.model.Email
import com.tomorrow.kmmProjectStartup.domain.model.OTP
import com.tomorrow.kmmProjectStartup.domain.model.UpdateInfo
import com.tomorrow.kmmProjectStartup.domain.utils.PhoneNumber
import com.tomorrow.kmmProjectStartup.domain.utils.UUID
import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RepositoryImplementation :
    AppSettingsRepository, AuthenticationRepository,
    KoinComponent {

    private val apiService: ApiService by inject()
    private val localDatabase: LocalDatabase by inject()
    private val encryptedStorage: com.tomorrow.mobile_starter_app.shared.data.data_source.local.EncryptedStorage by inject()
    private val userMapper = UserMapper()
    private val isAuthenticated = MutableStateFlow<Boolean?>(null)
    private val colorTheme = MutableStateFlow(encryptedStorage.colorTheme ?: ColorTheme.Auto)
    private val scope: CoroutineScope by inject()

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

    override suspend fun getUpdateInfo(appPlatform: AppPlatform): Result<UpdateInfo> =
        apiService.getUpdate(AppPlatformMapper().mapToEntity(appPlatform))
            .mapCatching { UpdateInfoMapper().mapFromEntity(it) }


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

    private suspend fun internalLogout() = Result.success(Unit).map { clearAllData() }.onFailure {
        if (it is ClientRequestException && it.response.status == HttpStatusCode.Unauthorized) clearAllData()
    }

    override suspend fun logout(): Result<Unit> {
        encryptedStorage.fcmToken?.let { apiService.deleteFCMToken(it) }
        return internalLogout()
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
}