package com.tomorrow.lda.shared.domain.model

import com.tomorrow.lda.shared.data.data_source.utils.Loadable
import com.tomorrow.lda.shared.data.data_source.utils.NotLoaded

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