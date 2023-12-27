package com.tomorrow.convenire.shared.data.data_source.mapper

import com.tomorrow.convenire.shared.data.data_source.model.QrCodeDataDTO
import com.tomorrow.convenire.shared.data.data_source.utils.EntityMapper
import com.tomorrow.convenire.shared.data.data_source.utils.fromApiFormatToDate
import com.tomorrow.convenire.shared.domain.model.QrCodeData

class QrCodeDataMapper : EntityMapper<QrCodeData, QrCodeDataDTO> {
    override fun mapFromEntity(entity: QrCodeDataDTO): QrCodeData = QrCodeData(
        title = entity.conventionName,
        year = entity.conventionStartDate.fromApiFormatToDate().date.year,
        date = "${entity.conventionStartDate.fromApiFormatToDate().date.month.name} ${entity.conventionStartDate.fromApiFormatToDate().date.dayOfMonth} - ${entity.conventionEndDate.fromApiFormatToDate().date.dayOfMonth}",
        description = entity.ticketDescription,
    )

    override fun mapToEntity(domainModel: QrCodeData): QrCodeDataDTO {
        TODO("Not yet implemented")
    }
}