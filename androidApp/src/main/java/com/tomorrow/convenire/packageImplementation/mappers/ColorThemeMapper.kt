package com.tomorrow.convenire.packageImplementation.mappers

import com.tomorrow.kmmProjectStartup.data.utils.EntityMapper
import com.tomorrow.components.buttons.ColorTheme as PresentationModel
import com.tomorrow.convenire.shared.domain.model.ColorTheme as DomainModel

class ColorThemePresentationModelMapper : EntityMapper<PresentationModel, DomainModel> {
    override fun mapFromEntity(entity: DomainModel): PresentationModel = when (entity) {
        DomainModel.Light -> PresentationModel.Light
        DomainModel.Auto -> PresentationModel.Auto
        DomainModel.Dark -> PresentationModel.Dark
    }

    override fun mapToEntity(domainModel: PresentationModel): DomainModel = when (domainModel) {
        PresentationModel.Light -> DomainModel.Light
        PresentationModel.Auto -> DomainModel.Auto
        PresentationModel.Dark -> DomainModel.Dark
    }

}
