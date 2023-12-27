package com.tomorrow.convenire.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.convenire.shared.domain.repositories.AuthenticationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class IsAuthenticatedUseCase : KoinComponent {
    private val repository: AuthenticationRepository by inject()

    @NativeCoroutines
    fun asFlow() = repository.isAuthenticated()

    fun isAuthenticated() = repository.isAuthenticated().value
}

