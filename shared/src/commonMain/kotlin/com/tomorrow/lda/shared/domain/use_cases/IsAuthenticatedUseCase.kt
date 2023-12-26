package com.tomorrow.lda.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.lda.shared.domain.repositories.AuthenticationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class IsAuthenticatedUseCase : KoinComponent {
    private val repository: AuthenticationRepository by inject()

    @NativeCoroutines
    fun asFlow() = repository.isAuthenticated()

    fun isAuthenticated() = repository.isAuthenticated().value
}

