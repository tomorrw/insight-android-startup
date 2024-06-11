package com.tomorrow.convenire.packageImplementation.mappers

import com.tomorrow.convenire.shared.domain.model.Ad
import com.tomorrow.kmmProjectStartup.data.utils.EntityMapper
import com.tomorrow.carousel.Ad as AdCarouselModel
import com.tomorrow.convenire.shared.domain.model.Ad as AdDomainModel

class AdPresentationModelMapper: EntityMapper<AdCarouselModel, AdDomainModel> {
    override fun mapFromEntity(entity: AdDomainModel): AdCarouselModel = AdCarouselModel(
        image = entity.image,
        url = entity.url
    )

    override fun mapToEntity(domainModel: com.tomorrow.carousel.Ad): Ad {
        TODO("Not yet implemented")
    }


}