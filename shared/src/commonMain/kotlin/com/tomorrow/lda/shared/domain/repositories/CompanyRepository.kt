package com.tomorrow.lda.shared.domain.repositories

import com.tomorrow.lda.shared.domain.model.Company
import kotlinx.coroutines.flow.Flow

interface CompanyRepository {
    fun getCompanyById(id: String): Flow<Company>

    fun getCompanies(): Flow<List<Company>>

    fun refreshCompanies(): Flow<List<Company>>

    fun getCategories(): Flow<List<Company.Category>>

    fun refreshCategories(): Flow<List<Company.Category>>
}