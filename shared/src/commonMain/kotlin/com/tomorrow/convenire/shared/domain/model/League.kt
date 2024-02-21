package com.tomorrow.convenire.shared.domain.model

class League(
    val name: String,
    val lecturesAttendedCount: Int,
    val lecturesRemaining: Int,
    val percentage: Float,
    val color: String
) {
    val totalNumberOfLectures = lecturesRemaining + lecturesAttendedCount
}