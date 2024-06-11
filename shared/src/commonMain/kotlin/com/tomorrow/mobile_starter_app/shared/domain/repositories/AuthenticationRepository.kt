package com.tomorrow.mobile_starter_app.shared.domain.repositories

import com.tomorrow.mobile_starter_app.shared.domain.model.ColorTheme
import com.tomorrow.kmmProjectStartup.domain.model.Email
import com.tomorrow.kmmProjectStartup.domain.model.OTP
import com.tomorrow.mobile_starter_app.shared.domain.model.ConfigurationData
import com.tomorrow.mobile_starter_app.shared.domain.model.User
import com.tomorrow.kmmProjectStartup.domain.utils.PhoneNumber
import com.tomorrow.kmmProjectStartup.domain.utils.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthenticationRepository {
    suspend fun register(firstName: String, lastName: String, email: Email?, phoneNumber: PhoneNumber): Result<UUID>
    suspend fun verify(phoneNumber: PhoneNumber, otp: OTP): Result<User>
    suspend fun verify(email: Email, otp: OTP): Result<User>
    suspend fun login(phoneNumber: PhoneNumber): Result<UUID>
    suspend fun login(email: Email): Result<UUID>
    fun getLoggedInUser(): Flow<User>
    fun isAuthenticated(): StateFlow<Boolean?>
    suspend fun logout(): Result<Unit>
    fun getConfiguration(): Flow<ConfigurationData>
    fun getColorTheme(): StateFlow<ColorTheme>
    fun setColorTheme(colorTheme: ColorTheme): Result<String>
    fun saveFCMToken(fcmToken: String?): Result<String>
}