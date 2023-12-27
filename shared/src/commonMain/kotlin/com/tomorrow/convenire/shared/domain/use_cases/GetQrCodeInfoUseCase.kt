package com.tomorrow.convenire.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.convenire.shared.domain.model.QrCodeData
import com.tomorrow.convenire.shared.domain.model.User
import com.tomorrow.convenire.shared.domain.repositories.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetQrCodeInfoUseCase: KoinComponent {
    val repository: AuthenticationRepository by inject()

    @NativeCoroutines
    @Throws(Exception::class)
    fun getQrInfo(): Flow<QrCodeData> = repository.getQr()

}