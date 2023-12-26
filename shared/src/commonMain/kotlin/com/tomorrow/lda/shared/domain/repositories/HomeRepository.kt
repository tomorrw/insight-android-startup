package com.tomorrow.lda.shared.domain.repositories

import com.tomorrow.lda.shared.domain.model.HomeData
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHomeData(): Flow<HomeData>

    fun refreshHomeData(): Flow<HomeData>
}