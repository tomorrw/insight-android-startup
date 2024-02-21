package com.tomorrow.convenire.feature_listing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tomorrow.convenire.common.cards.InlineCardDisplay

@Composable
fun ListLoader() {
    LazyColumn {
        item { Spacer(Modifier.height(16.dp)) }

        items(5) {
            if (it != 0) Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFDAE6F1))
            )
            InlineCardDisplay(avatar = "", name = "")
        }
    }
}