package com.tomorrow.lda.shared.domain.model

import kotlinx.datetime.LocalDateTime

class UpdateInfo(
    var lastSupportedVersion: Version,
    var softUpdateDate: LocalDateTime,
    var hardUpdateDate: LocalDateTime,
)