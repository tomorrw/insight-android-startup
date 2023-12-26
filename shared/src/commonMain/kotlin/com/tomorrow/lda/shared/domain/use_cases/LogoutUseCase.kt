package com.tomorrow.lda.shared.domain.use_cases

import com.tomorrow.lda.shared.domain.model.ResultIOS
import com.tomorrow.lda.shared.domain.model.toResultIOS
import com.tomorrow.lda.shared.domain.repositories.AuthenticationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LogoutUseCase : KoinComponent {
    val repository: AuthenticationRepository by inject()

    suspend fun logout(): Result<Unit> = repository.logout()

    suspend fun logoutIOS(): ResultIOS<Unit, Throwable> = logout().toResultIOS()
}

