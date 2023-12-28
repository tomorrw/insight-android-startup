package com.tomorrow.convenire.shared.data.data_source.mapper

import com.tomorrow.convenire.shared.data.data_source.model.TicketDataDTO
import com.tomorrow.convenire.shared.data.data_source.utils.EntityMapper
import com.tomorrow.convenire.shared.data.data_source.utils.fromApiFormatToDate
import com.tomorrow.convenire.shared.domain.model.TicketData

class TicketDataMapper : EntityMapper<TicketData, TicketDataDTO> {
    override fun mapFromEntity(entity: TicketDataDTO): TicketData = TicketData(
        title = entity.conventionName,
        startDate = entity.conventionStartDate?.fromApiFormatToDate()?.date,
        endDate = entity.conventionEndDate?.fromApiFormatToDate()?.date,
        description = entity.ticketDescription,
        showTicket = entity.showTicket ?: true,
    )

    override fun mapToEntity(domainModel: TicketData): TicketDataDTO {
        TODO("Not yet implemented")
    }
}