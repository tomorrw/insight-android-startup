package com.tomorrow.convenire.shared.data.data_source.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
enum class SalutationDTO {
    Mr,
    Ms,
    Mrs,
    Miss,
    Madam,
    @OptIn(ExperimentalSerializationApi::class)
    @JsonNames("Dr.", "Dr")
    Dr,
    Sir,
    Pr,
    Esq,

    @SerialName("")
    None
}