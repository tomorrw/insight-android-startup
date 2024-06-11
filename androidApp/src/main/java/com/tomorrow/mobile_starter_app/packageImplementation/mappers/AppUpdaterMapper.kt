package com.tomorrow.mobile_starter_app.packageImplementation.mappers

import com.tomorrow.appupdate.UpdateType
import com.tomorrow.kmmProjectStartup.data.utils.EntityMapper
import com.tomorrow.kmmProjectStartup.domain.model.AppConfig as AppConfigDomainModel
import com.tomorrow.appupdate.StoreInfo as StoreInfoPresentationModel
import com.tomorrow.appupdate.UpdateType as UpdatePresentationModel
import com.tomorrow.mobile_starter_app.shared.domain.use_cases.GetUpdateTypeUseCase.UpdateType as UpdateDomainModel

class AppUpdaterMapper: EntityMapper<UpdateDomainModel, UpdatePresentationModel> {
    override fun mapFromEntity(entity: UpdateType): UpdateDomainModel {
        TODO("Not yet implemented")
    }

    override fun mapToEntity(domainModel: UpdateDomainModel): UpdateType = when(domainModel){
        UpdateDomainModel.Forced -> UpdateType.Forced
        UpdateDomainModel.Flexible -> UpdateType.Flexible
        UpdateDomainModel.None -> UpdateType.None
    }
}

class StoreInfoMapper: EntityMapper<AppConfigDomainModel, StoreInfoPresentationModel> {
    override fun mapFromEntity(entity: StoreInfoPresentationModel): AppConfigDomainModel {
        TODO("Not yet implemented")
    }

    override fun mapToEntity(domainModel: AppConfigDomainModel): StoreInfoPresentationModel {
        return StoreInfoPresentationModel(
            name = domainModel.name,
            updateUrl = domainModel.updateUrl
        )
    }
}
