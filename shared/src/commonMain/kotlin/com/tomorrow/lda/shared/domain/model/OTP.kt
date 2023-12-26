package com.tomorrow.lda.shared.domain.model

class OTP(
    private val int1: Int,
    private val int2: Int,
    private val int3: Int,
    private val int4: Int,
) {
    fun toInt(): Int = toString().toInt()
    override fun toString() = "$int1$int2$int3$int4"

    companion object {
        fun fromString(string: String): OTP {
            if (string.length != 4 && string.any { !it.isDigit() }) throw Exception("OTP is not Valid")
            else return OTP(
                string[0].digitToInt(),
                string[1].digitToInt(),
                string[2].digitToInt(),
                string[3].digitToInt()
            )
        }
    }
}

