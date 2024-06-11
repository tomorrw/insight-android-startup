package com.tomorrow.mobile_starter_app.packageImplementation.mappers

import com.tomorrow.components.others.PersonPresentationModel
import com.tomorrow.mobile_starter_app.shared.domain.model.Speaker

fun Speaker.toPersonPresentationModel(onClick: (String) -> Unit) = PersonPresentationModel(
    id = this.id,
    fullName = this.fullName.getFullName(),
    image = this.image,
    countryFlag = this.nationality?.url,
    onClick = onClick
)