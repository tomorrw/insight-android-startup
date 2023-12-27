package com.tomorrow.convenire.shared.domain.model

import kotlinx.coroutines.flow.Flow

data class QrCodeData(
    val user: User,
    val qrCode: String
)
