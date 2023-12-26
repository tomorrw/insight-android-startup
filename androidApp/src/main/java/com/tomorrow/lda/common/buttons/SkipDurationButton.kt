package com.tomorrow.lda.common.buttons

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.tomorrow.lda.R

@Composable
fun Skip30Button(
    state: SkipDurationButtonState,
    onClick: () -> Unit
) {
    val seekIcon = when (state) {
        SkipDurationButtonState.Backward -> R.drawable.ic_replay
        SkipDurationButtonState.Forward -> R.drawable.ic_forward
    }

    IconButton(
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(seekIcon),
            contentDescription = null,
        )
    }
}

enum class SkipDurationButtonState {
    Backward,
    Forward,
}