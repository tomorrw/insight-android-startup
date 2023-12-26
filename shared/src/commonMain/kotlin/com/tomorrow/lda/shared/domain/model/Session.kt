package com.tomorrow.lda.shared.domain.model

import com.tomorrow.lda.shared.data.data_source.utils.Loadable
import com.tomorrow.lda.shared.data.data_source.utils.NotLoaded
import kotlinx.datetime.*

class Session(
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
        val myStart = startTime.toInstant(timeZone)
        val otherStart = session.startTime.toInstant(timeZone)
        val otherEnd = session.endTime.toInstant(timeZone)
        val myEnd = endTime.toInstant(timeZone)

        return (myStart > otherStart && myStart < otherEnd) || (myEnd > otherStart && myEnd < otherEnd) || (myStart < otherStart && myEnd > otherEnd) || (myStart == otherStart)
    }

    fun getDateString(): String {
        return this.startTime.date.toString()
    }

    fun isSessionHappeningNow(): Boolean {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return now >= this.startTime && now <= this.endTime
    }
}