package com.tomorrow.lda.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
class ActionDTO(
    val id: String,
    @SerialName("action_label")
    val actionLabel: String? = null,
    @SerialName("action_url")
    val actionUrl: String? = null,
    @SerialName("action_type")
    val actionType: ActionType? = null,
    @SerialName("is_primary")
    val isPrimary: Int = 0,
    @SerialName("is_disabled")
    val isDisabled: Int = 0,
){
    @Serializable
    enum class ActionType {
        @SerialName("open_link")
        OpenLink,
        @SerialName("api_hit")
        ApiHit
    }
}