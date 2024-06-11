package com.tomorrow.mobile_starter_app.shared.domain.use_cases

import com.tomorrow.kmmProjectStartup.domain.model.ResultIOS
import com.tomorrow.kmmProjectStartup.domain.model.toResultIOS
import com.tomorrow.mobile_starter_app.shared.domain.repositories.AuthenticationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SaveFCMToken : KoinComponent {
    val repository: AuthenticationRepository by inject()

    fun saveFCMToken(fcmToken: String?): Result<String> = repository.saveFCMToken(fcmToken)
    fun saveFCMTokenIOS(fcmToken: String?): ResultIOS<String, Throwable> = repository.saveFCMToken(fcmToken).toResultIOS()
}