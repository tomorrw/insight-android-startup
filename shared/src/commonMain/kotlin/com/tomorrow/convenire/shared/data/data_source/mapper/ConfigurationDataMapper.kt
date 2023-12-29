package com.tomorrow.convenire.shared.data.data_source.mapper

import com.tomorrow.convenire.shared.data.data_source.model.ConfigurationDTO
import com.tomorrow.convenire.shared.data.data_source.utils.EntityMapper
import com.tomorrow.convenire.shared.data.data_source.utils.fromApiFormatToDate
import com.tomorrow.convenire.shared.domain.model.ConfigurationData

class ConfigurationDataMapper : EntityMapper<ConfigurationData, ConfigurationDTO> {
    override fun mapFromEntity(entity: ConfigurationDTO): ConfigurationData = ConfigurationData(
        title = entity.title,
        subTitle = entity.ticketSubTitle,
        startDate = entity.startDate?.fromApiFormatToDate()?.date,
        endDate = entity.endDate?.fromApiFormatToDate()?.date,
        description = entity.ticketDescription,
        showTicket = entity.showTicket,
        showExhibitionMap = entity.showExhibitionMap,
        showExhibitionOffers = entity.showExhibitionOffers,
        status = entity.ticketStatus,
    )

    override fun mapToEntity(domainModel: ConfigurationData): ConfigurationDTO {
        TODO("Not yet implemented")
    }
}