package com.tomorrow.convenire.shared.data.data_source.mapper

import com.tomorrow.convenire.shared.data.data_source.model.AppPlatformDTO
import com.tomorrow.kmmProjectStartup.data.utils.EntityMapper
import com.tomorrow.kmmProjectStartup.domain.model.AppPlatform

class AppPlatformMapper : EntityMapper<AppPlatform, AppPlatformDTO> {
    override fun mapFromEntity(entity: AppPlatformDTO) = when (entity) {
        AppPlatformDTO.Android -> AppPlatform.Android
        AppPlatformDTO.IOS -> AppPlatform.IOS
    }

    override fun mapToEntity(domainModel: AppPlatform) = when (domainModel) {
        AppPlatform.Android -> AppPlatformDTO.Android
        AppPlatform.IOS -> AppPlatformDTO.IOS
    }
}