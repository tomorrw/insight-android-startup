package com.tomorrow.mobile_starter_app.shared.domain.repositories

import com.tomorrow.mobile_starter_app.shared.domain.model.HomeData
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHomeData(): Flow<HomeData>

    fun refreshHomeData(): Flow<HomeData>
}