package com.tomorrow.convenire.shared.domain.model

import com.tomorrow.convenire.shared.data.data_source.utils.Loadable
import com.tomorrow.convenire.shared.data.data_source.utils.NotLoaded

class SpeakerDetail(
    id: String,
    firstName: String,
    lastName: String,
    title: String,
    image: String?,
    socialLinks: List<SocialLink>,
    val detailPages: Loadable<List<Page>> = NotLoaded(),
    nationality: Country? = null
) : Speaker(
    id,
    firstName,
    lastName,
    title,
    image,
    socialLinks,
    nationality
)