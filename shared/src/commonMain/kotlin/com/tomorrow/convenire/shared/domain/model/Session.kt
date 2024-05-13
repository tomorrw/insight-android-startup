package com.tomorrow.convenire.shared.domain.model

import com.tomorrow.convenire.shared.data.data_source.utils.Loadable
import com.tomorrow.convenire.shared.data.data_source.utils.NotLoaded
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

open class Session(
    val id: String,
    val title: String,
    val image: String,
    val topic: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val location: String,
    val companyId: String?,
    val detailPage: Loadable<Page> = NotLoaded(),
    val canAskQuestions: Boolean = false,
    val hasAttended: Boolean = false,
    val minutesAttended: Int? = null,
    val speakers: List<Speaker>,
    val actions: List<Action>,
    val numberOfAttendees: Loadable<Int> = NotLoaded(),
    val numberOfSeats: Loadable<Int> = NotLoaded(),
) {
    private fun LocalTime.toSimpleTime(): String = listOf(
        this.hour,
        this.minute,
    ).joinToString(":") { it.toString().padStart(2, '0') }

    fun getTimeInterval(): String =
        "${startTime.time.toSimpleTime()} - ${endTime.time.toSimpleTime()}"

    fun overlapsWithOtherSession(session: Session): Boolean {

        val timeZone = TimeZone.currentSystemDefault()
        val myStart = startTime.truncateToMinutes().toInstant(timeZone)
        val otherStart = session.startTime.truncateToMinutes().toInstant(timeZone)
        val otherEnd = session.endTime.truncateToMinutes().toInstant(timeZone)
        val myEnd = endTime.truncateToMinutes().toInstant(timeZone)

        return (myStart > otherStart && myStart < otherEnd) || (myEnd > otherStart && myEnd < otherEnd) || (myStart < otherStart && myEnd > otherEnd) || (myStart == otherStart)
    }

    private fun LocalDateTime.truncateToMinutes(): LocalDateTime {
        return LocalDateTime(
            year,
            month,
            dayOfMonth,
            hour,
            minute,
            0,
            0
        )
    }
    fun getAttendeesCount(): String? {
        val numberOfA = numberOfAttendees.getDataIfLoaded()
        val numberOfS = numberOfSeats.getDataIfLoaded()
        return if (numberOfA != null && numberOfS != null) {
            if (numberOfS == 0) "$numberOfA"
            else "$numberOfA / $numberOfS"
        } else null
    }

    fun getDateString(): String {
        return this.startTime.date.toString()
    }

    fun isSessionHappeningNow(): Boolean {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return now >= this.startTime && now <= this.endTime
    }

    data class Tag(val text: String, val color: String, val background: String)

    fun getTag(): Tag? {
        return if (this.isSessionHappeningNow() && this.minutesAttended !== null) Tag(
            "In Session",
            "#46BFD3",
            "$959EAD",
        )
        else if (this.isSessionHappeningNow()) Tag(
            "Now",
            "#46BFD3",
            "$959EAD",
        )
        else if (this.minutesAttended != null && !this.hasAttended) Tag(
            "Incomplete",
            "#CC5D5D",
            "#f4e7e7"
        )
        else if (this.hasAttended) Tag(
            "Attended",
            "#208705",
            "#d9ead5"
        )
        else null
    }

}