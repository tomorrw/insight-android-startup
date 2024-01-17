package com.tomorrow.convenire.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SalutationDTO {
    Mr,
    Ms,
    Mrs,
    Miss,
    Madam,
    Dr,
    Sir,
    Pr,
    Esq,

    @SerialName("")
    None
}