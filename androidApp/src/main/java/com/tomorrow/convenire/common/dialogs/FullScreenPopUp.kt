package com.tomorrow.convenire.common.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex


@Composable
fun FullScreenPopUp(shouldPopup: MutableState<Boolean>, content: @Composable () -> Unit) {
    if (shouldPopup.value) {
        Box(
            Modifier
                .fillMaxSize()
                .zIndex(10f)
                .clickable { shouldPopup.value = false },
            contentAlignment = Alignment.Center
        ) {
            //dark Background
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
            )
            content()
        }
    }
}