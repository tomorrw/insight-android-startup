package com.tomorrow.lda.shared.domain.repositories

import com.tomorrow.lda.shared.domain.model.Offer
import com.tomorrow.lda.shared.domain.model.Spinner
import kotlinx.coroutines.flow.Flow

interface OffersRepository {
    fun getOffers(): Flow<List<Offer>>
    fun getSpinners(): Flow<List<Spinner>>
    fun getClaimedOffers(): Flow<List<Offer>>
}