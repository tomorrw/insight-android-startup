package com.tomorrow.convenire.shared.domain.model

import com.tomorrow.convenire.shared.data.data_source.utils.Loadable
import com.tomorrow.convenire.shared.data.data_source.utils.Loaded
import com.tomorrow.convenire.shared.data.data_source.utils.NotLoaded

data class HomeData(
    val highlightedPost: Loadable<Post> = NotLoaded(),
    val todaySpeakers: Loadable<List<SpeakerDetail>> = NotLoaded(),
    val upcomingSessions: Loadable<List<Session>> = NotLoaded(),
    val posts: Loadable<List<Post>> = NotLoaded(),
    val highlightedAds:  Loadable<List<Ad>>,
    val ads: Loadable<List<Ad>>
) {
    fun isNotLoaded() = listOf(
        highlightedPost,
        todaySpeakers,
        upcomingSessions,
        posts,
        ads
    ).all { it !is Loaded }

    fun isLoaded() = !isNotLoaded()
}