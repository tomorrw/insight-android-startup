package com.tomorrow.lda.shared.data.data_source.mapper

import com.tomorrow.lda.shared.data.data_source.model.UpdateInfoDTO
import com.tomorrow.lda.shared.data.data_source.utils.EntityMapper
import com.tomorrow.lda.shared.data.data_source.utils.fromApiFormatToDate2
import com.tomorrow.lda.shared.domain.model.UpdateInfo
import com.tomorrow.lda.shared.domain.model.Version

class UpdateInfoMapper : EntityMapper<UpdateInfo, UpdateInfoDTO> {
    override fun mapFromEntity(entity: UpdateInfoDTO) = UpdateInfo(
        lastSupportedVersion = Version(entity.version),
        softUpdateDate = entity.softUpdateDate.fromApiFormatToDate2(),
        hardUpdateDate = entity.hardUpdateDate.fromApiFormatToDate2()
    )

    override fun mapToEntity(domainModel: UpdateInfo): UpdateInfoDTO {
        TODO("Not yet implemented")
    }

}
