package com.tomorrow.convenire.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SessionDTO(
    val id: String,
    val title: String,
    val topic: String,
    val start: String,
    val end: String,
    val location: String,
    @SerialName("company_id")
    val companyId: String?,
    @SerialName("full_image_url")
    val image: String?,
    @SerialName("detail_page")
    val detailPage: DetailPageDTO? = null,
    @SerialName("social_links")
    val socialLinks: List<SocialLinkDTO>,
    val speakers: List<SpeakerDTO>? = null,
    @SerialName("questions_allowed")
    val canAskQuestions: Int? = 0,
    val actions: List<ActionDTO>? = listOf(),
    @SerialName("number_of_attendees")
    val numberOfAttendees: Int? = null,
    @SerialName("number_of_seats")
    val numberOfSeats: Int? = null,
    @SerialName("is_attended")
    val hasAttended: Int? = 0,
    @SerialName("is_completed")
    val isCompleted: Boolean? = null,
    @SerialName("minutes_attended")
    val minutesAttended: Int? = null
)