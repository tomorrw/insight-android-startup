package com.tomorrow.convenire.shared.data.data_source.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 *Transform String of format "2022-10-18 08:30:00" to
 * LocalDateTime "2022-10-18T08:30:00"
 *
 */
fun String.fromApiFormatToDate() = LocalDateTime.parse(this.replace(" ", "T"))

fun String.fromApiFormatToDate2() = this.toInstant().toLocalDateTime(TimeZone.currentSystemDefault())
