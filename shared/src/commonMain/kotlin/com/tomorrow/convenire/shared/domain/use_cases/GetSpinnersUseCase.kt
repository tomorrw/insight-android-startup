package com.tomorrow.convenire.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.convenire.shared.domain.model.Spinner
import com.tomorrow.convenire.shared.domain.repositories.OffersRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetSpinnersUseCase : KoinComponent {
    private val repository: OffersRepository by inject()
    @NativeCoroutines
    fun getSpinners(): Flow<List<Spinner>> = repository.getSpinners()

    @NativeCoroutines
    fun refresh(): Flow<List<Spinner>> = getSpinners()
}