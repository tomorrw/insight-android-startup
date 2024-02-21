package com.tomorrow.convenire.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.convenire.shared.domain.model.HomeData
import com.tomorrow.convenire.shared.domain.repositories.HomeRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetHomeDataUseCase : KoinComponent {
    private val repository: HomeRepository by inject()

    @NativeCoroutines
    fun getHome(): Flow<HomeData> = repository.getHomeData()

    @NativeCoroutines
    fun refresh(): Flow<HomeData> = repository.refreshHomeData()
}