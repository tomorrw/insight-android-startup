package com.tomorrow.convenire.shared.domain.repositories

import com.tomorrow.convenire.shared.domain.model.Email
import com.tomorrow.convenire.shared.domain.model.OTP
import com.tomorrow.convenire.shared.domain.model.ConfigurationData
import com.tomorrow.convenire.shared.domain.model.User
import com.tomorrow.convenire.shared.domain.utils.PhoneNumber
import com.tomorrow.convenire.shared.domain.utils.UUID
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
    fun saveFCMToken(fcmToken: String?): Result<String>
}