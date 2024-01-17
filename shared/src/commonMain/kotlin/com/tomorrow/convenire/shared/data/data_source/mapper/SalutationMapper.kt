package com.tomorrow.convenire.shared.data.data_source.mapper

import com.tomorrow.convenire.shared.data.data_source.model.SalutationDTO
import com.tomorrow.convenire.shared.data.data_source.utils.EntityMapper
import com.tomorrow.convenire.shared.domain.model.Salutation

class SalutationMapper : EntityMapper<Salutation, SalutationDTO> {
    override fun mapFromEntity(entity: SalutationDTO) = when (entity) {
        SalutationDTO.Mr -> Salutation.Mr
        SalutationDTO.Ms -> Salutation.Ms
        SalutationDTO.Mrs -> Salutation.Mrs
        SalutationDTO.Miss -> Salutation.Miss
        SalutationDTO.Madam -> Salutation.Madam
        SalutationDTO.Dr -> Salutation.Dr
        SalutationDTO.Sir -> Salutation.Sir
        SalutationDTO.Pr -> Salutation.Pr
        SalutationDTO.Esq -> Salutation.Esq
        SalutationDTO.None -> Salutation.None
    }

    override fun mapToEntity(domainModel: Salutation) = when (domainModel) {
        Salutation.Mr -> SalutationDTO.Mr
        Salutation.Ms -> SalutationDTO.Ms
        Salutation.Mrs -> SalutationDTO.Mrs
        Salutation.Miss -> SalutationDTO.Miss
        Salutation.Madam -> SalutationDTO.Madam
        Salutation.Dr -> SalutationDTO.Dr
        Salutation.Sir -> SalutationDTO.Sir
        Salutation.Pr -> SalutationDTO.Pr
        Salutation.Esq -> SalutationDTO.Esq
        Salutation.None -> SalutationDTO.None
    }
}