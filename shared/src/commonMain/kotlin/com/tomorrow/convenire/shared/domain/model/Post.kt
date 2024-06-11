package com.tomorrow.convenire.shared.domain.model

import com.tomorrow.kmmProjectStartup.data.utils.Loadable
import com.tomorrow.kmmProjectStartup.data.utils.NotLoaded
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class Post(
    val id: String,
    val title: String,
    val description: String,
    val image: String?,
    val detailPage: Loadable<Page> = NotLoaded(),
    val isHighlighted: Boolean = false,
    val publishedAt: LocalDateTime,
    val company: Company? = null,
    val action: List<Action>
) {
    fun getHumanReadablePublishedAt(): String {
        val duration = (Clock.System.now() - publishedAt.toInstant(TimeZone.currentSystemDefault()))
        if (duration.inWholeDays > 14L) return "${duration.inWholeDays / 7L} weeks ago"
        if (duration.inWholeDays > 7L) return "${duration.inWholeDays / 7L} week ago"
        if (duration.inWholeDays == 1L) return "${duration.inWholeDays} day ago"
        if (duration.inWholeDays != 0L) return "${duration.inWholeDays} days ago"
        if (duration.inWholeHours == 1L) return "${duration.inWholeHours} hour ago"
        if (duration.inWholeHours != 0L) return "${duration.inWholeHours} hours ago"
        if (duration.inWholeMinutes == 1L) return "${duration.inWholeHours} minute ago"
        if (duration.inWholeMinutes != 0L) return "${duration.inWholeHours} minutes ago"
        return "now"
    }


}
