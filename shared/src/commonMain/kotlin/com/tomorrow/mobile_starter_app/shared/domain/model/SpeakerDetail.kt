package com.tomorrow.mobile_starter_app.shared.domain.model

import com.tomorrow.kmmProjectStartup.data.utils.Loadable
import com.tomorrow.kmmProjectStartup.data.utils.NotLoaded
import com.tomorrow.kmmProjectStartup.domain.model.FullName
import com.tomorrow.kmmProjectStartup.domain.model.SocialLink

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