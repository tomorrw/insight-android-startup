package com.tomorrow.convenire.shared.domain.model

import com.tomorrow.convenire.shared.data.data_source.utils.Loadable
import com.tomorrow.convenire.shared.data.data_source.utils.NotLoaded

class SpeakerDetail(
    id: String,
    fullName: FullName,
    title: String,
    image: String?,
    socialLinks: List<SocialLink>,
    val detailPages: Loadable<List<Page>> = NotLoaded(),
    nationality: Country? = null
) : Speaker(
    id,
    fullName,
    title,
    image,
    socialLinks,
    nationality
)