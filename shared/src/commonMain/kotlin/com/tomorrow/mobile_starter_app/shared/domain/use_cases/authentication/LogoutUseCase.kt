package com.tomorrow.mobile_starter_app.shared.domain.use_cases.authentication

import com.tomorrow.kmmProjectStartup.domain.model.ResultIOS
import com.tomorrow.kmmProjectStartup.domain.model.toResultIOS
import com.tomorrow.mobile_starter_app.shared.domain.repositories.AuthenticationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LogoutUseCase : KoinComponent {
    val repository: AuthenticationRepository by inject()

    suspend fun logout(): Result<Unit> = repository.logout()

    suspend fun logoutIOS(): ResultIOS<Unit, Throwable> = logout().toResultIOS()
}

