package com.tomorrow.mobile_starter_app.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.mobile_starter_app.shared.domain.model.Offer
import com.tomorrow.mobile_starter_app.shared.domain.repositories.OffersRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetOffersUseCase: KoinComponent {
    private val repository: OffersRepository by inject()
    @NativeCoroutines
    fun getOffers(): Flow<List<Offer>> = repository.getOffers()

    @NativeCoroutines
    fun refresh(): Flow<List<Offer>> = getOffers()

    @NativeCoroutines
    fun getClaimedOffers(): Flow<List<Offer>> = repository.getClaimedOffers()

    @NativeCoroutines
    fun refreshClaimedOffers(): Flow<List<Offer>> = getClaimedOffers()
}