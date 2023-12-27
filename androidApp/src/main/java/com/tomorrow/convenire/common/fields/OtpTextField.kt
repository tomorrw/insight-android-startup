package com.tomorrow.convenire.common.fields

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun OtpTextField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpCount: Int = 4,
    enabled: Boolean = true,
    onOtpTextChange: (String, Boolean) -> Unit
) {
    BasicTextField(
        modifier = modifier,
        value = otpText,
        onValueChange = {
            if (it.length <= otpCount) {
                onOtpTextChange.invoke(it, it.length == otpCount)
            }
        },
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = modifier.fillMaxWidth()
            ) {
                repeat(otpCount) { index ->
                    Spacer(modifier = Modifier.width(16.dp))
                    CharView(
                        index = index,
                        text = otpText
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    )
}

@Composable
private fun CharView(
    index: Int,
    text: String
) {
    val isFocused = text.length == index
    val char = when {
        index == text.length -> "_"
        index > text.length -> "_"
        else -> text[index].toString()
    }
    Text(
        modifier = Modifier
            .width(40.dp)
            .border(
                2.dp, when {
                    isFocused -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.outline
                }, RoundedCornerShape(8.dp)
            )
            .padding(vertical = 8.dp),
        text = char,
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center
    )
}