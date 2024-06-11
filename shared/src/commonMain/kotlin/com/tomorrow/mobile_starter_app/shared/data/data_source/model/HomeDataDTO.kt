package com.tomorrow.mobile_starter_app.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class HomeDataDTO(
    @SerialName("upcoming_events")
    val upcomingSessions: List<SessionDTO>? = null,
    @SerialName("todays_speakers")
    val todaySpeakers: List<SpeakerDTO>? = null,
    val posts: List<PostDTO>? = null,
    val ads: List<AdDTO>? = null,
) {
    fun isEmpty() = upcomingSessions == null &&
            todaySpeakers == null &&
            posts == null &&
            ads == null

    fun isNotEmpty() = !isEmpty()

}