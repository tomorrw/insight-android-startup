package com.tomorrow.convenire.shared.domain.use_cases

import kotlin.math.max

object CompareStringsUseCase {
    private fun measureDamerauLevenshtein(a: CharSequence, b: CharSequence): Int {
        val cost = Array(a.length + 1) { IntArray(b.length + 1) }
        for (iA in 0..a.length) {
            cost[iA][0] = iA
        }
        for (iB in 0..b.length) {
            cost[0][iB] = iB
        }
        val mapCharAToIndex = hashMapOf<Char, Int>()

        for (iA in 1..a.length) {
            var prevMatchingBIndex = 0
            for (iB in 1..b.length) {
                val doesPreviousMatch = (a[iA - 1] == b[iB - 1])

                val possibleCosts = mutableListOf<Int>()
                if (doesPreviousMatch) {
                    // Perfect match cost.
                    possibleCosts.add(cost[iA - 1][iB - 1])
                } else {
                    // Substitution cost.
                    possibleCosts.add(cost[iA - 1][iB - 1] + 1)
                }
                // Insertion cost.
                possibleCosts.add(cost[iA][iB - 1] + 1)
                // Deletion cost.
                possibleCosts.add(cost[iA - 1][iB] + 1)

                // Transposition cost.
                val bCharIndexInA = mapCharAToIndex[b[iB - 1]] ?: 0
                if (bCharIndexInA != 0 && prevMatchingBIndex != 0) {
                    possibleCosts.add(
                        cost[bCharIndexInA - 1][prevMatchingBIndex - 1]
                                + (iA - bCharIndexInA - 1) + 1 + (iB - prevMatchingBIndex - 1)
                    )
                }

                cost[iA][iB] = possibleCosts.min()

                if (doesPreviousMatch) prevMatchingBIndex = iB
            }
            mapCharAToIndex[a[iA - 1]] = iA
        }
        return cost[a.length][b.length]
    }

    private fun measureHammingDistance(s1: String, s2: String): Int {
        if (s2.length < s1.length) return measureHammingDistance(s2, s1)

        var i = 0
        var count = 0
        while (i < s1.length) {
            if (s1[i] != s2[i]) count++
            i++
        }
        return count
    }

    fun findSimilarity(x: String, y: String): Double {
        val length = max(x.length, y.length).toDouble()
        return if (length > 0) {
            val distance: List<Int> = listOf(measureDamerauLevenshtein(x, y), measureHammingDistance(x, y))

            (length - distance.average()) / length
        } else 1.0
    }
}