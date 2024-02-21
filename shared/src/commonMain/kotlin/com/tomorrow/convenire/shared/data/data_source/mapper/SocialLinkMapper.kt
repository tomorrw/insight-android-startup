package com.tomorrow.convenire.shared.data.data_source.mapper

import com.tomorrow.convenire.shared.data.data_source.model.SocialLinkDTO
import com.tomorrow.convenire.shared.data.data_source.utils.EntityMapper
import com.tomorrow.convenire.shared.domain.model.SocialLink

class SocialLinkMapper : EntityMapper<SocialLink, SocialLinkDTO> {
    override fun mapFromEntity(entity: SocialLinkDTO) = SocialLink(
        url = entity.url, platform = when (entity.platform) {
            SocialLinkDTO.Platform.Instagram -> SocialLink.Platform.Instagram
            SocialLinkDTO.Platform.Website -> SocialLink.Platform.Website
            SocialLinkDTO.Platform.LinkedIn -> SocialLink.Platform.LinkedIn
            SocialLinkDTO.Platform.Facebook -> SocialLink.Platform.Facebook
            SocialLinkDTO.Platform.Tiktok -> SocialLink.Platform.Tiktok
            SocialLinkDTO.Platform.Twitter -> SocialLink.Platform.Twitter
            SocialLinkDTO.Platform.Youtube -> SocialLink.Platform.Youtube
            SocialLinkDTO.Platform.WhatsApp -> SocialLink.Platform.WhatsApp
            SocialLinkDTO.Platform.Phone -> SocialLink.Platform.Phone
        }
    )

    override fun mapToEntity(domainModel: SocialLink): SocialLinkDTO {
        TODO("Not yet implemented")
    }
}