package com.tomorrow.convenire.shared.domain.model

import kotlin.math.max

class Version(version: String?) : Comparable<Version?> {
    private val version: String

    fun get(): String {
        return version
    }

    override operator fun compareTo(other: Version?): Int {
        if (other == null) return 1
        val thisParts = this.get().split(".").toTypedArray()
        val thatParts = other.get().split(".").toTypedArray()
        val length: Int = max(thisParts.size, thatParts.size)
        for (i in 0 until length) {
            val thisPart = if (i < thisParts.size) thisParts[i].toInt() else 0
            val thatPart = if (i < thatParts.size) thatParts[i].toInt() else 0
            if (thisPart < thatPart) return -1
            if (thisPart > thatPart) return 1
        }
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        return if (this::class != other::class) false else this.compareTo(other as Version) == 0
    }

    override fun hashCode(): Int {
        return version.hashCode()
    }

    init {
        if (version == null) throw IllegalArgumentException("Version can not be null")
        if (!version.matches(Regex("[0-9]+(\\.[0-9]+)*"))) throw IllegalArgumentException("Invalid version format")
        this.version = version
    }
}