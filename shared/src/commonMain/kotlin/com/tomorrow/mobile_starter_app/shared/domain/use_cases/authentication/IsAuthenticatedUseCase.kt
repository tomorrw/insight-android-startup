package com.tomorrow.mobile_starter_app.shared.domain.use_cases.authentication

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.mobile_starter_app.shared.domain.repositories.AuthenticationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class IsAuthenticatedUseCase : KoinComponent {
    private val repository: AuthenticationRepository by inject()

    @NativeCoroutines
    fun asFlow() = repository.isAuthenticated()

    fun isAuthenticated() = repository.isAuthenticated().value
}

