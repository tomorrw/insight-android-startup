package com.tomorrow.lda.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.lda.shared.domain.model.User
import com.tomorrow.lda.shared.domain.repositories.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetUserUseCase : KoinComponent {
    val repository: AuthenticationRepository by inject()

    @NativeCoroutines
    @Throws(Exception::class)
    fun getUser(): Flow<User> = repository.getLoggedInUser()
}