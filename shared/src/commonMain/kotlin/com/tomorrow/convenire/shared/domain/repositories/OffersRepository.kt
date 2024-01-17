package com.tomorrow.convenire.shared.domain.repositories

import com.tomorrow.convenire.shared.domain.model.Offer
import com.tomorrow.convenire.shared.domain.model.Spinner
import kotlinx.coroutines.flow.Flow

interface OffersRepository {
    fun getOffers(): Flow<List<Offer>>
    fun getSpinners(): Flow<List<Spinner>>
    fun getClaimedOffers(): Flow<List<Offer>>
}