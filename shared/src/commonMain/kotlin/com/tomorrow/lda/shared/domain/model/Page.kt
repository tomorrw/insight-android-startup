package com.tomorrow.lda.shared.domain.model

data class Page(
    val title: String,
    val sections: List<Section>,
) {
    sealed class Section {
        abstract val title: String

        class InfoSection(override val title: String, val description: String, val image: String? = null) : Section()
        class VideoSection(
            override val title: String,
            val videoUrl: String,
            val description: String
        ) : Section()

        class EventList(override val title: String, val events: List<Session>) : Section()
        class Speakers(override val title: String, val speakers: List<Speaker>, val shouldDisplayTitle: Boolean = false) : Section()
    }
}
