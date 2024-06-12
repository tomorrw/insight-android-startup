package com.tomorrow.mobile_starter_app.shared.domain.use_cases.authentication

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.mobile_starter_app.shared.domain.model.User
import com.tomorrow.mobile_starter_app.shared.domain.repositories.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetUserUseCase : KoinComponent {
    val repository: AuthenticationRepository by inject()

    @NativeCoroutines
    @Throws(Exception::class)
    fun getUser(): Flow<User> = repository.getLoggedInUser()
}