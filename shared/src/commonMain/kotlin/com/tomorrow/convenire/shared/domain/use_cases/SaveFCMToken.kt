package com.tomorrow.convenire.shared.domain.use_cases

import com.tomorrow.convenire.shared.domain.model.ResultIOS
import com.tomorrow.convenire.shared.domain.model.toResultIOS
import com.tomorrow.convenire.shared.domain.repositories.AuthenticationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SaveFCMToken : KoinComponent {
    val repository: AuthenticationRepository by inject()
    fun saveFCMToken(fcmToken: String?): ResultIOS<String, Throwable> =
        repository.saveFCMToken(fcmToken).toResultIOS()

    fun saveFCMTokenIOS(fcmToken: String?): ResultIOS<String, Throwable> =
        repository.saveFCMToken(fcmToken).toResultIOS()
}