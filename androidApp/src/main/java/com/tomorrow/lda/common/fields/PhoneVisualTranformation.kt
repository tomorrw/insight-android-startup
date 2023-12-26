package com.tomorrow.lda.common.fields

import android.telephony.PhoneNumberUtils
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.tomorrow.lda.shared.domain.utils.PhoneNumber

object PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalNumber = text.text
        val phone = PhoneNumber(originalNumber)
        val formatted = phone.getFormattedNumberInOriginalFormat() ?: return TransformedText(
            text,
            OffsetMapping.Identity
        )

        return TransformedText(
            AnnotatedString(formatted),
            object : OffsetMapping {
                val originalToTransformed = mutableListOf<Int>()
                val transformedToOriginal = mutableListOf<Int>()

                init {

                    // original =====> +97477935424     -> ['+','9','7','4','7','7','9','3','5','4','2','4']
                    //                              index: [ 0 , 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10, 11]
                    //                              index: [ 0 , 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10, 11, 12, 13]
                    // transformed ==> +974 7793 5424   -> ['+','9','7','4',' ','7','7','9','3',' ','5','4','2','4']

                    //               originalToTransformed [ 0 , 1 , 2 , 3 , 5 , 6 , 7 , 8 , 10, 11, 12, 13, 14]
                    //               transformedToOriginal [ 0 , 1 , 2 , 3 , 4 , 4 , 5 , 6 , 7 , 8 , 8 , 9 , 10, 11, 12]

                    // 14 and 12 exist to handle cursor at end

                    for (i in formatted.length - 1 downTo 0) {
                        if (
                            PhoneNumberUtils.isNonSeparator(formatted[i])
                            && originalToTransformed.size < originalNumber.length
                        ) originalToTransformed.prepend(i)

                        transformedToOriginal.prepend(originalNumber.length - originalToTransformed.size)
                    }

                    val remainingOffsets = originalNumber.length - originalToTransformed.size

                    if (remainingOffsets != 0) originalToTransformed.prependRepeated(
                        originalToTransformed.firstOrNull() ?: 0,
                        remainingOffsets
                    )

                    listOf(
                        transformedToOriginal,
                        originalToTransformed
                    ).forEach { it.add(it.maxOrNull()?.plus(1) ?: 0) }
                }

                override fun originalToTransformed(offset: Int): Int = originalToTransformed[offset]
                override fun transformedToOriginal(offset: Int): Int = transformedToOriginal[offset]
            })
    }

    private fun <T> MutableList<T>.prepend(element: T) {
        add(0, element)
    }


    private fun <T> MutableList<T>.prependAll(elements: Array<T>) {
        addAll(0, elements.toList())
    }

    private inline fun <reified T> MutableList<T>.prependRepeated(element: T, count: Int) {
        prependAll(Array(count) { element })
    }
}
