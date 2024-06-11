package com.tomorrow.mobile_starter_app.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.mobile_starter_app.shared.domain.model.ConfigurationData
import com.tomorrow.mobile_starter_app.shared.domain.repositories.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetConfigurationUseCase: KoinComponent {
    val repository: AuthenticationRepository by inject()

    @NativeCoroutines
    @Throws(Exception::class)
    fun getTicketInfo(): Flow<ConfigurationData> = repository.getConfiguration()

}