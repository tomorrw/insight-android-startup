package com.tomorrow.mobile_starter_app.shared.domain.model

import com.tomorrow.kmmProjectStartup.data.utils.Loadable
import com.tomorrow.kmmProjectStartup.data.utils.Loaded
import com.tomorrow.kmmProjectStartup.data.utils.NotLoaded

data class HomeData(
    val highlightedPost: Loadable<Post> = NotLoaded(),
    val todaySpeakers: Loadable<List<SpeakerDetail>> = NotLoaded(),
    val upcomingSessions: Loadable<List<Session>> = NotLoaded(),
    val posts: Loadable<List<Post>> = NotLoaded(),
    val highlightedAds: Loadable<List<Ad>>,
    val ads: Loadable<List<Ad>>
) {
    fun isNotLoaded() = listOf(
        highlightedPost,
        todaySpeakers,
        upcomingSessions,
        posts,
        ads
    ).all { it !is Loaded }

    fun isEmpty() = listOf(
        todaySpeakers,
        upcomingSessions,
        posts,
        ads
    ).all { it.getDataIfLoaded()?.isEmpty() == true }.and( highlightedPost.getDataIfLoaded() == null)

    fun isLoaded() = !isNotLoaded()
}