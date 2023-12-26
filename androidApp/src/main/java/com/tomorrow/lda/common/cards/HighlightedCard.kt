package com.tomorrow.lda.common.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tomorrow.lda.common.AppSeparator

@Composable
fun HighlightedCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    image: String? = null,
    onClick: () -> Unit,
) = Box(
    modifier = modifier
        .clip(RoundedCornerShape(8.dp))
        .background(color = MaterialTheme.colorScheme.background)
        .clickable { onClick() }
) {
    Column(
        modifier = Modifier
            .padding(all = 16.dp)
            .padding(bottom = 18.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        image?.let {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface),
                model = it,
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall
        )

        Column {
            Spacer(modifier = Modifier.height(24.dp))
            AppSeparator(Modifier.width(72.dp))
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.secondary)
            )
        }
    }
}