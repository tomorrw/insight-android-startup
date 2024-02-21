package com.tomorrow.convenire.shared.domain.model

// Primary constructor
class Duration(val seconds: Int) {
    constructor(seconds: Double) : this(seconds.toInt())
    constructor(millis: Long) : this((millis / 1000).toInt())

    init {
        if (seconds < 0) throw IllegalArgumentException("Duration argument `seconds` must not be negative")
    }

    fun toVideoFormatString(): String {
        val hours = (seconds % 86400) / 3600
        val minutes = ((seconds % 86400) % 3600) / 60
        val seconds = ((seconds % 86400) % 3600) % 60

        val minutesPadded = minutes.toString().padStart(2, '0')
        val secondsPadded = seconds.toString().padStart(2, '0')

        return if (hours == 0)
            "$minutes:$secondsPadded"
        else
            "$hours:$minutesPadded:$secondsPadded"
    }

    fun convertTo(unit: Unit) = convert(Unit.Second, unit, seconds.toDouble())

    companion object {
        fun convert(sourceUnit: Unit, targetUnit: Unit, value: Double): Double {
            return when (sourceUnit) {
                Unit.Millisecond -> when (targetUnit) {
                    Unit.Millisecond -> value
                    Unit.Second -> value / 1000
                    Unit.Minute -> value / 60000 // 60 * 1000
                    Unit.Hour -> value / 3600000  // 60 * 60 * 1000
                    Unit.Day -> value / 86400000 // 24 * 60 * 60 * 1000
                }
                Unit.Second -> when (targetUnit) {
                    Unit.Millisecond -> value * 1000
                    Unit.Second -> value
                    Unit.Minute -> value / 60
                    Unit.Hour -> value / 3600 // 60 * 60
                    Unit.Day -> value / 86400 // 24 * 60 * 60
                }
                Unit.Minute -> when (targetUnit) {
                    Unit.Millisecond -> value * 60000 // 60 * 1000
                    Unit.Second -> value * 60
                    Unit.Minute -> value
                    Unit.Hour -> value / 60
                    Unit.Day -> value / 1440 // 24 * 60
                }
                Unit.Hour -> when (targetUnit) {
                    Unit.Millisecond -> value * 3600000 // 60 * 60 * 1000
                    Unit.Second -> value * 36000 // 60 * 60
                    Unit.Minute -> value * 60
                    Unit.Hour -> value
                    Unit.Day -> value / 24
                }
                Unit.Day -> when (targetUnit) {
                    Unit.Millisecond -> value * 86400000 // 24 * 60 * 60 * 1000
                    Unit.Second -> value * 86400 // 24 * 60 * 60
                    Unit.Minute -> value * 1440 // 24 * 60
                    Unit.Hour -> value * 24
                    Unit.Day -> value
                }
            }
        }
    }

    enum class Unit {
        Millisecond,
        Second,
        Minute,
        Hour,
        Day,
    }
}